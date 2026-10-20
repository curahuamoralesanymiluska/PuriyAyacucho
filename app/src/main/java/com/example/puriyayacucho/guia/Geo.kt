package com.example.puriyayacucho.guia

import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt

/** Un punto geográfico simple (sin dependencias de Android, para poder probarlo). */
data class Punto(val lat: Double, val lng: Double)

object Geo {

    private const val RADIO_TIERRA_M = 6_371_000.0

    /** Distancia en línea recta entre dos puntos (fórmula de Haversine), en metros. */
    fun distanciaMetros(a: Punto, b: Punto): Double {
        val dLat = Math.toRadians(b.lat - a.lat)
        val dLng = Math.toRadians(b.lng - a.lng)
        val h = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(a.lat)) * cos(Math.toRadians(b.lat)) * sin(dLng / 2) * sin(dLng / 2)
        return 2 * RADIO_TIERRA_M * asin(sqrt(h))
    }

    /** Longitud total de una línea formada por varios puntos, en metros. */
    fun longitudMetros(puntos: List<Punto>): Double =
        puntos.zipWithNext().sumOf { (a, b) -> distanciaMetros(a, b) }

    /** Agrega puntos intermedios cada [pasoMetros] para medir distancias a la ruta con precisión. */
    fun densificar(puntos: List<Punto>, pasoMetros: Double = 100.0): List<Punto> {
        if (puntos.size < 2) return puntos
        val resultado = mutableListOf(puntos.first())
        puntos.zipWithNext().forEach { (a, b) ->
            val tramos = max(1, (distanciaMetros(a, b) / pasoMetros).toInt())
            for (i in 1..tramos) {
                val t = i.toDouble() / tramos
                resultado.add(Punto(a.lat + (b.lat - a.lat) * t, a.lng + (b.lng - a.lng) * t))
            }
        }
        return resultado
    }

    /** Distancia mínima de un punto a cualquier punto de una ruta (ya densificada). */
    fun distanciaMinimaARuta(p: Punto, rutaDensa: List<Punto>): Double =
        rutaDensa.minOfOrNull { distanciaMetros(p, it) } ?: Double.MAX_VALUE

    /** Índice del punto de la lista más cercano a [p]. */
    fun indiceMasCercano(p: Punto, puntos: List<Punto>): Int =
        puntos.indices.minByOrNull { distanciaMetros(p, puntos[it]) } ?: 0
}
