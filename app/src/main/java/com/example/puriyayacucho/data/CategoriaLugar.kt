package com.example.puriyayacucho.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.puriyayacucho.R

/**
 * Las 8 categorías de la app. Cada una tiene sus colores (chip, pin y degradado),
 * ícono, texto visible y desplazamiento de ID (para que los IDs no choquen
 * entre categorías en Favoritos y Reservas).
 */
enum class CategoriaLugar(
    val clave: String,
    /** Color principal (chips, pines del mapa, inicio del degradado). */
    val colorHex: String,
    /** Color claro para el final del degradado de las tarjetas de Inicio. */
    val colorClaroHex: String,
    @DrawableRes val icono: Int,
    @StringRes val etiqueta: Int,
    val offsetId: Int
) {
    TEMPLO("templos", "#FF3355", "#FF8197", R.drawable.ic_cat_iglesia, R.string.chip_temples, 0),
    MUSEO("museos", "#7B4DFF", "#B197FC", R.drawable.ic_cat_museo, R.string.chip_museums, 2000),
    RESTAURANTE("comida", "#FF7A18", "#FFB463", R.drawable.ic_cat_comida, R.string.chip_food, 1000),
    NATURALEZA("naturaleza", "#12B886", "#5FE0B5", R.drawable.ic_cat_naturaleza, R.string.chip_nature, 3000),
    HOTEL("hoteles", "#3B6CF6", "#7FA2FF", R.drawable.ic_cat_hotel, R.string.chip_hotels, 6000),
    AIRBNB("airbnb", "#0EA5C6", "#5ED3EA", R.drawable.ic_cat_airbnb, R.string.chip_airbnb, 7000),
    BAR("bares", "#C2255C", "#F06595", R.drawable.ic_cat_bar, R.string.chip_bars, 8000),
    RECREACION("recreacion", "#E69A00", "#FFC93C", R.drawable.ic_cat_recreacion, R.string.chip_recreation, 9000),
    CAFE("cafes", "#8D5A3B", "#C8A27C", R.drawable.ic_cat_comida, R.string.chip_cafes, 10000);

    companion object {
        fun desdeClave(clave: String?): CategoriaLugar? = entries.firstOrNull { it.clave == clave }
    }
}
