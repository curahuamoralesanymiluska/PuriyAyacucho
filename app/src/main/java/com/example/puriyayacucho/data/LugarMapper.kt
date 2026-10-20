package com.example.puriyayacucho.data

import android.content.Context
import com.example.puriyayacucho.R

/** Un lugar de cualquier categoría, convertido al modelo común LugarTuristico. */
data class LugarIndexado(
    val categoria: CategoriaLugar,
    val lugar: LugarTuristico
)

// Conversión de cada modelo al modelo común LugarTuristico (funciones de extensión).

/** Texto al final de la descripción: de dónde salen los datos y si la ubicación es aproximada. */
private fun notaFuente(ctx: Context, fuente: String, aproximada: Boolean): String {
    val partes = mutableListOf<String>()
    if (aproximada) partes += ctx.getString(R.string.label_approx_location)
    if (fuente.isNotBlank()) partes += "${ctx.getString(R.string.label_source)}: $fuente"
    return if (partes.isEmpty()) "" else "\n\n" + partes.joinToString("\n")
}

fun Museo.toLugarTuristico(ctx: Context) = LugarTuristico(
    id = id + CategoriaLugar.MUSEO.offsetId,
    nombre = nombre,
    descripcion = descripcion,
    tipo = ctx.getString(R.string.cat_museum),
    horario = horario,
    ubicacion = ubicacion,
    recomendacion = ctx.getString(R.string.rec_recommended),
    imagen = imagen,
    videoUrl = videoUrl,
    coordenadas = coordenadas,
    galeria = galeria
)

fun Restaurante.toLugarTuristico(ctx: Context) = LugarTuristico(
    id = id + CategoriaLugar.RESTAURANTE.offsetId,
    nombre = nombre,
    descripcion = "$descripcion ${ctx.getString(R.string.label_dishes)}: ${platos.joinToString(", ")}",
    tipo = ctx.getString(R.string.cat_restaurant),
    horario = horario,
    ubicacion = ubicacion,
    recomendacion = ctx.getString(R.string.rec_try_dishes),
    imagen = imagen,
    videoUrl = videoUrl,
    coordenadas = coordenadas,
    telefono = telefono,
    galeria = galeria,
    itemFiltrable = ItemFiltrable(
        comodidades = comodidades,
        precioDesde = parsearPrecioMin(precio),
        rating = rating
    )
)

fun LugarNatural.toLugarTuristico(ctx: Context) = LugarTuristico(
    id = id + CategoriaLugar.NATURALEZA.offsetId,
    nombre = nombre,
    descripcion = "$descripcion ${ctx.getString(R.string.label_altitude)}: $altitud. ${ctx.getString(R.string.label_access)}: $acceso",
    tipo = ctx.getString(R.string.cat_nature),
    horario = ctx.getString(R.string.label_free_access),
    ubicacion = ubicacion,
    recomendacion = "${ctx.getString(R.string.label_best_season)}: $mejorEpoca",
    imagen = imagen,
    videoUrl = videoUrl,
    coordenadas = coordenadas,
    galeria = galeria
)

fun Hospedaje.toLugarTuristico(ctx: Context): LugarTuristico {
    val esHotel = tipo == TipoHospedaje.HOTEL
    val cat = if (esHotel) CategoriaLugar.HOTEL else CategoriaLugar.AIRBNB
    val textoServicios = if (servicios.isEmpty()) "" else "\n\n${ctx.getString(R.string.label_services)}: ${servicios.joinToString(" · ")}"
    val textoTelefono = if (telefono == "N/A") "" else "\n${ctx.getString(R.string.label_phone)}: $telefono"
    return LugarTuristico(
        id = id + cat.offsetId,
        nombre = nombre,
        descripcion = "$subtipo · $descripcion$textoServicios$textoTelefono" + notaFuente(ctx, fuente, coordenadasAproximadas),
        tipo = ctx.getString(if (esHotel) R.string.cat_hotel else R.string.cat_airbnb),
        horario = precioNoche,
        ubicacion = ubicacion,
        recomendacion = estrellas?.let { ctx.resources.getQuantityString(R.plurals.estrellas_cantidad, it, it) } ?: "",
        imagen = imagen,
        coordenadas = coordenadas,
        telefono = telefono,
        galeria = galeria,
        itemFiltrable = ItemFiltrable(
            comodidades = comodidades,
            camas = camas,
            huespedesMax = huespedesMax,
            estrellas = estrellas,
            precioDesde = parsearPrecioMin(precioNoche),
            rating = rating
        )
    )
}

fun Bar.toLugarTuristico(ctx: Context) = LugarTuristico(
    id = id + CategoriaLugar.BAR.offsetId,
    nombre = nombre,
    descripcion = "$descripcion\n\n$estilo · $precio" +
        (if (telefono == "N/A") "" else "\n${ctx.getString(R.string.label_phone)}: $telefono") +
        notaFuente(ctx, fuente, coordenadasAproximadas),
    tipo = ctx.getString(R.string.cat_bar),
    horario = horario,
    ubicacion = ubicacion,
    recomendacion = "",
    imagen = imagen,
    videoUrl = videoUrl,
    coordenadas = coordenadas,
    telefono = telefono,
    galeria = galeria,
    itemFiltrable = ItemFiltrable(
        comodidades = comodidades,
        precioDesde = parsearPrecioMin(precio),
        rating = rating
    )
)

fun LugarRecreativo.toLugarTuristico(ctx: Context) = LugarTuristico(
    id = id + CategoriaLugar.RECREACION.offsetId,
    nombre = nombre,
    descripcion = "$descripcion\n\n${ctx.getString(R.string.label_activities)}: ${actividades.joinToString(" · ")}" +
        notaFuente(ctx, fuente, coordenadasAproximadas),
    tipo = ctx.getString(R.string.cat_recreation),
    horario = horario,
    ubicacion = "$ubicacion · $distanciaDesdeHuamanga",
    recomendacion = "${ctx.getString(R.string.label_entry)}: $precioEntrada",
    imagen = imagen,
    videoUrl = videoUrl,
    coordenadas = coordenadas,
    galeria = galeria
)

fun Cafe.toLugarTuristico(ctx: Context) = LugarTuristico(
    id = id + CategoriaLugar.CAFE.offsetId,
    nombre = nombre,
    descripcion = "$descripcion\n\n${ctx.getString(R.string.label_phone)}: $telefono" + notaFuente(ctx, fuente, coordenadasAproximadas),
    tipo = ctx.getString(R.string.chip_cafes),
    horario = horario,
    ubicacion = ubicacion,
    recomendacion = precio,
    imagen = imagen,
    coordenadas = coordenadas,
    telefono = telefono,
    itemFiltrable = ItemFiltrable(comodidades = comodidades, rating = rating)
)

/**
 * Convierte cada modelo (Museo, Restaurante, Hospedaje...) a LugarTuristico
 * para reutilizar la misma pantalla de detalle, la búsqueda global y la guía.
 * Reemplaza el mapeo repetido que había en ExploreFragment.
 */
object LugarMapper {

    /** Lista de una sola categoría, ya convertida. */
    fun deCategoria(ctx: Context, categoria: CategoriaLugar): List<LugarTuristico> = when (categoria) {
        CategoriaLugar.TEMPLO -> LugarTuristicoFactory.obtenerLugaresTuristicosAyacucho(ctx)
        CategoriaLugar.MUSEO -> MuseoFactory.obtenerMuseosAyacucho(ctx).map { it.toLugarTuristico(ctx) }
        CategoriaLugar.RESTAURANTE -> RestauranteFactory.obtenerRestaurantesAyacucho(ctx).map { it.toLugarTuristico(ctx) }
        CategoriaLugar.NATURALEZA -> LugarNaturalFactory.obtenerLugaresNaturalesAyacucho(ctx).map { it.toLugarTuristico(ctx) }
        CategoriaLugar.HOTEL -> HospedajeFactory.obtenerHoteles().map { it.toLugarTuristico(ctx) }
        CategoriaLugar.AIRBNB -> HospedajeFactory.obtenerAirbnb().map { it.toLugarTuristico(ctx) }
        CategoriaLugar.BAR -> BarFactory.obtenerBares().map { it.toLugarTuristico(ctx) }
        CategoriaLugar.RECREACION -> RecreacionFactory.obtenerLugaresRecreativos().map { it.toLugarTuristico(ctx) }
        CategoriaLugar.CAFE -> CafeFactory.obtenerCafes().map { it.toLugarTuristico(ctx) }
    }

    /** Todos los lugares de la app (8 categorías). Se usa en la búsqueda y en la guía. */
    fun todos(ctx: Context): List<LugarIndexado> =
        CategoriaLugar.entries.flatMap { cat -> deCategoria(ctx, cat).map { LugarIndexado(cat, it) } }
}
