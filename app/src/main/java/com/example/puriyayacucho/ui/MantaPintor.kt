package com.example.puriyayacucho.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF

/**
 * Dibuja una franja inspirada en la manta ayacuchana: líneas de colores, zigzags y rombos
 * tejidos, como las fajas y mantas de Huamanga. Se usa en [MantaView] y en el retablo.
 *
 * - [Estilo.COMPLETA]: con todos sus colores (morado, amarillo, carmesí, celeste...). Para
 *   franjas delgadas sobre fondos claros.
 * - [Estilo.BLANCA]: el mismo tejido solo en blanco translúcido, para usarlo encima de los
 *   fondos rosados sin recargar.
 */
object MantaPintor {

    enum class Estilo { COMPLETA, BLANCA }

    private const val NAVY = "#2A1B6E"
    private const val AMARILLO = "#F7C600"
    private const val CIAN = "#12B5EA"
    private const val CARMESI = "#F5174F"
    private const val MORADO = "#5B1FD6"
    private const val ROSA = "#FF8FB1"

    private sealed class Fila(val fraccion: Float) {
        class Liso(f: Float, val color: String) : Fila(f)
        class Zigzag(f: Float, val linea: String, val hilo: String) : Fila(f)
        class Rombos(f: Float, val fondo: String, val rombo: String) : Fila(f)
    }

    /** Orden de las filas de arriba hacia abajo (suman 1). Es simétrica, como una faja. */
    private val filas = listOf(
        Fila.Liso(0.09f, NAVY),
        Fila.Liso(0.06f, AMARILLO),
        Fila.Zigzag(0.22f, CARMESI, AMARILLO),
        Fila.Liso(0.05f, CIAN),
        Fila.Rombos(0.16f, CARMESI, "#FFFFFF"),
        Fila.Liso(0.05f, CIAN),
        Fila.Zigzag(0.22f, MORADO, ROSA),
        Fila.Liso(0.06f, AMARILLO),
        Fila.Liso(0.09f, NAVY)
    )

    private val relleno = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val linea = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
    private val path = Path()
    private val colores = HashMap<String, Int>()

    private fun color(hex: String): Int = colores.getOrPut(hex) { Color.parseColor(hex) }

    /** Dibuja la franja dentro de [area]. Si [vertical], el tejido corre de arriba hacia abajo. */
    fun dibujar(canvas: Canvas, area: RectF, estilo: Estilo, vertical: Boolean = false) {
        if (area.width() <= 0f || area.height() <= 0f) return
        if (vertical) {
            // Se dibuja horizontal y se gira: el largo pasa a ser el alto
            canvas.save()
            canvas.translate(area.left, area.bottom)
            canvas.rotate(-90f)
            dibujarHorizontal(canvas, 0f, 0f, area.height(), area.width(), estilo)
            canvas.restore()
        } else {
            dibujarHorizontal(canvas, area.left, area.top, area.width(), area.height(), estilo)
        }
    }

    private fun dibujarHorizontal(canvas: Canvas, x0: Float, y0: Float, ancho: Float, alto: Float, estilo: Estilo) {
        val blanca = estilo == Estilo.BLANCA
        var y = y0
        canvas.save()
        canvas.clipRect(x0, y0, x0 + ancho, y0 + alto)
        filas.forEach { fila ->
            val h = alto * fila.fraccion
            when (fila) {
                is Fila.Liso -> {
                    relleno.color = if (blanca) Color.argb(110, 255, 255, 255) else color(fila.color)
                    canvas.drawRect(x0, y, x0 + ancho, y + h, relleno)
                }
                is Fila.Zigzag -> {
                    if (!blanca) {
                        relleno.color = Color.WHITE
                        canvas.drawRect(x0, y, x0 + ancho, y + h, relleno)
                    }
                    val periodo = h * 1.6f
                    path.reset()
                    var x = x0 - periodo
                    var arriba = true
                    path.moveTo(x, y + h * 0.22f)
                    while (x < x0 + ancho + periodo) {
                        x += periodo / 2
                        arriba = !arriba
                        path.lineTo(x, if (arriba) y + h * 0.22f else y + h * 0.78f)
                    }
                    linea.color = if (blanca) Color.argb(230, 255, 255, 255) else color(fila.linea)
                    linea.strokeWidth = h * 0.34f
                    canvas.drawPath(path, linea)
                    if (!blanca) {
                        linea.color = color(fila.hilo)
                        linea.strokeWidth = h * 0.11f
                        canvas.drawPath(path, linea)
                    }
                }
                is Fila.Rombos -> {
                    relleno.color = if (blanca) Color.argb(60, 255, 255, 255) else color(fila.fondo)
                    canvas.drawRect(x0, y, x0 + ancho, y + h, relleno)
                    val periodo = h * 1.5f
                    val r = h * 0.40f
                    val cy = y + h / 2
                    linea.color = if (blanca) Color.WHITE else color(fila.rombo)
                    linea.strokeWidth = r * 0.42f
                    relleno.color = linea.color
                    var cx = x0 + periodo / 2
                    while (cx < x0 + ancho + periodo) {
                        val rr = r * 0.78f
                        path.reset()
                        path.moveTo(cx, cy - rr)
                        path.lineTo(cx + rr, cy)
                        path.lineTo(cx, cy + rr)
                        path.lineTo(cx - rr, cy)
                        path.close()
                        canvas.drawPath(path, linea)
                        val rc = r * 0.2f
                        path.reset()
                        path.moveTo(cx, cy - rc)
                        path.lineTo(cx + rc, cy)
                        path.lineTo(cx, cy + rc)
                        path.lineTo(cx - rc, cy)
                        path.close()
                        canvas.drawPath(path, relleno)
                        cx += periodo
                    }
                }
            }
            y += h
        }
        canvas.restore()
    }
}
