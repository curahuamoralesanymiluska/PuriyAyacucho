package com.example.puriyayacucho.guia

import kotlin.math.max

/** Un tramo del viaje que la personita recorre (caminar, combi o taxi). */
data class SegmentoViaje(
    val paso: Paso,
    val estilo: EstiloTramo,
    val recorrido: Recorrido,
    val minutos: Double,
    /** Minuto del viaje en que empieza este tramo. */
    val inicioMin: Double
) {
    val finMin: Double get() = inicioMin + minutos
}

/** Dónde está la personita en un momento del viaje. */
data class EstadoViaje(
    val indice: Int,
    val fraccionTramo: Double,
    val punto: Punto,
    val rumbo: Float,
    /** De 0 (inicio) a 1 (llegada). */
    val progreso: Double
)

/**
 * Convierte un [PlanRuta] en un viaje que se puede animar minuto a minuto
 * (Kotlin puro, para poder probarlo). Cada tramo dura lo que dice su paso, así
 * la personita camina despacio y en combi o taxi va más rápido.
 */
class Viaje(
    plan: PlanRuta,
    /** Trazos por calles (índice del tramo → puntos), de [RutasCalles]. Opcional. */
    trazosCalles: Map<Int, List<Punto>> = emptyMap()
) {

    val segmentos: List<SegmentoViaje>
    val totalMin: Double

    init {
        val movimientos = plan.pasos.filter {
            it.tipo == TipoPaso.CAMINAR || it.tipo == TipoPaso.TRANSPORTE || it.tipo == TipoPaso.TAXI
        }
        var minuto = 0.0
        segmentos = movimientos.zip(plan.tramos).mapIndexed { i, (paso, tramo) ->
            val duracion = max(1, paso.minutos).toDouble()
            val puntos = trazosCalles[i] ?: RutaGeometria.trazo(tramo)
            SegmentoViaje(paso, tramo.estilo, Recorrido(puntos), duracion, minuto)
                .also { minuto += duracion }
        }
        totalMin = minuto
    }

    /** Todos los puntos del viaje (para dibujar la línea completa). */
    val puntos: List<Punto> get() = segmentos.flatMap { it.recorrido.puntos }

    fun estadoEn(minuto: Double): EstadoViaje {
        if (segmentos.isEmpty()) return EstadoViaje(0, 0.0, Punto(0.0, 0.0), 0f, 1.0)
        val m = minuto.coerceIn(0.0, totalMin)
        val indice = segmentos.indexOfFirst { m <= it.finMin }.let { if (it < 0) segmentos.lastIndex else it }
        val seg = segmentos[indice]
        val fraccion = if (seg.minutos == 0.0) 1.0 else ((m - seg.inicioMin) / seg.minutos).coerceIn(0.0, 1.0)
        return EstadoViaje(
            indice = indice,
            fraccionTramo = fraccion,
            punto = seg.recorrido.puntoEn(fraccion),
            rumbo = seg.recorrido.rumboEn(fraccion),
            progreso = if (totalMin == 0.0) 1.0 else m / totalMin
        )
    }

    /**
     * Minuto del viaje que corresponde a una ubicación GPS. Nunca retrocede de [desdeMin]
     * (para que la personita no salte hacia atrás). Devuelve también la distancia a la ruta.
     *
     * Solo mira los tramos que aún no terminan y, si la ruta pasa dos veces cerca del mismo
     * lugar (ida y vuelta por la misma calle), elige el tramo más próximo en el viaje y no
     * uno posterior: así la personita no se adelanta. [toleranciaM] es cuánto más lejos puede
     * estar ese tramo anterior respecto del más cercano de todos.
     */
    fun proyectar(p: Punto, desdeMin: Double, toleranciaM: Double = 30.0): Pair<Double, Double> {
        val candidatos = segmentos.filter { it.finMin >= desdeMin }.ifEmpty { segmentos.takeLast(1) }
            .map { seg ->
                val (fraccion, distancia) = seg.recorrido.proyectar(p)
                (seg.inicioMin + fraccion * seg.minutos) to distancia
            }
        if (candidatos.isEmpty()) return desdeMin to Double.MAX_VALUE
        val masCerca = candidatos.minOf { it.second }
        val (minuto, distancia) = candidatos.first { it.second <= masCerca + toleranciaM }
        return max(minuto, desdeMin) to distancia
    }

    /** Metros que faltan desde ese minuto hasta la llegada (con las mismas distancias de los pasos). */
    fun metrosRestantes(minuto: Double): Double {
        val estado = estadoEn(minuto)
        val actual = segmentos.getOrNull(estado.indice) ?: return 0.0
        val enTramo = actual.paso.metros * (1 - estado.fraccionTramo)
        return enTramo + segmentos.drop(estado.indice + 1).sumOf { it.paso.metros }
    }
}
