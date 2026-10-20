package com.example.puriyayacucho.data

import androidx.annotation.StringRes
import com.example.puriyayacucho.R
import com.example.puriyayacucho.guia.Geo
import com.example.puriyayacucho.guia.Punto

/** Qué figura dibuja la reconstrucción sobre la cámara. */
enum class TipoReconstruccion { RECINTO_WARI, USHNO_INCA, OBELISCO }

/** Punto que el usuario puede tocar en la vista RA. x/y van de 0 a 1 (relativos a la pantalla). */
data class PuntoInteresRA(
    @StringRes val titulo: Int,
    @StringRes val descripcion: Int,
    val x: Float,
    val y: Float
)

data class SitioRA(
    val id: Int,
    @StringRes val nombre: Int,
    val punto: Punto,
    val radioActivacionMetros: Double = 300.0,
    val tipo: TipoReconstruccion,
    @StringRes val narracion: Int,
    val puntosInteres: List<PuntoInteresRA>,
    /** Modelo 3D para ARCore (en app/src/main/assets). TODO: reemplazar por el modelo real. */
    val modelo3D: String,
    /** Tamaño (en metros) con el que se muestra el modelo en ARCore. */
    val tamanoMetros: Float
)

/**
 * Sitios con experiencia de Realidad Aumentada.
 * TODO: verificar coordenadas exactas en campo y ajustar el radio de activación.
 */
object SitioRAFactory {

    val sitios = listOf(
        SitioRA(
            id = 1,
            nombre = R.string.ra_site_wari,
            punto = Punto(-13.059466, -74.199535),
            tipo = TipoReconstruccion.RECINTO_WARI,
            narracion = R.string.ra_wari_narration,
            modelo3D = "models/recinto_wari.glb",
            tamanoMetros = 3.0f,
            puntosInteres = listOf(
                PuntoInteresRA(R.string.ra_wari_poi1_title, R.string.ra_wari_poi1_desc, 0.5f, 0.42f),
                PuntoInteresRA(R.string.ra_wari_poi2_title, R.string.ra_wari_poi2_desc, 0.2f, 0.62f)
            )
        ),
        SitioRA(
            id = 2,
            nombre = R.string.ra_site_quinua,
            punto = Punto(-13.0190, -74.1130),
            radioActivacionMetros = 2_500.0,
            tipo = TipoReconstruccion.OBELISCO,
            narracion = R.string.ra_quinua_narration,
            modelo3D = "models/obelisco_quinua.glb",
            tamanoMetros = 2.5f,
            puntosInteres = listOf(
                PuntoInteresRA(R.string.ra_quinua_poi1_title, R.string.ra_quinua_poi1_desc, 0.5f, 0.3f),
                PuntoInteresRA(R.string.ra_quinua_poi2_title, R.string.ra_quinua_poi2_desc, 0.75f, 0.7f)
            )
        ),
        SitioRA(
            id = 3,
            nombre = R.string.ra_site_vilcas,
            punto = Punto(-13.7104, -74.0467),
            radioActivacionMetros = 500.0,
            tipo = TipoReconstruccion.USHNO_INCA,
            narracion = R.string.ra_vilcas_narration,
            modelo3D = "models/ushno_vilcas.glb",
            tamanoMetros = 3.0f,
            puntosInteres = listOf(
                PuntoInteresRA(R.string.ra_vilcas_poi1_title, R.string.ra_vilcas_poi1_desc, 0.5f, 0.36f),
                PuntoInteresRA(R.string.ra_vilcas_poi2_title, R.string.ra_vilcas_poi2_desc, 0.22f, 0.66f)
            )
        )
    )

    /** Sitio RA que corresponde a un lugar de la app (si está a menos de [maxMetros]). */
    fun paraLugar(lat: Double, lng: Double, maxMetros: Double = 600.0): SitioRA? =
        sitios.minByOrNull { Geo.distanciaMetros(it.punto, Punto(lat, lng)) }
            ?.takeIf { Geo.distanciaMetros(it.punto, Punto(lat, lng)) <= maxMetros }

    fun porId(id: Int): SitioRA? = sitios.firstOrNull { it.id == id }
}
