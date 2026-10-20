package com.example.puriyayacucho.guia

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.example.puriyayacucho.R
import com.example.puriyayacucho.data.CategoriaLugar
import com.example.puriyayacucho.data.LugarIndexado
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

/**
 * Íconos propios para el mapa de la guía: pines con el ícono de cada categoría
 * (cubiertos para comida, copa para bares, cama para hoteles...), el punto "Tú",
 * los paraderos y la personita del viaje.
 *
 * Solo se puede usar después de que el mapa esté listo (onMapReady).
 */
object MarcadoresMapa {

    private val cache = HashMap<String, BitmapDescriptor>()

    const val COLOR_CAMINAR = "#1E88E5"
    const val COLOR_COMBI = "#FF7A18"
    const val COLOR_TAXI = "#FFB300"
    const val COLOR_PUNTO_TU = "#3B6CF6"
    const val COLOR_MARCA = "#FF3355"

    /** Pin en forma de gota con el ícono de la categoría. */
    fun pinCategoria(context: Context, categoria: CategoriaLugar, grande: Boolean = false): BitmapDescriptor =
        pin(context, categoria.icono, Color.parseColor(categoria.colorHex), if (grande) 48f else 36f)

    /** Pin pequeño (solo ícono) para los lugares cercanos a la ruta: no tapa el mapa. */
    fun pinPequeno(context: Context, categoria: CategoriaLugar): BitmapDescriptor =
        pin(context, categoria.icono, Color.parseColor(categoria.colorHex), 26f)

    /**
     * Etiqueta con nombre: píldora blanca con el ícono en un círculo de color y el texto,
     * y una puntita abajo que señala el lugar (ancla en 0.5, 1).
     * Ej.: [copa] Magia Negra · [bus] Sube aquí · Paradero Magdalena.
     */
    fun etiqueta(context: Context, texto: String, @DrawableRes icono: Int, @ColorInt color: Int): BitmapDescriptor =
        cache.getOrPut("etq-$texto-$icono-$color") {
            val d = context.resources.displayMetrics.density
            val alto = 30f * d
            val punta = 6f * d
            val margen = 3f * d
            val pinturaTexto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                this.color = Color.parseColor("#1D1D27")
                textSize = 12f * d
                typeface = Typeface.DEFAULT_BOLD
            }
            val visible = recortar(texto, pinturaTexto, 210f * d)
            val anchoTexto = pinturaTexto.measureText(visible)
            val diametro = alto - 6f * d
            val ancho = 3f * d + diametro + 7f * d + anchoTexto + 11f * d
            val w = (ancho + margen * 2).toInt()
            val h = (alto + punta + margen * 2).toInt()
            val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)

            val caja = RectF(margen, margen, margen + ancho, margen + alto)
            val forma = Path().apply {
                addRoundRect(caja, alto / 2, alto / 2, Path.Direction.CW)
                moveTo(w / 2f - punta, caja.bottom - 1)
                lineTo(w / 2f, caja.bottom + punta)
                lineTo(w / 2f + punta, caja.bottom - 1)
                close()
            }
            canvas.drawPath(forma, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                this.color = Color.WHITE
                setShadowLayer(2.5f * d, 0f, 1f * d, Color.argb(70, 0, 0, 0))
            })
            val cx = caja.left + 3f * d + diametro / 2
            val cy = caja.centerY()
            canvas.drawCircle(cx, cy, diametro / 2, Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color })
            dibujarIcono(context, canvas, icono, cx, cy, diametro * 0.62f, Color.WHITE)
            val base = cy - (pinturaTexto.descent() + pinturaTexto.ascent()) / 2
            canvas.drawText(visible, cx + diametro / 2 + 7f * d, base, pinturaTexto)
            BitmapDescriptorFactory.fromBitmap(bmp)
        }

    /** Acorta el texto con "…" si no entra en [maximo] píxeles. */
    private fun recortar(texto: String, pintura: Paint, maximo: Float): String {
        if (pintura.measureText(texto) <= maximo) return texto
        var fin = texto.length
        while (fin > 1 && pintura.measureText(texto.substring(0, fin).trimEnd() + "…") > maximo) fin--
        return texto.substring(0, fin).trimEnd() + "…"
    }

    /** Pin del destino cuando no sabemos su categoría (bandera rosa). */
    fun pinDestino(context: Context): BitmapDescriptor =
        pin(context, R.drawable.ic_bandera, Color.parseColor(COLOR_MARCA), 48f)

    fun pin(context: Context, @DrawableRes icono: Int, @ColorInt color: Int, anchoDp: Float): BitmapDescriptor =
        cache.getOrPut("pin-$icono-$color-$anchoDp") {
            val d = context.resources.displayMetrics.density
            val ancho = anchoDp * d
            val margen = 3f * d // espacio para la sombra
            val w = (ancho + margen * 2).toInt()
            val h = (ancho * 1.28f + margen * 2).toInt()
            val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            val cx = w / 2f
            val cy = margen + ancho / 2f
            val r = ancho / 2f

            val gota = Path().apply {
                addCircle(cx, cy, r, Path.Direction.CW)
                moveTo(cx - r * 0.55f, cy + r * 0.72f)
                lineTo(cx, h - margen)
                lineTo(cx + r * 0.55f, cy + r * 0.72f)
                close()
            }
            // Borde blanco con sombra suave
            canvas.drawPath(gota, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                this.color = Color.WHITE
                setShadowLayer(2.5f * d, 0f, 1.2f * d, Color.argb(80, 0, 0, 0))
            })
            // Círculo de color
            canvas.drawCircle(cx, cy, r - 2.5f * d, Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color })
            dibujarIcono(context, canvas, icono, cx, cy, r * 1.05f, Color.WHITE)
            BitmapDescriptorFactory.fromBitmap(bmp)
        }

    /** Punto "Tú" (origen): círculo azul con halo y la silueta de una persona. */
    fun puntoTu(context: Context): BitmapDescriptor = circulo(context, R.drawable.ic_persona, Color.parseColor(COLOR_PUNTO_TU), 30f, halo = true)

    /** Paradero de combi: cuadrito naranja con un bus. */
    fun paradero(context: Context): BitmapDescriptor =
        cache.getOrPut("paradero") {
            val d = context.resources.displayMetrics.density
            val lado = 28f * d
            val margen = 3f * d
            val w = (lado + margen * 2).toInt()
            val bmp = Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            val caja = RectF(margen, margen, margen + lado, margen + lado)
            canvas.drawRoundRect(caja, 8f * d, 8f * d, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                setShadowLayer(2f * d, 0f, 1f * d, Color.argb(80, 0, 0, 0))
            })
            val interior = RectF(caja.left + 2.5f * d, caja.top + 2.5f * d, caja.right - 2.5f * d, caja.bottom - 2.5f * d)
            canvas.drawRoundRect(interior, 6f * d, 6f * d, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor(COLOR_COMBI) })
            dibujarIcono(context, canvas, R.drawable.ic_bus, w / 2f, w / 2f, lado * 0.62f, Color.WHITE)
            BitmapDescriptorFactory.fromBitmap(bmp)
        }

    /** Cuántos cuadros tiene la animación de la personita en el mapa. */
    const val CUADROS_PERSONITA = 8

    /** Punto del dibujo que va sobre la ruta (los pies), como fracción del ancho y alto. */
    const val ANCLA_PERSONITA_X = 0.5f
    const val ANCLA_PERSONITA_Y = 0.86f

    /**
     * La personita andina del viaje, dibujada para el mapa: camina (cuadro a cuadro),
     * va en combi o en taxi, o salta al llegar. [haciaIzquierda] la voltea según
     * hacia dónde avanza la ruta.
     */
    fun personita(
        context: Context,
        medio: ProgresoViajeView.Medio,
        cuadro: Int,
        haciaIzquierda: Boolean,
        saltando: Boolean = false
    ): BitmapDescriptor {
        val c = Math.floorMod(cuadro, CUADROS_PERSONITA)
        return cache.getOrPut("persona-$medio-$c-$haciaIzquierda-$saltando") {
            val u = context.resources.displayMetrics.density * 1.25f
            val w = (84 * u).toInt()
            val h = (64 * u).toInt()
            val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            val x = w * ANCLA_PERSONITA_X
            val pies = h * ANCLA_PERSONITA_Y
            // Anillo rosa en el piso: marca dónde está la personita sobre la ruta
            val anillo = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.argb(70, 255, 51, 85) }
            canvas.drawOval(RectF(x - 16 * u, pies - 3 * u, x + 16 * u, pies + 7 * u), anillo)
            if (haciaIzquierda) canvas.scale(-1f, 1f, x, pies)
            val fase = c.toFloat() / CUADROS_PERSONITA * 2f * Math.PI.toFloat()
            val pintor = PersonitaPintor(u)
            when {
                saltando -> pintor.dibujarPersona(
                    canvas, x, pies, fase, saltando = true,
                    salto = kotlin.math.abs(kotlin.math.sin(fase / 2)) * 9f
                )
                medio == ProgresoViajeView.Medio.A_PIE -> pintor.dibujarPersona(canvas, x, pies, fase)
                else -> pintor.dibujarVehiculo(canvas, x, pies, fase, medio == ProgresoViajeView.Medio.TAXI, enMovimiento = true)
            }
            BitmapDescriptorFactory.fromBitmap(bmp)
        }
    }

    private fun circulo(context: Context, @DrawableRes icono: Int, @ColorInt color: Int, diametroDp: Float, halo: Boolean): BitmapDescriptor =
        cache.getOrPut("circ-$icono-$color-$diametroDp-$halo") {
            val d = context.resources.displayMetrics.density
            val diametro = diametroDp * d
            val extra = if (halo) diametro * 0.35f else 3f * d
            val w = (diametro + extra * 2).toInt()
            val bmp = Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            val c = w / 2f
            val r = diametro / 2f
            if (halo) {
                canvas.drawCircle(c, c, r + extra * 0.9f, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    this.color = Color.argb(55, Color.red(color), Color.green(color), Color.blue(color))
                })
            }
            canvas.drawCircle(c, c, r, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                this.color = Color.WHITE
                setShadowLayer(2.5f * d, 0f, 1f * d, Color.argb(90, 0, 0, 0))
            })
            canvas.drawCircle(c, c, r - 3f * d, Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color })
            dibujarIcono(context, canvas, icono, c, c, r * 1.1f, Color.WHITE)
            BitmapDescriptorFactory.fromBitmap(bmp)
        }

    private fun dibujarIcono(context: Context, canvas: Canvas, @DrawableRes icono: Int, cx: Float, cy: Float, tamano: Float, @ColorInt color: Int) {
        val drawable = ContextCompat.getDrawable(context, icono)?.mutate() ?: return
        drawable.setTint(color)
        val mitad = (tamano / 2).toInt()
        drawable.setBounds(cx.toInt() - mitad, cy.toInt() - mitad, cx.toInt() + mitad, cy.toInt() + mitad)
        drawable.draw(canvas)
    }

    /** Categoría de un lugar de la app a partir de su nombre (o null si no es un lugar de la app). */
    fun categoriaDe(todos: List<LugarIndexado>, nombre: String): CategoriaLugar? =
        todos.firstOrNull { it.lugar.nombre == nombre }?.categoria
}
