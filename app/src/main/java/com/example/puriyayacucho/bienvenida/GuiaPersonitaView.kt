package com.example.puriyayacucho.bienvenida

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import android.util.AttributeSet
import android.view.View
import com.example.puriyayacucho.ui.BordadoPintor
import com.example.puriyayacucho.ui.MantaPintor
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

/**
 * La guía de Puriy: una personita andina propia de la app (chullo tejido, poncho rosa con una
 * franja de manta ayacuchana y una flor bordada en el pecho). Es la misma familia de la
 * personita que camina en el mapa y de la foto de perfil vacía.
 *
 * Se mueve sola: respira, parpadea y mueve la boca mientras [hablando] es true. Además:
 * - [entrar]: llega caminando desde la izquierda;
 * - [saludar]: agita la mano;
 * - [saltar]: da un saltito (al terminar la bienvenida).
 *
 * Si el celular tiene las animaciones desactivadas (accesibilidad), se queda quieta.
 * El dibujo usa una caja de 120 x 160 unidades con los pies abajo.
 */
class GuiaPersonitaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var hablando: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    private val animado = animacionesActivas(context)
    private var tEntrada = -1L
    private var tSaludo = -1L
    private var tSalto = -1L
    private var proximoParpadeo = 0L

    private val relleno = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val trazo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
    private val rect = RectF()
    private val boca = Path()
    private val zigzag = Path()
    private var florPecho: Bitmap? = null

    // Formas fijas (en unidades del dibujo)
    private val poncho = Path().apply {
        moveTo(46f, 80f)
        quadTo(36f, 82f, 29f, 90f)
        quadTo(22f, 110f, 22f, 127f)
        quadTo(60f, 137f, 98f, 127f)
        quadTo(98f, 110f, 91f, 90f)
        quadTo(84f, 82f, 74f, 80f)
        quadTo(60f, 86f, 46f, 80f)
        close()
    }
    private val chullo = Path().apply {
        moveTo(35f, 46f)
        cubicTo(33f, 20f, 87f, 20f, 85f, 46f)
        quadTo(60f, 41f, 35f, 46f)
        close()
    }
    private val flequillo = Path().apply {
        moveTo(38f, 44f)
        quadTo(44f, 52f, 49f, 46f)
        quadTo(54f, 53f, 60f, 46f)
        quadTo(66f, 53f, 71f, 46f)
        quadTo(76f, 52f, 82f, 44f)
        lineTo(82f, 40f)
        lineTo(38f, 40f)
        close()
    }
    private val orejeras = listOf(-1f, 1f).map { s ->
        Path().apply {
            moveTo(60f + s * 15f, 44f)
            quadTo(60f + s * 27f, 48f, 60f + s * 24f, 64f)
            quadTo(60f + s * 20f, 68f, 60f + s * 15f, 60f)
            close()
        }
    }
    private val cordones = listOf(-1f, 1f).map { s ->
        Path().apply {
            moveTo(60f + s * 21f, 65f)
            quadTo(60f + s * 23f, 72f, 60f + s * 21f, 78f)
        }
    }

    init {
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
    }

    /** Llega caminando desde la izquierda (aprox. 1 s). */
    fun entrar() {
        if (!animado) return
        tEntrada = SystemClock.uptimeMillis()
        invalidate()
    }

    /** Agita la mano unos segundos. */
    fun saludar() {
        if (!animado) return
        tSaludo = SystemClock.uptimeMillis()
        invalidate()
    }

    /** Un saltito de alegría. */
    fun saltar() {
        if (!animado) return
        tSalto = SystemClock.uptimeMillis()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val d = resources.displayMetrics.density
        setMeasuredDimension(
            resolveSize((150 * d).toInt(), widthMeasureSpec),
            resolveSize((190 * d).toInt(), heightMeasureSpec)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        florPecho = null
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (width == 0 || height == 0) return
        val t = SystemClock.uptimeMillis()
        val k = min(width / 120f, height / 160f)

        // ---- Estado de la animación en este instante ----
        var paso = Float.NaN
        var dx = 0f
        if (tEntrada >= 0) {
            val p = ((t - tEntrada) / DURACION_ENTRADA).coerceIn(0f, 1f)
            if (p < 1f) {
                val suave = 1f - (1f - p).pow(3)
                dx = -(1f - suave) * width * 1.05f
                paso = (t - tEntrada) / 1000f * 2f * PI.toFloat() * 2.2f
            } else {
                tEntrada = -1
            }
        }
        var ola = Float.NaN
        if (tSaludo >= 0) {
            val dt = t - tSaludo
            if (dt < DURACION_SALUDO) ola = dt / DURACION_SALUDO * 2f * PI.toFloat() * 3.2f else tSaludo = -1
        }
        var salto = 0f
        if (tSalto >= 0) {
            val dt = t - tSalto
            if (dt < DURACION_SALTO) salto = sin(dt / DURACION_SALTO * PI.toFloat()) * 14f else tSalto = -1
        }
        val abertura = if (hablando && animado) {
            (abs(sin(t / 85.0)) * (0.55 + 0.45 * abs(sin(t / 240.0)))).toFloat()
        } else 0f
        var ojosCerrados = false
        if (animado) {
            if (proximoParpadeo == 0L) proximoParpadeo = t + 1_800
            if (t >= proximoParpadeo) {
                if (t < proximoParpadeo + 130) ojosCerrados = true
                else proximoParpadeo = t + Random.nextLong(2_600, 4_400)
            }
        }
        val respira = if (animado) sin(t / 650.0).toFloat() else 0f

        // ---- Dibujo ----
        canvas.save()
        canvas.translate((width - 120f * k) / 2f + dx, height - 160f * k)
        canvas.scale(k, k)
        dibujar(canvas, k, paso, ola, salto, abertura, ojosCerrados, respira)
        canvas.restore()

        if (animado && isShown) postInvalidateOnAnimation()
    }

    private fun dibujar(
        c: Canvas, k: Float, paso: Float, ola: Float, salto: Float,
        abertura: Float, ojosCerrados: Boolean, respira: Float
    ) {
        val camina = !paso.isNaN()
        val saluda = !ola.isNaN()
        val bamboleo = if (camina) abs(sin(paso)) * 2.2f else 0f
        val dy = -salto - bamboleo + respira * 0.6f

        // Sombra en el piso (se achica al saltar)
        val anchoSombra = 26f * max(0.45f, 1f - salto / 30f)
        relleno.color = Color.argb(33, 20, 10, 30)
        rect.set(60f - anchoSombra, 150.5f, 60f + anchoSombra, 159.5f)
        c.drawOval(rect, relleno)

        // Piernas: de frente, al caminar cada pie se levanta por turnos
        val cadera = 128f + dy
        trazo.pathEffect = null
        for ((x, s) in listOf(51f to 1f, 69f to -1f)) {
            val levanta = if (camina) max(0f, sin(paso) * s) * 5f else 0f
            val fx = x + if (salto > 0f) -s * 2.5f else 0f
            val fy = cadera + 22f - levanta
            trazo.color = PANTALON
            trazo.strokeWidth = 9f
            c.drawLine(x, cadera, fx, fy, trazo)
            relleno.color = ZAPATO
            rect.set(fx - s * 1.5f - 6.5f, fy + 2.5f - 3.6f, fx - s * 1.5f + 6.5f, fy + 2.5f + 3.6f)
            c.drawOval(rect, relleno)
        }

        c.save()
        c.translate(0f, dy)
        if (camina) c.rotate(Math.toDegrees(sin(paso) * 0.045).toFloat(), 60f, 128f)

        // Brazos por detrás del poncho (el derecho solo si no está saludando)
        brazo(c, 33f, 92f, 0.34f + if (camina) sin(paso) * 0.12f else 0f, 22f, -1f)
        if (!saluda) brazo(c, 87f, 92f, 0.34f + if (camina) -sin(paso) * 0.12f else 0f, 22f, 1f)

        // Poncho con sombra suave a un lado y franja de manta abajo
        relleno.color = PONCHO
        c.drawPath(poncho, relleno)
        c.save()
        c.clipPath(poncho)
        relleno.color = Color.argb(26, 160, 0, 40)
        c.drawRect(78f, 80f, 108f, 140f, relleno)
        rect.set(18f, 113f, 104f, 121.5f)
        MantaPintor.dibujar(c, rect, MantaPintor.Estilo.COMPLETA)
        c.restore()

        // Flecos
        trazo.color = FRANJA
        trazo.strokeWidth = 1.6f
        var xf = 26f
        while (xf <= 94f) {
            val tt = (xf - 60f) / 38f
            val yf = 127f + (1f - tt * tt) * 7.5f - 0.6f
            c.drawLine(xf, yf, xf + 0.4f, yf + 4.2f, trazo)
            xf += 3.4f
        }

        // Cuello
        boca.reset()
        boca.moveTo(48f, 81f)
        boca.quadTo(60f, 89f, 72f, 81f)
        trazo.color = PONCHO_OSCURO
        trazo.strokeWidth = 2.2f
        c.drawPath(boca, trazo)

        // Flor bordada en el pecho (se dibuja una vez y se reutiliza)
        val flor = florPecho ?: crearFlorPecho(k).also { florPecho = it }
        rect.set(60f - 8.5f, 99f - 8.5f, 60f + 8.5f, 99f + 8.5f)
        c.drawBitmap(flor, null, rect, relleno.apply { color = Color.WHITE })

        // Brazo que saluda, por delante
        if (saluda) brazo(c, 87f, 92f, 2.5f + sin(ola) * 0.28f, 23f, 1f)

        cabeza(c, abertura, ojosCerrados)
        c.restore()
    }

    /** [angulo]: 0 = hacia abajo, PI = hacia arriba. [afuera]: -1 brazo izquierdo, 1 derecho. */
    private fun brazo(c: Canvas, x: Float, y: Float, angulo: Float, largo: Float, afuera: Float) {
        val hx = x + afuera * sin(angulo) * largo
        val hy = y + cos(angulo) * largo
        trazo.pathEffect = null
        trazo.color = PONCHO
        trazo.strokeWidth = 9f
        c.drawLine(x, y, hx, hy, trazo)
        relleno.color = PIEL
        c.drawCircle(hx, hy, 5f, relleno)
    }

    private fun cabeza(c: Canvas, abertura: Float, ojosCerrados: Boolean) {
        // Orejeras del chullo con su cordón y pompón (por detrás de la cara)
        relleno.color = CHULLO_OSCURO
        orejeras.forEach { c.drawPath(it, relleno) }
        trazo.color = FRANJA
        trazo.strokeWidth = 1.4f
        cordones.forEach { c.drawPath(it, trazo) }
        relleno.color = FRANJA
        c.drawCircle(39f, 79.5f, 2.8f, relleno)
        c.drawCircle(81f, 79.5f, 2.8f, relleno)

        // Cara y flequillo
        relleno.color = PIEL
        c.drawCircle(60f, 54f, 23f, relleno)
        relleno.color = PELO
        c.drawPath(flequillo, relleno)

        // Chullo tejido: franjas y zigzag
        relleno.color = CHULLO
        c.drawPath(chullo, relleno)
        c.save()
        c.clipPath(chullo)
        relleno.color = FRANJA
        c.drawRect(30f, 36.5f, 90f, 40.7f, relleno)
        dibujarZigzag(c, 34.5f, 31.5f, Color.WHITE, 1.4f)
        dibujarZigzag(c, 39.6f, 37.6f, PONCHO, 1.2f)
        relleno.color = Color.argb(217, 255, 255, 255)
        c.drawRect(30f, 42.5f, 90f, 44.1f, relleno)
        c.restore()

        // Pompón
        relleno.color = FRANJA
        c.drawCircle(60f, 22.5f, 5.6f, relleno)
        trazo.pathEffect = DashPathEffect(floatArrayOf(1.1f, 1.1f), 0f)
        trazo.color = Color.parseColor("#E2A800")
        trazo.strokeWidth = 1f
        c.drawCircle(60f, 22.5f, 5.6f, trazo)
        trazo.pathEffect = null

        // Mejillas
        relleno.color = MEJILLA
        c.drawCircle(46.5f, 62f, 4.2f, relleno)
        c.drawCircle(73.5f, 62f, 4.2f, relleno)

        // Ojos (con brillo) o parpadeo
        for (ox in floatArrayOf(51.5f, 68.5f)) {
            val oy = 55.5f
            if (!ojosCerrados) {
                relleno.color = OJO
                rect.set(ox - 2.7f, oy - 3.3f, ox + 2.7f, oy + 3.3f)
                c.drawOval(rect, relleno)
                relleno.color = Color.WHITE
                c.drawCircle(ox + 0.9f, oy - 1.2f, 0.95f, relleno)
            } else {
                boca.reset()
                boca.moveTo(ox - 2.8f, oy)
                boca.quadTo(ox, oy + 2f, ox + 2.8f, oy)
                trazo.color = OJO
                trazo.strokeWidth = 1.5f
                c.drawPath(boca, trazo)
            }
        }

        // Nariz
        boca.reset()
        boca.moveTo(58.6f, 60.5f)
        boca.quadTo(60f, 61.8f, 61.4f, 60.5f)
        trazo.color = Color.parseColor("#C98E66")
        trazo.strokeWidth = 1.1f
        c.drawPath(boca, trazo)

        // Boca: sonrisa o abierta mientras habla
        boca.reset()
        if (abertura < 0.15f) {
            boca.moveTo(54.5f, 65.2f)
            boca.quadTo(60f, 70.5f, 65.5f, 65.2f)
            trazo.color = BOCA
            trazo.strokeWidth = 1.7f
            c.drawPath(boca, trazo)
        } else {
            val ry = 1.4f + abertura * 2.6f
            boca.moveTo(55f, 65f)
            boca.quadTo(60f, 65.8f, 65f, 65f)
            boca.quadTo(64.5f, 65f + ry * 1.6f, 60f, 65f + ry * 1.7f)
            boca.quadTo(55.5f, 65f + ry * 1.6f, 55f, 65f)
            boca.close()
            relleno.color = BOCA
            c.drawPath(boca, relleno)
            c.save()
            c.clipPath(boca)
            relleno.color = Color.parseColor("#FF8FA3")
            rect.set(60f - 2.6f, 65f + ry * 1.7f - 1.6f, 60f + 2.6f, 65f + ry * 1.7f + 1.6f)
            c.drawOval(rect, relleno)
            c.restore()
        }
    }

    private fun dibujarZigzag(c: Canvas, yAbajo: Float, yArriba: Float, color: Int, grosor: Float) {
        zigzag.reset()
        var x = 31f
        var arriba = true
        zigzag.moveTo(x, yAbajo)
        while (x < 90f) {
            x += 3f
            arriba = !arriba
            zigzag.lineTo(x, if (arriba) yAbajo else yArriba)
        }
        trazo.color = color
        trazo.strokeWidth = grosor
        c.drawPath(zigzag, trazo)
    }

    /** La flor del pecho en un bitmap del tamaño real en pantalla (17 x 17 unidades). */
    private fun crearFlorPecho(k: Float): Bitmap {
        val lado = max(1, (17f * k).toInt())
        val bmp = Bitmap.createBitmap(lado, lado, Bitmap.Config.ARGB_8888)
        val escala = lado / 17f
        BordadoPintor.florOcho(
            Canvas(bmp), 0.35f * escala, lado / 2f, lado / 2f, 7.2f * escala,
            BordadoPintor.MOSTAZA, BordadoPintor.CORAL, 0.2f
        )
        return bmp
    }

    companion object {
        private const val DURACION_ENTRADA = 1_100f
        private const val DURACION_SALUDO = 1_700f
        private const val DURACION_SALTO = 650f

        private val PIEL = Color.parseColor("#F2C29B")
        private val PONCHO = Color.parseColor("#FF3355")
        private val PONCHO_OSCURO = Color.parseColor("#E0294A")
        private val FRANJA = Color.parseColor("#FFC93C")
        private val PANTALON = Color.parseColor("#2B2D42")
        private val ZAPATO = Color.parseColor("#5A3A2A")
        private val CHULLO = Color.parseColor("#FF7A18")
        private val CHULLO_OSCURO = Color.parseColor("#E2650C")
        private val PELO = Color.parseColor("#3B2A20")
        private val OJO = Color.parseColor("#2B2440")
        private val BOCA = Color.parseColor("#8E1F3A")
        private val MEJILLA = Color.argb(115, 255, 120, 140)

        /** false si el celular tiene las animaciones desactivadas (opción de accesibilidad). */
        fun animacionesActivas(context: Context): Boolean =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ValueAnimator.areAnimatorsEnabled()
            } else {
                Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) != 0f
            }
    }
}
