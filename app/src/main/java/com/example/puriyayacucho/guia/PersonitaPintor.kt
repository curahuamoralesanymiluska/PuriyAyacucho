package com.example.puriyayacucho.guia

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Dibuja a la personita andina del viaje (chullo, poncho rosa con franja amarilla)
 * y la combi o el taxi en el que viaja. Se usa en dos lugares:
 * - la barra de progreso ([ProgresoViajeView]),
 * - el marcador animado del mapa ([MarcadoresMapa.personita]).
 *
 * [u] es la escala: 1 unidad = [u] píxeles (normalmente la densidad de pantalla).
 */
class PersonitaPintor(private val u: Float) {

    private val colorPiel = Color.parseColor("#F2C29B")
    private val colorPoncho = Color.parseColor("#FF3355")
    private val colorFranja = Color.parseColor("#FFC93C")
    private val colorPantalon = Color.parseColor("#2B2D42")
    private val colorChullo = Color.parseColor("#FF7A18")

    private val relleno = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val trazo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
    private val sombra = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.argb(40, 0, 0, 0) }
    private val rect = RectF()
    private val path = Path()

    /**
     * Personita caminando. [fase] es el ángulo del paso (en radianes): al aumentar,
     * las piernas y los brazos se balancean. Si [saltando], salta con los brazos arriba
     * ([salto] = altura del salto en unidades).
     */
    fun dibujarPersona(canvas: Canvas, x: Float, pies: Float, fase: Float, saltando: Boolean = false, salto: Float = 0f) {
        val alturaSalto = if (saltando) salto * u else abs(sin(fase)) * 1.6f * u

        // Sombra en el piso (se achica cuando salta)
        val anchoSombra = (9 * u) * (1 - alturaSalto / (24 * u)).coerceAtLeast(0.3f)
        rect.set(x - anchoSombra, pies + 2 * u, x + anchoSombra, pies + 5 * u)
        canvas.drawOval(rect, sombra)

        val cadera = pies - 13 * u - alturaSalto
        val angulo = if (saltando) 0.35f else sin(fase) * 0.5f

        // Piernas
        trazo.color = colorPantalon
        trazo.strokeWidth = 4 * u
        canvas.drawLine(x, cadera, x + sin(angulo) * 13 * u, cadera + cos(angulo) * 13 * u, trazo)
        canvas.drawLine(x, cadera, x - sin(angulo) * 13 * u, cadera + cos(angulo) * 13 * u, trazo)

        // Brazos (balanceo contrario a las piernas; arriba si salta)
        val hombro = cadera - 11 * u
        trazo.color = colorPiel
        trazo.strokeWidth = 3.2f * u
        if (saltando) {
            canvas.drawLine(x - 3 * u, hombro, x - 9 * u, hombro - 9 * u, trazo)
            canvas.drawLine(x + 3 * u, hombro, x + 9 * u, hombro - 9 * u, trazo)
        } else {
            canvas.drawLine(x, hombro, x - sin(angulo) * 10 * u, hombro + cos(angulo) * 10 * u, trazo)
            canvas.drawLine(x, hombro, x + sin(angulo) * 10 * u, hombro + cos(angulo) * 10 * u, trazo)
        }

        // Poncho (trapecio) con franja amarilla
        path.reset()
        path.moveTo(x - 4.5f * u, cadera - 13 * u)
        path.lineTo(x + 4.5f * u, cadera - 13 * u)
        path.lineTo(x + 7.5f * u, cadera + 1.5f * u)
        path.lineTo(x - 7.5f * u, cadera + 1.5f * u)
        path.close()
        relleno.color = colorPoncho
        canvas.drawPath(path, relleno)
        trazo.color = colorFranja
        trazo.strokeWidth = 1.8f * u
        canvas.drawLine(x - 6.2f * u, cadera - 4 * u, x + 6.2f * u, cadera - 4 * u, trazo)

        // Cabeza
        val cabezaY = cadera - 19 * u
        relleno.color = colorPiel
        canvas.drawCircle(x + 0.5f * u, cabezaY, 5.5f * u, relleno)
        relleno.color = colorPantalon
        canvas.drawCircle(x + 2.8f * u, cabezaY + 0.3f * u, 0.9f * u, relleno) // ojo
        dibujarChullo(canvas, x + 0.5f * u, cabezaY)
    }

    /** Gorro andino: media luna naranja, orejera, franja blanca y pompón. */
    private fun dibujarChullo(canvas: Canvas, cx: Float, cabezaY: Float) {
        relleno.color = colorChullo
        rect.set(cx - 6.2f * u, cabezaY - 7.5f * u, cx + 6.2f * u, cabezaY + 4.5f * u)
        canvas.drawArc(rect, 180f, 180f, true, relleno)
        path.reset()
        path.moveTo(cx - 6.2f * u, cabezaY - 1.5f * u)
        path.lineTo(cx - 3.8f * u, cabezaY - 1.5f * u)
        path.lineTo(cx - 5.4f * u, cabezaY + 4.5f * u)
        path.close()
        canvas.drawPath(path, relleno)
        trazo.color = Color.WHITE
        trazo.strokeWidth = 1.2f * u
        canvas.drawLine(cx - 5.4f * u, cabezaY - 3.2f * u, cx + 5.4f * u, cabezaY - 3.2f * u, trazo)
        relleno.color = colorFranja
        canvas.drawCircle(cx, cabezaY - 8 * u, 2 * u, relleno)
    }

    /**
     * Combi (minibús naranja con franja blanca y tres ventanas) o taxi (carrito amarillo con
     * letrero en el techo), con la personita asomada por la ventana. [fase] hace girar las
     * ruedas y rebotar la carrocería.
     */
    fun dibujarVehiculo(canvas: Canvas, x: Float, pies: Float, fase: Float, esTaxi: Boolean, enMovimiento: Boolean) {
        val rebote = if (enMovimiento) abs(sin(fase)) * 1.2f * u else 0f
        val colorCarro = Color.parseColor(if (esTaxi) MarcadoresMapa.COLOR_TAXI else MarcadoresMapa.COLOR_COMBI)
        val mitad = if (esTaxi) 19f else 22f

        rect.set(x - mitad * u, pies + 2 * u, x + mitad * u, pies + 5 * u)
        canvas.drawOval(rect, sombra)

        // Líneas de velocidad
        if (enMovimiento) {
            trazo.color = Color.parseColor("#C9C9D6")
            trazo.strokeWidth = 2 * u
            val avance = ((fase / (2 * Math.PI.toFloat())) % 1f + 1f) % 1f
            for (i in 0..2) {
                val desfase = ((avance + i * 0.33f) % 1f) * 8 * u
                val ly = pies - 16 * u + i * 5 * u
                canvas.drawLine(x - (mitad + 8) * u - desfase, ly, x - (mitad + 3) * u - desfase, ly, trazo)
            }
        }

        val abajo = pies - 3 * u - rebote
        val ruedas: FloatArray
        val cabezaX: Float
        val cabezaY: Float
        if (esTaxi) {
            // Carrito: chasis bajo + cabina más angosta
            relleno.color = colorCarro
            rect.set(x - 19 * u, abajo - 11 * u, x + 19 * u, abajo)
            canvas.drawRoundRect(rect, 4 * u, 4 * u, relleno)
            path.reset()
            path.moveTo(x - 12 * u, abajo - 10 * u)
            path.lineTo(x - 8 * u, abajo - 19 * u)
            path.lineTo(x + 7 * u, abajo - 19 * u)
            path.lineTo(x + 12 * u, abajo - 10 * u)
            path.close()
            canvas.drawPath(path, relleno)
            // Ventanas
            relleno.color = Color.parseColor("#D6F1FF")
            rect.set(x - 9.5f * u, abajo - 17.5f * u, x - 1 * u, abajo - 11.5f * u)
            canvas.drawRoundRect(rect, 1.5f * u, 1.5f * u, relleno)
            rect.set(x + 0.5f * u, abajo - 17.5f * u, x + 9 * u, abajo - 11.5f * u)
            canvas.drawRoundRect(rect, 1.5f * u, 1.5f * u, relleno)
            // Letrero de taxi en el techo, con cuadritos
            relleno.color = Color.parseColor("#2B2D42")
            rect.set(x - 5 * u, abajo - 22.5f * u, x + 4 * u, abajo - 19 * u)
            canvas.drawRoundRect(rect, 1 * u, 1 * u, relleno)
            // Franja oscura al costado
            canvas.drawRect(x - 17 * u, abajo - 7 * u, x + 17 * u, abajo - 6 * u, relleno)
            relleno.color = Color.WHITE
            canvas.drawRect(x - 3.5f * u, abajo - 21.6f * u, x - 1.5f * u, abajo - 19.9f * u, relleno)
            canvas.drawRect(x + 0.5f * u, abajo - 21.6f * u, x + 2.5f * u, abajo - 19.9f * u, relleno)
            ruedas = floatArrayOf(x - 11 * u, x + 11 * u)
            cabezaX = x - 5.5f * u
            cabezaY = abajo - 13.5f * u
            canvas.save()
            canvas.clipRect(x - 9.5f * u, abajo - 25 * u, x - 1 * u, abajo - 11.5f * u)
        } else {
            // Combi: minibús alargado
            val arriba = abajo - 20 * u
            relleno.color = colorCarro
            rect.set(x - 22 * u, arriba, x + 21 * u, abajo)
            canvas.drawRoundRect(rect, 6 * u, 6 * u, relleno)
            relleno.color = Color.WHITE
            canvas.drawRect(x - 22 * u, abajo - 7 * u, x + 21 * u, abajo - 5.2f * u, relleno)
            relleno.color = Color.parseColor("#D6F1FF")
            for (vx in floatArrayOf(-19f, -8f, 3f)) {
                rect.set(x + vx * u, arriba + 3 * u, x + (vx + 9) * u, arriba + 10 * u)
                canvas.drawRoundRect(rect, 2 * u, 2 * u, relleno)
            }
            // Parabrisas
            rect.set(x + 14 * u, arriba + 3 * u, x + 19.5f * u, arriba + 11 * u)
            canvas.drawRoundRect(rect, 2 * u, 2 * u, relleno)
            ruedas = floatArrayOf(x - 13 * u, x + 12 * u)
            cabezaX = x - 3.5f * u
            cabezaY = arriba + 8 * u
            canvas.save()
            canvas.clipRect(x - 8 * u, arriba - 10 * u, x + 1 * u, arriba + 10 * u)
        }
        // La personita asomada por la ventana (con su chullo)
        relleno.color = colorPiel
        canvas.drawCircle(cabezaX, cabezaY, 3.8f * u, relleno)
        canvas.restore()
        relleno.color = colorChullo
        rect.set(cabezaX - 4.3f * u, cabezaY - 5.2f * u, cabezaX + 4.3f * u, cabezaY + 2.8f * u)
        canvas.drawArc(rect, 180f, 180f, true, relleno)
        relleno.color = colorFranja
        canvas.drawCircle(cabezaX, cabezaY - 5.3f * u, 1.4f * u, relleno)
        // Faro
        relleno.color = Color.parseColor("#FFF3B0")
        canvas.drawCircle(x + (if (esTaxi) 17.5f else 19.5f) * u, abajo - (if (esTaxi) 7.5f else 9f) * u, 1.6f * u, relleno)
        // Ruedas con rayos que giran
        val giro = fase * 1.5f
        for (rx in ruedas) {
            relleno.color = colorPantalon
            canvas.drawCircle(rx, abajo, 4.5f * u, relleno)
            relleno.color = Color.parseColor("#B8B8C8")
            canvas.drawCircle(rx, abajo, 1.8f * u, relleno)
            trazo.color = Color.parseColor("#B8B8C8")
            trazo.strokeWidth = 1.1f * u
            canvas.drawLine(
                rx - cos(giro) * 3.6f * u, abajo - sin(giro) * 3.6f * u,
                rx + cos(giro) * 3.6f * u, abajo + sin(giro) * 3.6f * u, trazo
            )
        }
    }
}
