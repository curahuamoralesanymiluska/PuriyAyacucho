package com.example.puriyayacucho.data

/**
 * Filtros aplicables a una lista de lugares.
 * Cada campo nulo significa "sin restricción".
 */
data class FiltroLugares(
    val comodidades: Set<Comodidad> = emptySet(),
    val minCamas: Int? = null,
    val minHuespedes: Int? = null,
    val minEstrellas: Int? = null,
    val precioMax: Double? = null,
    val precioMin: Double? = null,
    val minRating: Float? = null
) {
    val cantidadActivos: Int
        get() {
            var n = comodidades.size
            if (minCamas != null) n++
            if (minHuespedes != null) n++
            if (minEstrellas != null) n++
            if (precioMax != null) n++
            if (precioMin != null) n++
            if (minRating != null) n++
            return n
        }
    val estaVacio: Boolean get() = cantidadActivos == 0
}

/** Datos normalizados para poder filtrar cualquier categoría con la misma lógica. */
data class ItemFiltrable(
    val comodidades: Set<Comodidad> = emptySet(),
    val camas: Int? = null,
    val huespedesMax: Int? = null,
    val estrellas: Int? = null,
    val precioDesde: Double? = null,
    val rating: Float? = null
)

/**
 * Evalúa si un [ItemFiltrable] cumple con todos los filtros activos.
 * Regla: si un filtro numérico está activo y el dato del lugar es null
 * (sin confirmar), el lugar NO cumple.
 */
fun FiltroLugares.cumple(item: ItemFiltrable): Boolean {
    if (comodidades.isNotEmpty() && !item.comodidades.containsAll(comodidades)) return false
    if (minCamas != null && (item.camas == null || item.camas < minCamas)) return false
    if (minHuespedes != null && (item.huespedesMax == null || item.huespedesMax < minHuespedes)) return false
    if (minEstrellas != null && (item.estrellas == null || item.estrellas < minEstrellas)) return false
    if (precioMax != null && (item.precioDesde == null || item.precioDesde > precioMax)) return false
    if (precioMin != null && (item.precioDesde == null || item.precioDesde < precioMin)) return false
    if (minRating != null && (item.rating == null || item.rating < minRating)) return false
    return true
}

/**
 * Intenta leer el precio mínimo en soles desde textos como:
 * "S/. 25 - S/. 55", "S/ 120 por noche", "S/ 80".
 * Devuelve null si no puede parsearlo (por ejemplo "Consultar tarifa").
 */
fun parsearPrecioMin(texto: String?): Double? {
    if (texto.isNullOrBlank()) return null
    val regex = Regex("""S/\.?\s*(\d+(?:\.\d+)?)""")
    return regex.find(texto)?.groupValues?.get(1)?.toDoubleOrNull()
}
