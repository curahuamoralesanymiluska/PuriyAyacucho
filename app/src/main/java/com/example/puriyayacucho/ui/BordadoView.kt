package com.example.puriyayacucho.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.example.puriyayacucho.R

/**
 * Detalle de bordado ayacuchano (flores de lana) para usar con delicadeza: la esquina de una
 * tarjeta o encabezado, una guirnalda fina o una flor sola. Nunca detrás de textos.
 *
 * ```xml
 * <com.example.puriyayacucho.ui.BordadoView
 *     android:layout_width="64dp"
 *     android:layout_height="64dp"
 *     android:layout_gravity="top|end"
 *     app:bordadoForma="esquina"   // esquina, guirnalda o flor
 *     app:bordadoTono="color"      // "blanco" sobre fondos rosados
 *     app:bordadoEspejo="false" /> // true = esquina de la izquierda
 * ```
 */
class BordadoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var forma: BordadoPintor.Forma = BordadoPintor.Forma.ESQUINA
        set(value) { field = value; cache = null; invalidate() }

    var blanca: Boolean = false
        set(value) { field = value; cache = null; invalidate() }

    var espejo: Boolean = false
        set(value) { field = value; cache = null; invalidate() }

    private var cache: Bitmap? = null

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.BordadoView)
            forma = when (a.getInt(R.styleable.BordadoView_bordadoForma, 0)) {
                1 -> BordadoPintor.Forma.GUIRNALDA
                2 -> BordadoPintor.Forma.FLOR
                else -> BordadoPintor.Forma.ESQUINA
            }
            blanca = a.getInt(R.styleable.BordadoView_bordadoTono, 0) == 1
            espejo = a.getBoolean(R.styleable.BordadoView_bordadoEspejo, false)
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
        // El bordado es fijo: se dibuja una vez en un bitmap y luego solo se copia
        val bmp = cache ?: Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also { nuevo ->
            BordadoPintor.dibujar(Canvas(nuevo), width.toFloat(), height.toFloat(), forma, blanca, espejo)
            cache = nuevo
        }
        canvas.drawBitmap(bmp, 0f, 0f, null)
    }
}
