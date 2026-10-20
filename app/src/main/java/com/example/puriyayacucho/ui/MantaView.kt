package com.example.puriyayacucho.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.puriyayacucho.R

/**
 * Franja de manta ayacuchana para detalles sutiles: separadores, bordes de tarjetas, la
 * "manija" del login... Úsala delgada (4–10 dp) y como acento, nunca detrás de textos.
 *
 * ```xml
 * <com.example.puriyayacucho.ui.MantaView
 *     android:layout_width="72dp"
 *     android:layout_height="6dp"
 *     app:mantaEstilo="completa"      // o "blanca" sobre fondos rosados
 *     app:mantaVertical="false"
 *     app:mantaRadio="3dp" />         // esquinas redondeadas (opcional)
 * ```
 */
class MantaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var estilo: MantaPintor.Estilo = MantaPintor.Estilo.COMPLETA
        set(value) { field = value; cache = null; invalidate() }

    var vertical: Boolean = false
        set(value) { field = value; cache = null; invalidate() }

    /** Radio de las esquinas (px). 0 = esquinas rectas. */
    var radio: Float = 0f
        set(value) { field = value; cache = null; invalidate() }

    private var cache: Bitmap? = null
    private val area = RectF()
    private val recorte = Path()

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.MantaView)
            estilo = if (a.getInt(R.styleable.MantaView_mantaEstilo, 0) == 1) MantaPintor.Estilo.BLANCA
            else MantaPintor.Estilo.COMPLETA
            vertical = a.getBoolean(R.styleable.MantaView_mantaVertical, false)
            radio = a.getDimension(R.styleable.MantaView_mantaRadio, 0f)
            a.recycle()
        }
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cache = null
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (width == 0 || height == 0) return
        // El tejido es fijo: se dibuja una vez en un bitmap y luego solo se copia
        val bmp = cache ?: Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also { nuevo ->
            val c = Canvas(nuevo)
            area.set(0f, 0f, width.toFloat(), height.toFloat())
            if (radio > 0f) {
                recorte.reset()
                recorte.addRoundRect(area, radio, radio, Path.Direction.CW)
                c.clipPath(recorte)
            }
            MantaPintor.dibujar(c, area, estilo, vertical)
            cache = nuevo
        }
        canvas.drawBitmap(bmp, 0f, 0f, null)
    }
}
