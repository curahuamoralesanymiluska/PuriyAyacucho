package com.example.puriyayacucho.guia

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

/** Lluvia de confeti para celebrar la llegada. Llama a [lanzar] para empezar. */
class ConfetiView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private class Papelito(
        var x: Float, var y: Float,
        val vx: Float, var vy: Float,
        var giro: Float, val velocidadGiro: Float,
        val ancho: Float, val alto: Float,
        val color: Int
    )

    private val colores = listOf("#FF3355", "#FF7A18", "#FFC93C", "#12B886", "#3B6CF6", "#7B4DFF", "#F06595")
        .map { Color.parseColor(it) }
    private val papelitos = mutableListOf<Papelito>()
    private val pincel = Paint(Paint.ANTI_ALIAS_FLAG)
    private val u = resources.displayMetrics.density
    private var ultimoCuadro = 0L
    private var finEn = 0L

    fun lanzar() {
        papelitos.clear()
        repeat(110) {
            papelitos += Papelito(
                x = Random.nextFloat() * width,
                y = -Random.nextFloat() * height * 0.5f,
                vx = (Random.nextFloat() - 0.5f) * 120 * u,
                vy = (150 + Random.nextFloat() * 180) * u,
                giro = Random.nextFloat() * 360,
                velocidadGiro = (Random.nextFloat() - 0.5f) * 720,
                ancho = (5 + Random.nextFloat() * 5) * u,
                alto = (3 + Random.nextFloat() * 4) * u,
                color = colores[Random.nextInt(colores.size)]
            )
        }
        ultimoCuadro = SystemClock.uptimeMillis()
        finEn = ultimoCuadro + 3800
        postInvalidateOnAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (papelitos.isEmpty()) return
        val ahora = SystemClock.uptimeMillis()
        val dt = ((ahora - ultimoCuadro).coerceAtMost(50)) / 1000f
        ultimoCuadro = ahora
        val desvanecer = ((finEn - ahora) / 800f).coerceIn(0f, 1f)

        papelitos.forEach { p ->
            p.vy += 60 * u * dt            // gravedad suave
            p.x += p.vx * dt
            p.y += p.vy * dt
            p.giro += p.velocidadGiro * dt
            pincel.color = p.color
            pincel.alpha = (255 * desvanecer).toInt()
            canvas.save()
            canvas.rotate(p.giro, p.x, p.y)
            canvas.drawRect(p.x - p.ancho / 2, p.y - p.alto / 2, p.x + p.ancho / 2, p.y + p.alto / 2, pincel)
            canvas.restore()
        }
        if (ahora < finEn) postInvalidateOnAnimation() else papelitos.clear()
    }
}
