package com.example.puriyayacucho

import android.content.Context
import android.content.Intent
import com.example.puriyayacucho.data.LugarTuristico

/**
 * Abre PlaceDetailsActivity con los mismos extras que usa LugarTuristicoAdapter.
 * Así todas las categorías nuevas reutilizan la pantalla de detalle existente.
 */
object DetalleNavigator {

    fun abrir(context: Context, lugar: LugarTuristico) {
        val intent = Intent(context, PlaceDetailsActivity::class.java).apply {
            putExtra("placeId", lugar.id)
            putExtra("latitude", lugar.coordenadas.first)
            putExtra("longitude", lugar.coordenadas.second)
            putExtra("placeName", lugar.nombre)
            putExtra("description", lugar.descripcion)
            putExtra("category", lugar.tipo)
            putExtra("location", lugar.ubicacion)
            putExtra("recommendation", lugar.recomendacion)
            putExtra("hours", lugar.horario)
            putExtra("videoUrl", lugar.videoUrl)

            when (val img = lugar.imagen) {
                is Int -> putExtra("imageRes", img)
                is String -> putExtra("imageUrl", img)
            }

            val galleryRes = ArrayList<Int>()
            val galleryUrls = ArrayList<String>()
            lugar.galeria.forEach { item ->
                when (item) {
                    is Int -> galleryRes.add(item)
                    is String -> galleryUrls.add(item)
                }
            }
            putIntegerArrayListExtra("galleryImages", galleryRes)
            putStringArrayListExtra("galleryUrls", galleryUrls)
        }
        context.startActivity(intent)
    }
}
