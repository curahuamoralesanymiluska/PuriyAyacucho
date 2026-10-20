package com.example.puriyayacucho.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import com.example.puriyayacucho.R
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Cabecera del Calendario con forma de **retablo ayacuchano** (cajón de San Marcos):
 * frontón con flores, cajón con los textos centrados, dos puertas que se abren al aparecer
 * (con flores y una franja de manta en el borde) y una base con tejido de manta.
 *
 * Se dibuja en un espacio de 340 × 240 unidades que se escala al tamaño de la vista, así se
 * ve igual en cualquier celular. Los textos salen de los recursos (se traducen).
 */
class RetabloView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var lineaSuperior: String = context.getString(R.string.retablo_linea_superior)
        set(value) { field = value; invalidate() }
    var titulo: String = context.getString(R.string.retablo_titulo)
        set(value) { field = value; actualizarDescripcion(); invalidate() }
    var anio: String = context.getString(R.string.retablo_anio)
        set(value) { field = value; actualizarDescripcion(); invalidate() }

    /** De 0 (puertas cerradas) a 1 (abiertas). */
    var apertura: Float = 1f
        set(value) { field = value; invalidate() }

    private var animacion: ValueAnimator? = null
    private var yaSeAbrio = false

    private val rosa = Color.parseColor("#FF3355")
    private val rosaClaro = Color.parseColor("#FF5C77")
    private val vino = Color.parseColor("#9E1236")
    private val vinoOscuro = Color.parseColor("#6E0B25")
    private val vinoHoja = Color.parseColor("#C9244A")
    private val oro = Color.parseColor("#FFC93C")
    private val oroTexto = Color.parseColor("#FFE08A")
    private val petaloRosa = Color.parseColor("#FFD6DD")
    private val petaloOro = Color.parseColor("#FFE08A")

    private val relleno = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val trazo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
    private val texto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = Color.WHITE
    }
    private val path = Path()
    private val rect = RectF()
    private var degradado: LinearGradient? = null

    init {
        actualizarDescripcion()
    }

    private fun actualizarDescripcion() {
        contentDescription = "$titulo $anio"
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // Las puertas se abren la primera vez que aparece la cabecera
        if (!yaSeAbrio) {
            yaSeAbrio = true
            apertura = 0f
            animacion = ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 900
                startDelay = 250
                interpolator = OvershootInterpolator(1.1f)
                addUpdateListener { apertura = it.animatedValue as Float }
                start()
            }
        }
    }

    override fun onDetachedFromWindow() {
        animacion?.cancel()
        apertura = 1f
        super.onDetachedFromWindow()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val ancho = MeasureSpec.getSize(widthMeasureSpec)
        // Mantiene la proporción 340 × 240
        val alto = resolveSize((ancho * ALTO / ANCHO).toInt(), heightMeasureSpec)
        setMeasuredDimension(ancho, alto)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val escala = min(width / ANCHO, height / ALTO)
        if (escala <= 0f) return
        canvas.save()
        canvas.translate((width - ANCHO * escala) / 2f, (height - ALTO * escala) / 2f)
        canvas.scale(escala, escala)

        // Sombra
        relleno.color = Color.argb(28, 0, 0, 0)
        rect.set(64f, 228f, 276f, 238f)
        canvas.drawOval(rect, relleno)

        dibujarPuerta(canvas, -1)
        dibujarPuerta(canvas, 1)
        dibujarBase(canvas)
        dibujarCajon(canvas)
        dibujarFronton(canvas)
        dibujarTextos(canvas)
        canvas.restore()
    }

    private fun dibujarPuerta(canvas: Canvas, lado: Int) {
        val ancho = 66f * apertura
        if (ancho < 1f) return
        val bisagra = if (lado < 0) 80f else 260f
        val exterior = bisagra + lado * ancho
        val persp = 7f * apertura
        val arribaB = 92f
        val abajoB = 202f
        val arribaE = arribaB + persp
        val abajoE = abajoB - persp

        path.reset()
        path.moveTo(bisagra, arribaB)
        path.lineTo(exterior, arribaE)
        path.lineTo(exterior, abajoE)
        path.lineTo(bisagra, abajoB)
        path.close()
        relleno.color = rosaClaro
        canvas.drawPath(path, relleno)
        trazo.color = vino
        trazo.strokeWidth = 2f
        canvas.drawPath(path, trazo)

        if (ancho < 24f) return
        // Franja de manta en el borde exterior de la puerta
        val bordeExt = if (lado < 0) exterior + 2f else exterior - 11f
        rect.set(bordeExt, arribaE + 3f, bordeExt + 9f, abajoE - 3f)
        MantaPintor.dibujar(canvas, rect, MantaPintor.Estilo.COMPLETA, vertical = true)

        // Marco interior blanco
        val iBisagra = bisagra + lado * 6f
        val iExterior = exterior - lado * 15f
        path.reset()
        path.moveTo(iBisagra, arribaB + 7f)
        path.lineTo(iExterior, arribaE + 7f)
        path.lineTo(iExterior, abajoE - 7f)
        path.lineTo(iBisagra, abajoB - 7f)
        path.close()
        trazo.color = Color.WHITE
        trazo.strokeWidth = 1.2f
        canvas.drawPath(path, trazo)

        // Enredadera con tres flores
        val mx = (iBisagra + iExterior) / 2f
        trazo.color = vino
        trazo.strokeWidth = 1.6f
        path.reset()
        path.moveTo(mx, arribaB + 16f)
        path.quadTo(mx + lado * 5f, (arribaB + abajoB) / 2f - 20f, mx, (arribaB + abajoB) / 2f)
        path.quadTo(mx - lado * 5f, (arribaB + abajoB) / 2f + 20f, mx, abajoB - 14f)
        canvas.drawPath(path, trazo)
        val medio = (arribaB + abajoB) / 2f
        listOf(arribaB + 26f to false, medio to true, abajoB - 26f to false).forEach { (y, grande) ->
            hoja(canvas, mx, y + 8f, if (lado < 0) 200f else -20f, 10f, vino)
            hoja(canvas, mx, y + 8f, if (lado < 0) -20f else 200f, 10f, vinoHoja)
            flor(canvas, mx, y, if (grande) 11f else 9f, 8, if (grande) petaloOro else petaloRosa)
        }
    }

    private fun dibujarBase(canvas: Canvas) {
        relleno.color = vino
        rect.set(72f, 204f, 268f, 218f)
        canvas.drawRoundRect(rect, 2f, 2f, relleno)
        rect.set(75f, 207f, 265f, 215f)
        MantaPintor.dibujar(canvas, rect, MantaPintor.Estilo.COMPLETA)
        relleno.color = vinoOscuro
        rect.set(66f, 218f, 274f, 226f)
        canvas.drawRoundRect(rect, 3f, 3f, relleno)
    }

    private fun dibujarCajon(canvas: Canvas) {
        relleno.color = vino
        canvas.drawRect(82f, 88f, 258f, 206f, relleno)
        val shader = degradado ?: LinearGradient(
            0f, 93f, 0f, 201f, Color.parseColor("#FF3D5E"), Color.parseColor("#E8204A"), Shader.TileMode.CLAMP
        ).also { degradado = it }
        relleno.shader = shader
        canvas.drawRect(87f, 93f, 253f, 201f, relleno)
        relleno.shader = null
        trazo.color = Color.WHITE
        trazo.strokeWidth = 1.2f
        rect.set(95f, 101f, 245f, 193f)
        canvas.drawRect(rect, trazo)
        relleno.color = oro
        for ((x, y) in listOf(95f to 101f, 245f to 101f, 95f to 193f, 245f to 193f)) {
            canvas.drawCircle(x, y, 2.4f, relleno)
        }
    }

    private fun dibujarFronton(canvas: Canvas) {
        path.reset()
        path.moveTo(170f, 10f)
        path.lineTo(272f, 89f)
        path.lineTo(68f, 89f)
        path.close()
        relleno.color = rosa
        canvas.drawPath(path, relleno)
        trazo.color = vino
        trazo.strokeWidth = 3f
        canvas.drawPath(path, trazo)
        path.reset()
        path.moveTo(170f, 24f)
        path.lineTo(252f, 83f)
        path.lineTo(88f, 83f)
        path.close()
        trazo.color = Color.WHITE
        trazo.strokeWidth = 1.2f
        canvas.drawPath(path, trazo)
        // Cornisa
        relleno.color = vino
        canvas.drawRect(70f, 85f, 270f, 92f, relleno)
        // Flores
        hoja(canvas, 170f, 62f, 200f, 26f, vino)
        hoja(canvas, 170f, 62f, -20f, 26f, vino)
        hoja(canvas, 170f, 62f, 150f, 22f, vinoHoja)
        hoja(canvas, 170f, 62f, 30f, 22f, vinoHoja)
        flor(canvas, 170f, 56f, 20f, 10, petaloRosa)
        flor(canvas, 126f, 72f, 9f, 8, petaloOro)
        flor(canvas, 214f, 72f, 9f, 8, petaloOro)
        relleno.color = oro
        canvas.drawCircle(170f, 10f, 3.2f, relleno)
        canvas.drawCircle(70f, 88f, 3.2f, relleno)
        canvas.drawCircle(270f, 88f, 3.2f, relleno)
    }

    private fun dibujarTextos(canvas: Canvas) {
        val anchoMax = 136f
        // "CALENDARIO", espaciado
        texto.typeface = Typeface.DEFAULT
        texto.letterSpacing = 0.35f
        texto.textSize = 10f
        texto.color = Color.argb(235, 255, 255, 255)
        canvas.drawText(lineaSuperior.uppercase(), 170f, 124f, texto)
        // "PURIY AYACUCHO", se achica si no entra
        texto.typeface = Typeface.DEFAULT_BOLD
        texto.letterSpacing = 0.04f
        texto.textSize = 17f
        texto.color = Color.WHITE
        val tituloMayus = titulo.uppercase()
        val medido = texto.measureText(tituloMayus)
        if (medido > anchoMax) texto.textSize = 17f * anchoMax / medido
        canvas.drawText(tituloMayus, 170f, 149f, texto)
        // Año en dorado
        texto.letterSpacing = 0.08f
        texto.textSize = 28f
        texto.color = oroTexto
        canvas.drawText(anio, 170f, 183f, texto)
    }

    private fun flor(canvas: Canvas, cx: Float, cy: Float, r: Float, petalos: Int, colorAlterno: Int) {
        for (i in 0 until petalos) {
            val a = 2 * Math.PI * i / petalos
            val px = cx + cos(a).toFloat() * r * 0.55f
            val py = cy + sin(a).toFloat() * r * 0.55f
            relleno.color = if (i % 2 == 0) Color.WHITE else colorAlterno
            canvas.drawCircle(px, py, r * 0.45f, relleno)
        }
        relleno.color = oro
        canvas.drawCircle(cx, cy, r * 0.32f, relleno)
        relleno.color = vino
        canvas.drawCircle(cx, cy, r * 0.13f, relleno)
    }

    private fun hoja(canvas: Canvas, x: Float, y: Float, anguloGrados: Float, largo: Float, color: Int) {
        val a = Math.toRadians(anguloGrados.toDouble())
        val dx = cos(a).toFloat() * largo
        val dy = sin(a).toFloat() * largo
        val nx = -dy * 0.32f
        val ny = dx * 0.32f
        path.reset()
        path.moveTo(x, y)
        path.lineTo(x + dx * 0.5f + nx, y + dy * 0.5f + ny)
        path.lineTo(x + dx, y + dy)
        path.lineTo(x + dx * 0.5f - nx, y + dy * 0.5f - ny)
        path.close()
        relleno.color = color
        canvas.drawPath(path, relleno)
    }

    private companion object {
        const val ANCHO = 340f
        const val ALTO = 240f
    }
}
