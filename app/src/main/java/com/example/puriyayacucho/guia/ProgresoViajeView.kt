package com.example.puriyayacucho.guia

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import kotlin.math.abs
import kotlin.math.sin

/**
 * Barra de progreso del viaje con una personita andina (con chullo y poncho)
 * que camina sobre la línea. En los tramos de combi o taxi la personita va
 * dentro del vehículo, y al llegar salta de alegría.
 */
class ProgresoViajeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    enum class Medio { A_PIE, COMBI, TAXI }

    /** Un tramo de la barra, de [inicio] a [fin] (fracciones de 0 a 1). */
    data class TramoBarra(val inicio: Float, val fin: Float, @ColorInt val color: Int, val medio: Medio)

    /** Un punto importante en la barra: inicio, paradero, parada o destino. */
    data class Hito(val posicion: Float, @DrawableRes val icono: Int, @ColorInt val color: Int)

    var tramos: List<TramoBarra> = emptyList()
        set(value) { field = value; invalidate() }

    var hitos: List<Hito> = emptyList()
        set(value) { field = value; invalidate() }

    /** De 0 (inicio) a 1 (llegada). */
    var progreso: Float = 0f
        set(value) { field = value.coerceIn(0f, 1f); invalidate() }

    /** Si es true, la personita mueve las piernas (o giran las ruedas). */
    var enMovimiento: Boolean = false
        set(value) { field = value; invalidate() }

    /** Al llegar, la personita salta con los brazos arriba. */
    var llego: Boolean = false
        set(value) { field = value; invalidate() }

    private val u = resources.displayMetrics.density
    private val inicioAnimacion = SystemClock.uptimeMillis()
    private val pintor = PersonitaPintor(u)

    private val pista = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 6 * u
        color = Color.parseColor("#ECECF3")
    }
    private val tramoPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 6 * u
    }
    private val puntitos = DashPathEffect(floatArrayOf(0.1f * u, 9 * u), 0f)
    private val relleno = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val trazo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val alto = resolveSize((96 * u).toInt(), heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), alto)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val x0 = 30 * u
        val x1 = width - 30 * u
        val y = height - 20 * u
        val t = (SystemClock.uptimeMillis() - inicioAnimacion) / 1000f

        // 1. Pista gris de fondo
        canvas.drawLine(x0, y, x1, y, pista)

        // 2. Tramos: lo recorrido con color fuerte, lo que falta más suave
        tramos.forEach { tramo ->
            val a = x0 + (x1 - x0) * tramo.inicio
            val b = x0 + (x1 - x0) * tramo.fin
            val corte = (x0 + (x1 - x0) * progreso).coerceIn(a, b)
            tramoPaint.pathEffect = if (tramo.medio == Medio.A_PIE) puntitos else null
            tramoPaint.color = tramo.color
            tramoPaint.alpha = 255
            if (corte > a) canvas.drawLine(a, y, corte, y, tramoPaint)
            tramoPaint.alpha = 80
            if (b > corte) canvas.drawLine(corte, y, b, y, tramoPaint)
        }
        tramoPaint.alpha = 255

        // 3. Hitos (inicio, paraderos, paradas y destino)
        hitos.forEach { hito ->
            val hx = x0 + (x1 - x0) * hito.posicion
            val visitado = hito.posicion <= progreso + 0.001f
            relleno.color = if (visitado) hito.color else Color.WHITE
            canvas.drawCircle(hx, y, 11 * u, relleno)
            trazo.color = hito.color
            trazo.strokeWidth = 2.5f * u
            canvas.drawCircle(hx, y, 11 * u, trazo)
            ContextCompat.getDrawable(context, hito.icono)?.mutate()?.let { d ->
                d.setTint(if (visitado) Color.WHITE else hito.color)
                val m = (6.5f * u).toInt()
                d.setBounds(hx.toInt() - m, y.toInt() - m, hx.toInt() + m, y.toInt() + m)
                d.draw(canvas)
            }
        }

        // 4. La personita
        val px = x0 + (x1 - x0) * progreso
        val medio = tramos.firstOrNull { progreso >= it.inicio && progreso <= it.fin }?.medio ?: Medio.A_PIE
        val pies = y - 13 * u
        when {
            llego -> dibujarPersona(canvas, px, pies, t, saltando = true)
            medio == Medio.A_PIE -> dibujarPersona(canvas, px, pies, t, saltando = false)
            else -> dibujarVehiculo(canvas, px, pies, t, medio == Medio.TAXI)
        }

        if (enMovimiento || llego) postInvalidateOnAnimation()
    }

    private fun dibujarPersona(canvas: Canvas, x: Float, pies: Float, t: Float, saltando: Boolean) {
        val fase = if (enMovimiento || saltando) t * 2f * Math.PI.toFloat() * 1.8f else 0f
        val salto = if (saltando) abs(sin(t * Math.PI.toFloat() * 2.4f)) * 9f else 0f
        pintor.dibujarPersona(canvas, x, pies, fase, saltando, salto)
    }

    private fun dibujarVehiculo(canvas: Canvas, x: Float, pies: Float, t: Float, esTaxi: Boolean) {
        val fase = if (enMovimiento) t * 2f * Math.PI.toFloat() * 2.2f else 0f
        pintor.dibujarVehiculo(canvas, x, pies, fase, esTaxi, enMovimiento)
    }
}
