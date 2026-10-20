package com.example.puriyayacucho.guia

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Geometría para dibujar y animar el recorrido (Kotlin puro, sin Android).
 *
 * Como la app no usa la Directions API, los tramos de taxi son una recta entre dos
 * puntos. Para que no parezca que el taxi "atraviesa la ciudad en línea recta",
 * se dibujan con una curva suave, y la personita avanza por esa misma curva.
 */
object RutaGeometria {

    /** Puntos con los que se dibuja (y se anima) un tramo. */
    fun trazo(tramo: Tramo): List<Punto> =
        if (tramo.puntos.size == 2 && tramo.estilo == EstiloTramo.VEHICULO) curva(tramo.puntos[0], tramo.puntos[1])
        else tramo.puntos

    /** Curva cuadrática suave entre [a] y [b] (se arquea un [curvatura] de su largo). */
    fun curva(a: Punto, b: Punto, curvatura: Double = 0.14, segmentos: Int = 32): List<Punto> {
        val escalaLng = cos(Math.toRadians((a.lat + b.lat) / 2))
        // Trabajamos en un plano local: x = lng corregida, y = lat
        val ax = a.lng * escalaLng
        val bx = b.lng * escalaLng
        val dx = bx - ax
        val dy = b.lat - a.lat
        val largo = sqrt(dx * dx + dy * dy)
        if (largo == 0.0) return listOf(a, b)
        // Punto de control: el medio, desplazado en perpendicular
        val cx = (ax + bx) / 2 - dy / largo * largo * curvatura
        val cy = (a.lat + b.lat) / 2 + dx / largo * largo * curvatura
        return (0..segmentos).map { i ->
            val t = i.toDouble() / segmentos
            val u = 1 - t
            val x = u * u * ax + 2 * u * t * cx + t * t * bx
            val y = u * u * a.lat + 2 * u * t * cy + t * t * b.lat
            Punto(y, x / escalaLng)
        }
    }
}

/**
 * Una línea que se puede recorrer: da el punto y el rumbo a cierta fracción
 * (0 = inicio, 1 = final) y encuentra la fracción más cercana a un punto (GPS).
 */
class Recorrido(val puntos: List<Punto>) {

    private val acumulado: DoubleArray = DoubleArray(puntos.size).also { acc ->
        for (i in 1 until puntos.size) acc[i] = acc[i - 1] + Geo.distanciaMetros(puntos[i - 1], puntos[i])
    }

    val longitudMetros: Double get() = if (acumulado.isEmpty()) 0.0 else acumulado.last()

    fun puntoEn(fraccion: Double): Punto {
        if (puntos.isEmpty()) return Punto(0.0, 0.0)
        if (puntos.size == 1 || longitudMetros == 0.0) return puntos.first()
        val objetivo = fraccion.coerceIn(0.0, 1.0) * longitudMetros
        val i = indiceSegmento(objetivo)
        val a = puntos[i]
        val b = puntos[i + 1]
        val largo = acumulado[i + 1] - acumulado[i]
        val t = if (largo == 0.0) 0.0 else (objetivo - acumulado[i]) / largo
        return Punto(a.lat + (b.lat - a.lat) * t, a.lng + (b.lng - a.lng) * t)
    }

    /** Los puntos ya recorridos hasta esa fracción (para dibujar el "rastro"). */
    fun puntosHasta(fraccion: Double): List<Punto> {
        if (puntos.size < 2 || longitudMetros == 0.0) return puntos.take(1)
        val f = fraccion.coerceIn(0.0, 1.0)
        val i = indiceSegmento(f * longitudMetros)
        return puntos.subList(0, i + 1) + puntoEn(f)
    }

    /** Rumbo en grados (0 = norte, 90 = este) en esa fracción del recorrido. */
    fun rumboEn(fraccion: Double): Float {
        if (puntos.size < 2 || longitudMetros == 0.0) return 0f
        val i = indiceSegmento(fraccion.coerceIn(0.0, 1.0) * longitudMetros)
        val a = puntos[i]
        val b = puntos[i + 1]
        val y = (b.lng - a.lng) * cos(Math.toRadians(a.lat))
        val x = b.lat - a.lat
        val grados = Math.toDegrees(atan2(y, x))
        return ((grados + 360) % 360).toFloat()
    }

    /** Fracción del recorrido más cercana a [p] y la distancia (en metros) hasta él. */
    fun proyectar(p: Punto): Pair<Double, Double> {
        if (puntos.size < 2 || longitudMetros == 0.0) {
            return 0.0 to (puntos.firstOrNull()?.let { Geo.distanciaMetros(it, p) } ?: Double.MAX_VALUE)
        }
        var mejorFraccion = 0.0
        var mejorDistancia = Double.MAX_VALUE
        val escalaLng = cos(Math.toRadians(p.lat))
        for (i in 0 until puntos.size - 1) {
            val a = puntos[i]
            val b = puntos[i + 1]
            val abx = (b.lng - a.lng) * escalaLng
            val aby = b.lat - a.lat
            val apx = (p.lng - a.lng) * escalaLng
            val apy = p.lat - a.lat
            val largo2 = abx * abx + aby * aby
            val t = if (largo2 == 0.0) 0.0 else ((apx * abx + apy * aby) / largo2).coerceIn(0.0, 1.0)
            val q = Punto(a.lat + (b.lat - a.lat) * t, a.lng + (b.lng - a.lng) * t)
            val d = Geo.distanciaMetros(p, q)
            if (d < mejorDistancia) {
                mejorDistancia = d
                mejorFraccion = (acumulado[i] + (acumulado[i + 1] - acumulado[i]) * t) / longitudMetros
            }
        }
        return mejorFraccion to mejorDistancia
    }

    private fun indiceSegmento(metros: Double): Int {
        var i = 0
        while (i < puntos.size - 2 && acumulado[i + 1] < metros) i++
        return max(0, i)
    }
}
