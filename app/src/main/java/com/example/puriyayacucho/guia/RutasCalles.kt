package com.example.puriyayacucho.guia

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

/**
 * Trazo por las calles reales para los tramos a pie y en taxi, para que la línea del
 * mapa (y la personita) sigan las calles en vez de cruzar las manzanas en línea recta.
 *
 * Usa el servidor público de rutas de OpenStreetMap (OSRM de FOSSGIS), gratis y sin clave.
 * Es para uso moderado: pocas consultas, con caché. Si no hay internet o el servidor no
 * responde, se usa el trazo aproximado de siempre ([RutaGeometria.trazo]).
 *
 * Datos de calles: © colaboradores de OpenStreetMap (ODbL).
 */
object RutasCalles {

    private const val SERVIDOR = "https://routing.openstreetmap.de"
    private const val TIEMPO_MAXIMO_MS = 7_000L
    private const val MAX_PUNTOS = 20
    private const val AGENTE = "PuriyAyacucho/1.0 (app turistica estudiantil, Ayacucho - Peru)"

    private val cache = ConcurrentHashMap<String, List<Punto>>()

    /**
     * Para cada tramo devuelve su trazo por calles. La clave del mapa es el índice del tramo
     * en [PlanRuta.tramos]. Los tramos de combi pasan por los puntos de su línea, así la
     * línea dibujada sigue la carretera real.
     */
    suspend fun trazos(plan: PlanRuta): Map<Int, List<Punto>> {
        val resultado = mutableMapOf<Int, List<Punto>>()
        // Una consulta tras otra (no en paralelo), para no cargar el servidor gratuito
        withTimeoutOrNull(TIEMPO_MAXIMO_MS) {
            plan.tramos.forEachIndexed { i, tramo ->
                if (tramo.puntos.size >= 2 && Geo.longitudMetros(tramo.puntos) >= 30) {
                    trazar(tramo.puntos, aPie = tramo.estilo == EstiloTramo.CAMINATA)?.let { resultado[i] = it }
                }
            }
        }
        return resultado
    }

    /** Trazo por calles que pasa por [puntos] (en orden), o null si no se pudo obtener. */
    suspend fun trazar(puntos: List<Punto>, aPie: Boolean): List<Punto>? {
        // OSRM acepta pocos puntos intermedios: si la línea tiene muchos, tomamos algunos
        val paradas = reducir(puntos, MAX_PUNTOS)
        val clave = (if (aPie) "pie" else "auto") + paradas.joinToString("|") {
            String.format(Locale.US, "%.5f,%.5f", it.lat, it.lng)
        }
        cache[clave]?.let { return it }
        val calles = withContext(Dispatchers.IO) { runCatching { consultar(paradas, aPie) }.getOrNull() }
        if (calles != null && calles.size >= 2) {
            // Une el trazo con los puntos exactos de inicio y fin (la calle puede quedar a unos metros)
            val completo = listOf(puntos.first()) + calles + puntos.last()
            cache[clave] = completo
            return completo
        }
        return null
    }

    /** Se queda con [maximo] puntos repartidos a lo largo de la lista (siempre el primero y el último). */
    fun reducir(puntos: List<Punto>, maximo: Int): List<Punto> {
        if (puntos.size <= maximo) return puntos
        val paso = (puntos.size - 1).toDouble() / (maximo - 1)
        return (0 until maximo).map { puntos[Math.round(it * paso).toInt()] }
    }

    private fun consultar(puntos: List<Punto>, aPie: Boolean): List<Punto>? {
        val perfil = if (aPie) "routed-foot" else "routed-car"
        // En viajes largos (Vilcashuamán) basta un trazo simplificado
        val detalle = if (Geo.longitudMetros(puntos) > 15_000) "simplified" else "full"
        val coordenadas = puntos.joinToString(";") { String.format(Locale.US, "%.6f,%.6f", it.lng, it.lat) }
        val url = URL("$SERVIDOR/$perfil/route/v1/driving/$coordenadas?overview=$detalle&geometries=geojson")
        val conexion = (url.openConnection() as HttpURLConnection).apply {
            connectTimeout = 5_000
            readTimeout = 6_000
            setRequestProperty("User-Agent", AGENTE)
        }
        try {
            if (conexion.responseCode != HttpURLConnection.HTTP_OK) return null
            val texto = conexion.inputStream.bufferedReader().use { it.readText() }
            return leerRespuesta(texto)
        } finally {
            conexion.disconnect()
        }
    }

    /** Lee la respuesta de OSRM: routes[0].geometry.coordinates = [[lng, lat], ...]. */
    fun leerRespuesta(json: String): List<Punto>? {
        val raiz = JSONObject(json)
        if (raiz.optString("code") != "Ok") return null
        val rutas = raiz.optJSONArray("routes") ?: return null
        if (rutas.length() == 0) return null
        val coords = rutas.getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates")
        return (0 until coords.length()).map { i ->
            val c = coords.getJSONArray(i)
            Punto(c.getDouble(1), c.getDouble(0))
        }
    }
}
