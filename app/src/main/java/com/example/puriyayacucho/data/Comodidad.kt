package com.example.puriyayacucho.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.puriyayacucho.R

enum class GrupoComodidad { MUSICA_Y_AMBIENTE, FAMILIA, COMIDA, SERVICIOS, AMBIENTE }

/**
 * Comodidades verificadas con fuente pública para filtrar lugares.
 * Cada comodidad solo aparece en las categorías donde tiene sentido.
 */
enum class Comodidad(
    @StringRes val etiqueta: Int,
    @DrawableRes val icono: Int,
    val categorias: Set<CategoriaLugar>,
    val grupo: GrupoComodidad
) {
    // ── Bares ──────────────────────────────────────────────────────────────
    ROCK_EN_VIVO(
        R.string.com_rock_en_vivo, R.drawable.ic_volume,
        setOf(CategoriaLugar.BAR), GrupoComodidad.MUSICA_Y_AMBIENTE
    ),
    MUSICA_AYACUCHANA(
        R.string.com_musica_ayacuchana, R.drawable.ic_volume,
        setOf(CategoriaLugar.BAR), GrupoComodidad.MUSICA_Y_AMBIENTE
    ),
    MUSICA_EN_VIVO(
        R.string.com_musica_en_vivo, R.drawable.ic_volume,
        setOf(CategoriaLugar.BAR), GrupoComodidad.MUSICA_Y_AMBIENTE
    ),
    DISCOTECA(
        R.string.com_discoteca, R.drawable.ic_bar,
        setOf(CategoriaLugar.BAR), GrupoComodidad.MUSICA_Y_AMBIENTE
    ),
    KARAOKE(
        R.string.com_karaoke, R.drawable.ic_volume,
        setOf(CategoriaLugar.BAR), GrupoComodidad.MUSICA_Y_AMBIENTE
    ),
    CERVEZA_ARTESANAL(
        R.string.com_cerveza_artesanal, R.drawable.ic_bar,
        setOf(CategoriaLugar.BAR), GrupoComodidad.MUSICA_Y_AMBIENTE
    ),
    // ── Cafés ──────────────────────────────────────────────────────────────
    CAFE_ESPECIALIDAD(
        R.string.com_cafe_especialidad, R.drawable.ic_food,
        setOf(CategoriaLugar.CAFE), GrupoComodidad.AMBIENTE
    ),
    CAFE_DE_LA_REGION(
        R.string.com_cafe_de_la_region, R.drawable.ic_food,
        setOf(CategoriaLugar.CAFE), GrupoComodidad.AMBIENTE
    ),
    POSTRES(
        R.string.com_postres, R.drawable.ic_food,
        setOf(CategoriaLugar.CAFE, CategoriaLugar.RESTAURANTE), GrupoComodidad.COMIDA
    ),
    DESAYUNOS(
        R.string.com_desayunos, R.drawable.ic_food,
        setOf(CategoriaLugar.CAFE, CategoriaLugar.RESTAURANTE), GrupoComodidad.COMIDA
    ),
    VISTA_A_LA_PLAZA(
        R.string.com_vista_a_la_plaza, R.drawable.ic_place,
        setOf(CategoriaLugar.CAFE), GrupoComodidad.AMBIENTE
    ),
    // ── Familia ────────────────────────────────────────────────────────────
    ZONA_JUEGOS_NINOS(
        R.string.com_zona_juegos_ninos, R.drawable.ic_persona,
        setOf(CategoriaLugar.RESTAURANTE, CategoriaLugar.HOTEL), GrupoComodidad.FAMILIA
    ),
    MENU_INFANTIL(
        R.string.com_menu_infantil, R.drawable.ic_persona,
        setOf(CategoriaLugar.RESTAURANTE), GrupoComodidad.FAMILIA
    ),
    APTO_NINOS(
        R.string.com_apto_ninos, R.drawable.ic_persona,
        setOf(CategoriaLugar.HOTEL, CategoriaLugar.AIRBNB), GrupoComodidad.FAMILIA
    ),
    // ── Comida / hospedaje ─────────────────────────────────────────────────
    DESAYUNO_INCLUIDO(
        R.string.com_desayuno_incluido, R.drawable.ic_food,
        setOf(CategoriaLugar.HOTEL), GrupoComodidad.COMIDA
    ),
    DESAYUNO_DISPONIBLE(
        R.string.com_desayuno_disponible, R.drawable.ic_food,
        setOf(CategoriaLugar.HOTEL), GrupoComodidad.COMIDA
    ),
    COMIDA_TIPICA(
        R.string.com_comida_tipica, R.drawable.ic_food,
        setOf(CategoriaLugar.RESTAURANTE), GrupoComodidad.COMIDA
    ),
    OPCION_VEGETARIANA(
        R.string.com_opcion_vegetariana, R.drawable.ic_nature,
        setOf(CategoriaLugar.RESTAURANTE, CategoriaLugar.CAFE), GrupoComodidad.COMIDA
    ),
    RESTAURANTE_PROPIO(
        R.string.com_restaurante_propio, R.drawable.ic_food,
        setOf(CategoriaLugar.HOTEL), GrupoComodidad.SERVICIOS
    ),
    // ── Servicios generales ────────────────────────────────────────────────
    WIFI(
        R.string.com_wifi, R.drawable.ic_wifi,
        setOf(CategoriaLugar.HOTEL, CategoriaLugar.AIRBNB, CategoriaLugar.RESTAURANTE,
              CategoriaLugar.BAR, CategoriaLugar.CAFE), GrupoComodidad.SERVICIOS
    ),
    ESTACIONAMIENTO(
        R.string.com_estacionamiento, R.drawable.ic_parking,
        setOf(CategoriaLugar.HOTEL, CategoriaLugar.AIRBNB, CategoriaLugar.RESTAURANTE), GrupoComodidad.SERVICIOS
    ),
    AGUA_CALIENTE(
        R.string.com_agua_caliente, R.drawable.ic_hotel,
        setOf(CategoriaLugar.HOTEL, CategoriaLugar.AIRBNB), GrupoComodidad.SERVICIOS
    ),
    RECEPCION_24H(
        R.string.com_recepcion_24h, R.drawable.ic_hotel,
        setOf(CategoriaLugar.HOTEL), GrupoComodidad.SERVICIOS
    ),
    ACEPTA_MASCOTAS(
        R.string.com_acepta_mascotas, R.drawable.ic_favorite,
        setOf(CategoriaLugar.HOTEL, CategoriaLugar.AIRBNB, CategoriaLugar.CAFE), GrupoComodidad.SERVICIOS
    ),
    COCINA_EQUIPADA(
        R.string.com_cocina_equipada, R.drawable.ic_food,
        setOf(CategoriaLugar.AIRBNB), GrupoComodidad.SERVICIOS
    ),
    LAVADORA(
        R.string.com_lavadora, R.drawable.ic_hotel,
        setOf(CategoriaLugar.AIRBNB), GrupoComodidad.SERVICIOS
    ),
    TERRAZA(
        R.string.com_terraza, R.drawable.ic_place,
        setOf(CategoriaLugar.BAR, CategoriaLugar.RESTAURANTE), GrupoComodidad.MUSICA_Y_AMBIENTE
    ),
    DELIVERY(
        R.string.com_delivery, R.drawable.ic_food,
        setOf(CategoriaLugar.RESTAURANTE), GrupoComodidad.SERVICIOS
    ),
    PAGO_CON_TARJETA(
        R.string.com_pago_con_tarjeta, R.drawable.ic_check,
        setOf(CategoriaLugar.RESTAURANTE, CategoriaLugar.HOTEL), GrupoComodidad.SERVICIOS
    ),
    ACCESIBLE(
        R.string.com_accesible, R.drawable.ic_accessibility,
        setOf(CategoriaLugar.RESTAURANTE, CategoriaLugar.HOTEL), GrupoComodidad.SERVICIOS
    );

    companion object {
        fun paraCategoria(cat: CategoriaLugar) = entries.filter { cat in it.categorias }
    }
}
