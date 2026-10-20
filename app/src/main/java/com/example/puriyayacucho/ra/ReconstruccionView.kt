package com.example.puriyayacucho.ra

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.puriyayacucho.data.PuntoInteresRA
import com.example.puriyayacucho.data.TipoReconstruccion
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Dibuja la reconstrucción del sitio (en líneas cian, estilo holograma) sobre
 * la imagen de la cámara. [progreso] va de 0 (Hoy) a 1 (Reconstrucción).
 *
 * Es una "RA ligera": la figura se superpone a la cámara, no se ancla al suelo.
 * Para anclarla en 3D real se puede migrar a ARCore + un modelo .glb.
 */
class ReconstruccionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var tipo: TipoReconstruccion = TipoReconstruccion.RECINTO_WARI
        set(value) { field = value; invalidate() }

    var progreso: Float = 0.7f
        set(value) { field = value.coerceIn(0f, 1f); invalidate() }

    var puntosInteres: List<PuntoInteresRA> = emptyList()
        set(value) { field = value; invalidate() }

    /** Se llama cuando el usuario toca un punto de interés. */
    var onPuntoTocado: ((PuntoInteresRA) -> Unit)? = null

    private val cian = Color.parseColor("#5CF2FF")
    private val densidad = resources.displayMetrics.density

    private val linea = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = cian
        strokeWidth = 2f * densidad
    }
    private val lineaSuave = Paint(linea).apply { strokeWidth = 1.2f * densidad }
    private val relleno = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = cian
    }
    private val nodo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private val anilloPunto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.WHITE
        strokeWidth = 3f * densidad
    }
    private val centroPunto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#FF3D5A")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val alfaLinea = (70 + 185 * progreso).toInt()
        linea.alpha = alfaLinea
        lineaSuave.alpha = (alfaLinea * 0.5f).toInt()
        relleno.alpha = (10 + 60 * progreso).toInt()

        when (tipo) {
            TipoReconstruccion.RECINTO_WARI -> dibujarRecintoWari(canvas)
            TipoReconstruccion.USHNO_INCA -> dibujarUshno(canvas)
            TipoReconstruccion.OBELISCO -> dibujarObelisco(canvas)
        }
        dibujarPuntosInteres(canvas)
    }

    /** Recinto circular de muros altos (como Cheqo Wasi / Monjachayuq). */
    private fun dibujarRecintoWari(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        val cx = w / 2
        val base = h * 0.66f
        val rx = w * 0.40f
        val ry = w * 0.14f
        val alto = h * 0.20f * (0.35f + 0.65f * progreso) // los muros "crecen" con el progreso
        val arriba = base - alto

        canvas.drawOval(RectF(cx - rx, arriba - ry, cx + rx, arriba + ry), relleno)
        canvas.drawOval(RectF(cx - rx, arriba - ry, cx + rx, arriba + ry), linea)
        canvas.drawOval(RectF(cx - rx, base - alto / 2 - ry, cx + rx, base - alto / 2 + ry), lineaSuave)
        canvas.drawOval(RectF(cx - rx, base - ry, cx + rx, base + ry), linea)

        for (grado in 0 until 360 step 15) {
            val a = Math.toRadians(grado.toDouble())
            val x = cx + rx * cos(a).toFloat()
            val yOff = ry * sin(a).toFloat()
            val pincel = if (sin(a) > 0) linea else lineaSuave // los de adelante más visibles
            canvas.drawLine(x, base + yOff, x, arriba + yOff, pincel)
        }
        canvas.drawCircle(cx - rx, base, 4 * densidad, nodo)
        canvas.drawCircle(cx + rx, base, 4 * densidad, nodo)
        canvas.drawCircle(cx, base + ry, 4 * densidad, nodo)
    }

    /** Ushno: pirámide ceremonial inca de cinco plataformas. */
    private fun dibujarUshno(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        val cx = w / 2
        val base = h * 0.72f
        val niveles = 5
        val altoNivel = h * 0.055f * (0.4f + 0.6f * progreso)
        val profundidad = w * 0.06f
        var ancho = w * 0.80f
        var y = base
        for (i in 0 until niveles) {
            val frente = RectF(cx - ancho / 2, y - altoNivel, cx + ancho / 2, y)
            canvas.drawRect(frente, relleno)
            canvas.drawRect(frente, linea)
            // cara superior (en perspectiva)
            val tapa = Path().apply {
                moveTo(frente.left, frente.top)
                lineTo(frente.left + profundidad, frente.top - profundidad * 0.5f)
                lineTo(frente.right - profundidad, frente.top - profundidad * 0.5f)
                lineTo(frente.right, frente.top)
                close()
            }
            canvas.drawPath(tapa, lineaSuave)
            y -= altoNivel
            ancho *= 0.80f
        }
        // Escalinata central
        canvas.drawLine(cx - w * 0.05f, base, cx - w * 0.03f, y, lineaSuave)
        canvas.drawLine(cx + w * 0.05f, base, cx + w * 0.03f, y, lineaSuave)
        // Trono (asiento doble) en la cima
        canvas.drawRect(RectF(cx - w * 0.06f, y - altoNivel * 0.8f, cx + w * 0.06f, y), linea)
    }

    /** Obelisco conmemorativo sobre su plataforma. */
    private fun dibujarObelisco(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        val cx = w / 2
        val base = h * 0.74f
        val alto = h * 0.52f * (0.3f + 0.7f * progreso)
        val anchoBase = w * 0.13f
        val anchoArriba = w * 0.08f
        val arriba = base - alto

        // Plataforma escalonada
        canvas.drawRect(RectF(cx - w * 0.36f, base, cx + w * 0.36f, base + h * 0.03f), linea)
        canvas.drawRect(RectF(cx - w * 0.26f, base - h * 0.03f, cx + w * 0.26f, base), linea)

        val cuerpo = Path().apply {
            moveTo(cx - anchoBase / 2, base - h * 0.03f)
            lineTo(cx - anchoArriba / 2, arriba)
            lineTo(cx, arriba - w * 0.08f) // punta (piramidión)
            lineTo(cx + anchoArriba / 2, arriba)
            lineTo(cx + anchoBase / 2, base - h * 0.03f)
            close()
        }
        canvas.drawPath(cuerpo, relleno)
        canvas.drawPath(cuerpo, linea)
        canvas.drawLine(cx, arriba - w * 0.08f, cx, base - h * 0.03f, lineaSuave)
        for (i in 1..6) {
            val t = i / 7f
            val yy = base - h * 0.03f - (base - h * 0.03f - arriba) * t
            val semi = (anchoBase + (anchoArriba - anchoBase) * t) / 2
            canvas.drawLine(cx - semi, yy, cx + semi, yy, lineaSuave)
        }
    }

    private fun dibujarPuntosInteres(canvas: Canvas) {
        puntosInteres.forEach { p ->
            val x = p.x * width
            val y = p.y * height
            canvas.drawCircle(x, y, 14 * densidad, anilloPunto)
            canvas.drawCircle(x, y, 7 * densidad, centroPunto)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val tocado = puntosInteres.firstOrNull { p ->
                hypot(event.x - p.x * width, event.y - p.y * height) < 36 * densidad
            }
            if (tocado != null) {
                onPuntoTocado?.invoke(tocado)
                return true
            }
        }
        return event.action == MotionEvent.ACTION_DOWN && puntosInteres.isNotEmpty()
    }
}
