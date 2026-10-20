package com.example.puriyayacucho.guia

import com.example.puriyayacucho.data.LineaTransporte
import com.example.puriyayacucho.data.Paradero
import kotlin.math.ceil
import kotlin.math.roundToInt

enum class ModoViaje { TRANSPORTE, A_PIE, TAXI }

enum class TipoPaso { CAMINAR, TRANSPORTE, TAXI, PARADA, LLEGADA }

enum class EstiloTramo { CAMINATA, VEHICULO }

/** Un lugar por el que pasa el recorrido (origen, paradas intermedias o destino). */
data class Waypoint(val nombre: String, val punto: Punto)

/** Un paso de la línea de tiempo que ve el usuario. El texto se arma en la Activity (para traducirlo). */
data class Paso(
    val tipo: TipoPaso,
    val destinoNombre: String,
    val metros: Double = 0.0,
    val minutos: Int = 0,
    val linea: LineaTransporte? = null,
    val paraderoSubida: Paradero? = null,
    val paraderoBajada: Paradero? = null,
    /** Punto al que lleva este paso (para abrir la navegación). */
    val puntoDestino: Punto? = null
)

/** Un segmento de la ruta dibujado en el mapa. */
data class Tramo(val puntos: List<Punto>, val estilo: EstiloTramo)

data class PlanRuta(
    val pasos: List<Paso>,
    val tramos: List<Tramo>,
    val minutosTotales: Int,
    val tarifaTotal: Double,
    val distanciaMetros: Double,
    /** true si se pidió transporte público pero no hay línea cerca y se propuso taxi. */
    val sinTransporteDisponible: Boolean = false
) {
    /** Todos los puntos de la ruta, en orden. */
    val puntos: List<Punto> get() = tramos.flatMap { it.puntos }
}

/**
 * Arma un recorrido entre varios puntos usando las líneas de transporte locales.
 * Es Kotlin puro (sin Android) para poder probarlo con pruebas unitarias.
 *
 * Más adelante se puede reemplazar el cálculo de caminata/taxi por la
 * Directions API de Google implementando [DirectionsProvider].
 */
class RutaPlanner(
    private val lineas: List<LineaTransporte>,
    private val paraderos: List<Paradero>
) {

    companion object {
        const val VELOCIDAD_CAMINATA_M_POR_MIN = 83.3   // ~5 km/h
        const val FACTOR_CALLES = 1.3                  // las calles no son línea recta
        const val MAX_CAMINATA_A_PARADERO_M = 2_000.0
        const val CAMINATA_DIRECTA_MAX_M = 800.0        // si está cerca, mejor caminar
        const val ESPERA_TRANSPORTE_MIN = 5
        const val VELOCIDAD_TAXI_KMH = 25.0
        const val TAXI_TARIFA_BASE = 5.0               // TODO: verificar tarifas reales
        const val TAXI_TARIFA_POR_KM = 1.5
        /** Nombres de las paradas de los micros (paran donde les hagas la señal). */
        const val ESQUINA_SUBIDA = "la esquina más cercana de su ruta"
        const val ESQUINA_BAJADA = "la esquina más cercana a tu destino"
    }

    /** Planifica un recorrido que pasa por todos los [waypoints] en orden (mínimo 2). */
    fun planificar(waypoints: List<Waypoint>, modo: ModoViaje): PlanRuta {
        require(waypoints.size >= 2) { "Se necesitan al menos origen y destino" }

        val pasos = mutableListOf<Paso>()
        val tramos = mutableListOf<Tramo>()
        var minutos = 0
        var tarifa = 0.0
        var distancia = 0.0
        var sinTransporte = false

        waypoints.zipWithNext().forEachIndexed { i, (desde, hasta) ->
            val tramo = planificarTramo(desde, hasta, modo)
            pasos += tramo.pasos
            tramos += tramo.tramos
            minutos += tramo.minutosTotales
            tarifa += tramo.tarifaTotal
            distancia += tramo.distanciaMetros
            sinTransporte = sinTransporte || tramo.sinTransporteDisponible

            val esUltimo = i == waypoints.size - 2
            pasos += Paso(
                tipo = if (esUltimo) TipoPaso.LLEGADA else TipoPaso.PARADA,
                destinoNombre = hasta.nombre,
                puntoDestino = hasta.punto
            )
        }
        return PlanRuta(pasos, tramos, minutos, redondear(tarifa), distancia, sinTransporte)
    }

    /** Planifica un solo tramo entre dos puntos. */
    fun planificarTramo(origen: Waypoint, destino: Waypoint, modo: ModoViaje): PlanRuta {
        val metrosDirectos = Geo.distanciaMetros(origen.punto, destino.punto) * FACTOR_CALLES
        return when {
            modo == ModoViaje.A_PIE -> caminar(origen, destino)
            modo == ModoViaje.TAXI -> taxi(origen, destino)
            metrosDirectos <= CAMINATA_DIRECTA_MAX_M -> caminar(origen, destino)
            else -> transportePublico(origen, destino) ?: taxi(origen, destino).copy(sinTransporteDisponible = true)
        }
    }

    private fun caminar(origen: Waypoint, destino: Waypoint): PlanRuta {
        val metros = Geo.distanciaMetros(origen.punto, destino.punto) * FACTOR_CALLES
        val min = minutosCaminando(metros)
        return PlanRuta(
            pasos = listOf(Paso(TipoPaso.CAMINAR, destino.nombre, metros, min, puntoDestino = destino.punto)),
            tramos = listOf(Tramo(listOf(origen.punto, destino.punto), EstiloTramo.CAMINATA)),
            minutosTotales = min,
            tarifaTotal = 0.0,
            distanciaMetros = metros
        )
    }

    private fun taxi(origen: Waypoint, destino: Waypoint): PlanRuta {
        val metros = Geo.distanciaMetros(origen.punto, destino.punto) * FACTOR_CALLES
        val min = ceil(metros / 1000.0 / VELOCIDAD_TAXI_KMH * 60).toInt().coerceAtLeast(1)
        val precio = tarifaTaxi(metros)
        return PlanRuta(
            pasos = listOf(Paso(TipoPaso.TAXI, destino.nombre, metros, min, puntoDestino = destino.punto)),
            tramos = listOf(Tramo(listOf(origen.punto, destino.punto), EstiloTramo.VEHICULO)),
            minutosTotales = min,
            tarifaTotal = precio,
            distanciaMetros = metros
        )
    }

    /** Busca la línea con menos tiempo total. Devuelve null si ninguna sirve. */
    private fun transportePublico(origen: Waypoint, destino: Waypoint): PlanRuta? {
        var mejor: PlanRuta? = null

        for (linea in lineas) {
            val (subida, bajada) = if (linea.paradaLibre) {
                paradasLibres(linea, origen.punto, destino.punto) ?: continue
            } else {
                val paraderosLinea = paraderos.filter { linea.id in it.lineas }
                if (paraderosLinea.size < 2) continue
                val s = paraderosLinea.minByOrNull { Geo.distanciaMetros(origen.punto, it.punto) } ?: continue
                val b = paraderosLinea.minByOrNull { Geo.distanciaMetros(destino.punto, it.punto) } ?: continue
                s to b
            }
            if (subida.id == bajada.id) continue

            val caminata1 = Geo.distanciaMetros(origen.punto, subida.punto) * FACTOR_CALLES
            val caminata2 = Geo.distanciaMetros(bajada.punto, destino.punto) * FACTOR_CALLES
            if (caminata1 > MAX_CAMINATA_A_PARADERO_M || caminata2 > MAX_CAMINATA_A_PARADERO_M) continue

            val recorridoVehiculo = subRecorrido(linea, subida, bajada)
            val metrosVehiculo = Geo.longitudMetros(recorridoVehiculo)
            val minVehiculo = ceil(metrosVehiculo / 1000.0 / linea.velocidadKmh * 60).toInt() + ESPERA_TRANSPORTE_MIN
            val min1 = minutosCaminando(caminata1)
            val min2 = minutosCaminando(caminata2)

            val plan = PlanRuta(
                pasos = listOf(
                    Paso(TipoPaso.CAMINAR, subida.nombre, caminata1, min1, puntoDestino = subida.punto),
                    Paso(
                        TipoPaso.TRANSPORTE, bajada.nombre, metrosVehiculo, minVehiculo,
                        linea = linea, paraderoSubida = subida, paraderoBajada = bajada,
                        puntoDestino = bajada.punto
                    ),
                    Paso(TipoPaso.CAMINAR, destino.nombre, caminata2, min2, puntoDestino = destino.punto)
                ),
                tramos = listOf(
                    Tramo(listOf(origen.punto, subida.punto), EstiloTramo.CAMINATA),
                    Tramo(recorridoVehiculo, EstiloTramo.VEHICULO),
                    Tramo(listOf(bajada.punto, destino.punto), EstiloTramo.CAMINATA)
                ),
                minutosTotales = min1 + minVehiculo + min2,
                tarifaTotal = linea.tarifa,
                distanciaMetros = caminata1 + metrosVehiculo + caminata2
            )
            if (mejor == null || plan.minutosTotales < mejor.minutosTotales) mejor = plan
        }
        return mejor
    }

    /**
     * Micros de la ciudad: se suben y bajan en el punto de su recorrido más cercano al origen
     * y al destino (paran donde les hagas la señal). Devuelve null si ambos puntos coinciden.
     */
    private fun paradasLibres(linea: LineaTransporte, origen: Punto, destino: Punto): Pair<Paradero, Paradero>? {
        val ruta = Geo.densificar(linea.recorrido, 50.0)
        val i = Geo.indiceMasCercano(origen, ruta)
        val j = Geo.indiceMasCercano(destino, ruta)
        if (i == j) return null
        return Paradero(-(linea.id * 10_000 + i), ESQUINA_SUBIDA, ruta[i], listOf(linea.id)) to
            Paradero(-(linea.id * 10_000 + j), ESQUINA_BAJADA, ruta[j], listOf(linea.id))
    }

    /** Parte del recorrido de la línea entre dos paraderos (en el sentido que corresponda). */
    private fun subRecorrido(linea: LineaTransporte, subida: Paradero, bajada: Paradero): List<Punto> {
        val i = Geo.indiceMasCercano(subida.punto, linea.recorrido)
        val j = Geo.indiceMasCercano(bajada.punto, linea.recorrido)
        val medio = if (i <= j) linea.recorrido.subList(i, j + 1) else linea.recorrido.subList(j, i + 1).reversed()
        return listOf(subida.punto) + medio + listOf(bajada.punto)
    }

    fun minutosCaminando(metros: Double): Int =
        ceil(metros / VELOCIDAD_CAMINATA_M_POR_MIN).toInt().coerceAtLeast(1)

    fun tarifaTaxi(metros: Double): Double =
        redondear(TAXI_TARIFA_BASE + metros / 1000.0 * TAXI_TARIFA_POR_KM)

    /** Redondea a 0.50 soles hacia arriba. */
    private fun redondear(soles: Double): Double = ceil(soles * 2) / 2.0
}

/** Punto de extensión para conectar la Directions API de Google en el futuro. */
interface DirectionsProvider {
    suspend fun rutaCaminando(origen: Punto, destino: Punto): List<Punto>?
}

/** Formatea metros como "350 m" o "1.2 km". */
fun formatearDistancia(metros: Double): String =
    if (metros < 1000) "${(metros / 10).roundToInt() * 10} m" else String.format(java.util.Locale.US, "%.1f km", metros / 1000)
