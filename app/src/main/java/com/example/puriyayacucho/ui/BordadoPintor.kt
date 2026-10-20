package com.example.puriyayacucho.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Flores bordadas al estilo ayacuchano: pétalos con contorno oscuro, relleno de lana y
 * "corazón" claro, filas de puntadas, hojas con nervadura, tallos como cordón torcido y
 * bolitas de colores. Es un dibujo propio inspirado en el bordado tradicional (no copia
 * ninguna pieza). Se usa en [BordadoView] y en la flor del poncho de la guía de bienvenida.
 *
 * - [Forma.ESQUINA]: ramita para la esquina de una tarjeta o encabezado (arriba a la derecha;
 *   con `espejo`, a la izquierda).
 * - [Forma.GUIRNALDA]: franja horizontal de flores y hojas.
 * - [Forma.FLOR]: una flor sola con dos hojas, para estados vacíos o ilustraciones pequeñas.
 *
 * Con `blanca = true` todo se dibuja en blanco translúcido, para ponerlo sobre el rosa.
 */
object BordadoPintor {

    enum class Forma { ESQUINA, GUIRNALDA, FLOR }

    /** Un hilo de lana en tres tonos: oscuro (contorno), principal y claro. */
    class Hilo(oscuro: String, principal: String, claro: String) {
        internal val tonos = intArrayOf(Color.parseColor(oscuro), Color.parseColor(principal), Color.parseColor(claro))
    }

    val MOSTAZA = Hilo("#9A5B0B", "#E9A22A", "#F8D77A")
    val VIOLETA = Hilo("#2F2F6E", "#5F62B0", "#B9BCEB")
    private val VIOLETA_FONDO = Hilo("#26265A", "#4B4E99", "#9EA2DB")
    val ROSA = Hilo("#A3124F", "#E64C8D", "#F9BCD5")
    val CORAL = Hilo("#B23A22", "#F27449", "#FCC7AB")
    val TURQUESA = Hilo("#0C5F6B", "#1C9FAE", "#A4DEE4")
    val OLIVA = Hilo("#3F4A1A", "#7A8A35", "#BCC57E")
    val GUINDA = Hilo("#5E0F25", "#9E2340", "#E07A8F")
    val MORADO = Hilo("#45215A", "#7D3F94", "#C9A3D8")

    private const val OSC = 0
    private const val PRI = 1
    private const val CLA = 2
    /** Opacidad de cada tono en la versión blanca. */
    private val ALFA_BLANCA = floatArrayOf(0.9f, 0.16f, 0.42f)

    private val relleno = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val trazo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    /** Dibuja la [forma] dentro de un área de [ancho] x [alto] (en píxeles), desde (0, 0). */
    fun dibujar(canvas: Canvas, ancho: Float, alto: Float, forma: Forma, blanca: Boolean = false, espejo: Boolean = false) {
        if (ancho <= 0f || alto <= 0f) return
        when (forma) {
            Forma.ESQUINA -> esquina(canvas, ancho, alto, espejo, blanca)
            Forma.GUIRNALDA -> guirnalda(canvas, ancho, alto, blanca)
            Forma.FLOR -> flor(canvas, ancho, alto, blanca)
        }
    }

    // ---------- Composiciones ----------

    /** Ramita en una caja de 100 x 100 unidades con la esquina arriba a la derecha. */
    private fun esquina(c: Canvas, w: Float, h: Float, espejo: Boolean, blanca: Boolean) {
        val k = min(w, h) / 100f
        val u = k
        fun s(v: Float) = v * k
        c.save()
        if (espejo) {
            c.translate(w, 0f)
            c.scale(-1f, 1f)
        }
        c.translate(w - 100f * k, 0f)
        tallo(c, u, floatArrayOf(s(84f), s(18f), s(56f), s(30f), s(34f), s(30f), s(20f), s(30f), s(12f), s(40f)), TURQUESA, 2.6f, blanca)
        tallo(c, u, floatArrayOf(s(86f), s(26f), s(92f), s(48f), s(84f), s(70f)), TURQUESA, 2.2f, blanca)
        hoja(c, u, s(54f), s(30f), s(24f), s(9f), Math.PI.toFloat() * 0.86f, OLIVA, blanca)
        hoja(c, u, s(60f), s(28f), s(20f), s(8f), -Math.PI.toFloat() * 0.82f, OLIVA, blanca)
        hoja(c, u, s(90f), s(52f), s(18f), s(7f), Math.PI.toFloat() * 0.62f, OLIVA, blanca)
        bolita(c, u, s(14f), s(42f), s(3.4f), MORADO, blanca)
        bolita(c, u, s(9f), s(35f), s(2.8f), TURQUESA, blanca)
        bolita(c, u, s(20f), s(46f), s(2.6f), CORAL, blanca)
        bolita(c, u, s(78f), s(72f), s(3f), MORADO, blanca)
        bolita(c, u, s(86f), s(76f), s(2.6f), TURQUESA, blanca)
        florOcho(c, u, s(40f), s(17f), s(13f), MOSTAZA, CORAL, 0.3f, 8, blanca)
        florOcho(c, u, s(88f), s(60f), s(10f), ROSA, GUINDA, 0.1f, 6, blanca)
        margarita(c, u, s(84f), s(16f), s(25f), VIOLETA, VIOLETA_FONDO, GUINDA, 0.15f, blanca)
        c.restore()
    }

    /** Tallo ondulado con flores que se turnan, hojas y bolitas. La altura manda el tamaño. */
    private fun guirnalda(c: Canvas, w: Float, h: Float, blanca: Boolean) {
        val u = h / 30f
        val paso = 46f * u
        val cy = h / 2f
        val n = max(1, floor((w - 10f * u) / paso).toInt())
        val x0 = (w - n * paso) / 2f
        val puntos = ArrayList<Float>()
        puntos += x0 - 4f * u
        puntos += cy
        for (i in 0 until n * 2) {
            puntos += x0 + (i + 0.5f) * paso / 2f
            puntos += cy + (if (i % 2 == 1) 4f else -4f) * u
            puntos += x0 + (i + 1) * paso / 2f
            puntos += cy
        }
        tallo(c, u, puntos.toFloatArray(), TURQUESA, 2f, blanca)
        for (i in 0 until n) {
            val x = x0 + paso * (i + 0.5f)
            val xh = x - paso * 0.5f + 6f * u
            hoja(c, u, xh, cy, 13f * u, 5f * u, -0.5f, OLIVA, blanca)
            hoja(c, u, xh, cy, 13f * u, 5f * u, 0.5f, OLIVA, blanca)
            bolita(c, u, x - paso * 0.5f, cy - 7f * u, 2.2f * u, MORADO, blanca)
            bolita(c, u, x - paso * 0.5f, cy + 7f * u, 2.2f * u, CORAL, blanca)
            when (i % 5) {
                0 -> florOcho(c, u, x, cy, 12f * u, MOSTAZA, CORAL, i * 0.4f, 8, blanca)
                1 -> margarita(c, u, x, cy, 13f * u, VIOLETA, VIOLETA, GUINDA, i * 0.3f, blanca)
                2 -> florOcho(c, u, x, cy, 12f * u, ROSA, GUINDA, i * 0.4f, 8, blanca)
                3 -> margarita(c, u, x, cy, 13f * u, CORAL, CORAL, TURQUESA, i * 0.3f, blanca)
                else -> florOcho(c, u, x, cy, 12f * u, TURQUESA, MOSTAZA, i * 0.4f, 8, blanca)
            }
        }
    }

    /** Una flor de ocho pétalos con su tallo y dos hojas, centrada en una caja de 100 x 100. */
    private fun flor(c: Canvas, w: Float, h: Float, blanca: Boolean) {
        val k = min(w, h) / 100f
        val u = k
        fun s(v: Float) = v * k
        c.save()
        c.translate((w - 100f * k) / 2f, (h - 100f * k) / 2f)
        tallo(c, u, floatArrayOf(s(50f), s(60f), s(50f), s(78f), s(50f), s(92f)), TURQUESA, 2.6f, blanca)
        hoja(c, u, s(50f), s(80f), s(26f), s(10f), Math.PI.toFloat() * 0.9f, OLIVA, blanca)
        hoja(c, u, s(50f), s(80f), s(26f), s(10f), Math.PI.toFloat() * 0.1f, OLIVA, blanca)
        bolita(c, u, s(16f), s(66f), s(3.4f), MORADO, blanca)
        bolita(c, u, s(84f), s(66f), s(3.4f), TURQUESA, blanca)
        florOcho(c, u, s(50f), s(40f), s(34f), MOSTAZA, CORAL, (Math.PI / 8).toFloat(), 8, blanca)
        c.restore()
    }

    // ---------- Piezas ----------

    /**
     * Flor de pétalos redondos (8 por defecto) centrada en ([x], [y]) con radio [r].
     * [u] es el grosor base de las puntadas (en las mismas unidades que el canvas).
     */
    fun florOcho(
        c: Canvas, u: Float, x: Float, y: Float, r: Float, hilo: Hilo, centro: Hilo,
        giro: Float = 0f, n: Int = 8, blanca: Boolean = false
    ) {
        c.save()
        c.translate(x, y)
        c.rotate(Math.toDegrees(giro.toDouble()).toFloat())
        val ancho = r * (if (n > 6) 0.36f else 0.44f)
        // Primero todos los rellenos y luego los contornos: así cada pétalo queda bien delineado
        for (soloContorno in booleanArrayOf(false, true)) {
            for (i in 0 until n) {
                c.save()
                c.rotate(360f * i / n)
                c.translate(r * 0.12f, 0f)
                petalo(c, u, r * 0.88f, ancho, hilo, blanca, soloContorno)
                c.restore()
            }
        }
        centroFlor(c, u, r * 0.2f, centro, blanca)
        c.restore()
    }

    /** Margarita de dos coronas de pétalos finos. */
    private fun margarita(
        c: Canvas, u: Float, x: Float, y: Float, r: Float, hilo: Hilo, fondo: Hilo, centro: Hilo,
        giro: Float, blanca: Boolean
    ) {
        val n = 12
        c.save()
        c.translate(x, y)
        c.rotate(Math.toDegrees(giro.toDouble()).toFloat())
        for (soloContorno in booleanArrayOf(false, true)) {
            for (i in 0 until n) {
                c.save()
                c.rotate(360f * i / n)
                c.translate(r * 0.1f, 0f)
                petalo(c, u, r * 0.9f, r * 0.24f, fondo, blanca, soloContorno)
                c.restore()
            }
        }
        // En blanco, la segunda corona recargaría la flor: solo va en color
        if (!blanca) {
            for (soloContorno in booleanArrayOf(false, true)) {
                for (i in 0 until n) {
                    c.save()
                    c.rotate(360f * (i + 0.5f) / n)
                    c.translate(r * 0.08f, 0f)
                    petalo(c, u, r * 0.66f, r * 0.19f, hilo, false, soloContorno)
                    c.restore()
                }
            }
        }
        centroFlor(c, u, r * 0.2f, centro, blanca)
        c.restore()
    }

    /** Pétalo bordado apuntando hacia +x: relleno, corazón claro, filas de puntadas y contorno. */
    private fun petalo(c: Canvas, u: Float, largo: Float, ancho: Float, hilo: Hilo, blanca: Boolean, soloContorno: Boolean) {
        val p = petaloPath(largo, ancho)
        if (soloContorno) {
            trazo.pathEffect = null
            trazo.strokeWidth = max(1.1f * u, ancho * 0.17f) * (if (blanca) 0.7f else 1f)
            trazo.color = tono(hilo, OSC, blanca)
            c.drawPath(p, trazo)
            return
        }
        relleno.color = tono(hilo, PRI, blanca)
        c.drawPath(p, relleno)

        // Corazón claro (el mismo pétalo, más pequeño)
        c.save()
        c.translate(largo * 0.22f, 0f)
        c.scale(0.62f, 0.5f)
        relleno.color = tono(hilo, CLA, blanca)
        c.drawPath(p, relleno)
        c.restore()

        // Fila de puntadas (cadeneta) por dentro del contorno
        c.save()
        c.translate(largo * 0.1f, 0f)
        c.scale(0.82f, 0.72f)
        trazo.pathEffect = DashPathEffect(floatArrayOf(1.3f * u / 0.82f, 1.1f * u / 0.82f), 0f)
        trazo.strokeWidth = 0.9f * u / 0.77f
        trazo.color = if (blanca) tono(hilo, OSC, true, 0.6f) else conAlfa(hilo.tonos[OSC], 0.45f)
        c.drawPath(p, trazo)
        c.restore()

        // Puntadas claras sobre el corazón
        if (!blanca) {
            c.save()
            c.translate(largo * 0.22f, 0f)
            c.scale(0.62f, 0.5f)
            trazo.pathEffect = DashPathEffect(floatArrayOf(1.1f * u / 0.6f, 1.0f * u / 0.6f), 0f)
            trazo.strokeWidth = 0.8f * u / 0.55f
            trazo.color = Color.argb(140, 255, 255, 255)
            c.drawPath(p, trazo)
            c.restore()
        }
        trazo.pathEffect = null
    }

    private fun centroFlor(c: Canvas, u: Float, r: Float, hilo: Hilo, blanca: Boolean) {
        relleno.color = tono(hilo, PRI, blanca)
        c.drawCircle(0f, 0f, r, relleno)
        trazo.pathEffect = null
        trazo.strokeWidth = max(0.9f * u, r * 0.28f)
        trazo.color = tono(hilo, OSC, blanca)
        c.drawCircle(0f, 0f, r, trazo)
        trazo.pathEffect = DashPathEffect(floatArrayOf(0.9f * u, 0.9f * u), 0f)
        trazo.strokeWidth = 0.8f * u
        trazo.color = tono(hilo, CLA, blanca)
        c.drawCircle(0f, 0f, r * 0.45f, trazo)
        trazo.pathEffect = null
    }

    private fun hoja(c: Canvas, u: Float, x: Float, y: Float, largo: Float, ancho: Float, angulo: Float, hilo: Hilo, blanca: Boolean) {
        c.save()
        c.translate(x, y)
        c.rotate(Math.toDegrees(angulo.toDouble()).toFloat())
        val p = hojaPath(largo, ancho)
        relleno.color = tono(hilo, PRI, blanca)
        c.drawPath(p, relleno)
        c.save()
        c.translate(largo * 0.12f, -ancho * 0.12f)
        c.scale(0.72f, 0.42f)
        relleno.color = tono(hilo, CLA, blanca)
        c.drawPath(p, relleno)
        c.restore()
        // Nervadura en puntadas
        val vena = Path().apply {
            moveTo(largo * 0.08f, 0f)
            quadTo(largo * 0.5f, ancho * 0.12f, largo * 0.86f, 0f)
        }
        trazo.pathEffect = DashPathEffect(floatArrayOf(1.2f * u, 1.0f * u), 0f)
        trazo.strokeWidth = 0.9f * u
        trazo.color = tono(hilo, OSC, blanca, 0.8f)
        c.drawPath(vena, trazo)
        trazo.pathEffect = null
        trazo.strokeWidth = max(1.0f * u, ancho * 0.16f)
        trazo.color = tono(hilo, OSC, blanca)
        c.drawPath(p, trazo)
        c.restore()
    }

    /** Tallo como cordón torcido. [puntos] = x0, y0 y luego tríos de curvas (control x, y, fin x, y). */
    private fun tallo(c: Canvas, u: Float, puntos: FloatArray, hilo: Hilo, grosor: Float, blanca: Boolean) {
        val p = Path()
        p.moveTo(puntos[0], puntos[1])
        var i = 2
        while (i + 3 < puntos.size) {
            p.quadTo(puntos[i], puntos[i + 1], puntos[i + 2], puntos[i + 3])
            i += 4
        }
        trazo.pathEffect = null
        trazo.strokeWidth = grosor * u
        trazo.color = tono(hilo, OSC, blanca, 0.6f)
        c.drawPath(p, trazo)
        trazo.pathEffect = DashPathEffect(floatArrayOf(1.6f * u, 1.2f * u), 0f)
        trazo.strokeWidth = grosor * 0.6f * u
        trazo.color = tono(hilo, PRI, blanca)
        c.drawPath(p, trazo)
        trazo.pathEffect = null
    }

    private fun bolita(c: Canvas, u: Float, x: Float, y: Float, r: Float, hilo: Hilo, blanca: Boolean) {
        relleno.color = tono(hilo, PRI, blanca)
        c.drawCircle(x, y, r, relleno)
        trazo.pathEffect = null
        trazo.strokeWidth = max(0.7f * u, r * 0.3f)
        trazo.color = tono(hilo, OSC, blanca)
        c.drawCircle(x, y, r, trazo)
        relleno.color = tono(hilo, CLA, blanca)
        c.drawCircle(x - r * 0.3f, y - r * 0.3f, r * 0.28f, relleno)
    }

    private fun petaloPath(largo: Float, ancho: Float) = Path().apply {
        moveTo(0f, 0f)
        cubicTo(largo * 0.16f, -ancho * 0.95f, largo * 1.04f, -ancho * 0.9f, largo, 0f)
        cubicTo(largo * 1.04f, ancho * 0.9f, largo * 0.16f, ancho * 0.95f, 0f, 0f)
        close()
    }

    private fun hojaPath(largo: Float, ancho: Float) = Path().apply {
        moveTo(0f, 0f)
        cubicTo(largo * 0.25f, -ancho, largo * 0.72f, -ancho * 0.75f, largo, 0f)
        cubicTo(largo * 0.72f, ancho * 0.75f, largo * 0.25f, ancho, 0f, 0f)
        close()
    }

    /** Color del tono [rol] del [hilo]; en blanco, blanco con la opacidad del rol (x [extra]). */
    private fun tono(hilo: Hilo, rol: Int, blanca: Boolean, extra: Float = 1f): Int =
        if (!blanca) hilo.tonos[rol]
        else Color.argb((255 * ALFA_BLANCA[rol] * extra).roundToInt().coerceIn(0, 255), 255, 255, 255)

    private fun conAlfa(color: Int, alfa: Float): Int =
        Color.argb((255 * alfa).roundToInt(), Color.red(color), Color.green(color), Color.blue(color))
}
