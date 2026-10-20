package com.example.puriyayacucho.guia

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.example.puriyayacucho.data.CategoriaLugar

/**
 * Ícono de una categoría dentro de un círculo suave de su color
 * (cubiertos naranjas para comida, copa fucsia para bares, cama azul para hoteles...).
 * Se usa en los interruptores y en la lista de destinos de la Guía.
 */
object IconoCategoria {

    fun enCirculo(context: Context, categoria: CategoriaLugar, tamanoDp: Int = 34): Drawable {
        val d = context.resources.displayMetrics.density
        val tamano = (tamanoDp * d).toInt()
        val color = Color.parseColor(categoria.colorHex)

        val fondo = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(Color.argb(38, Color.red(color), Color.green(color), Color.blue(color)))
            setSize(tamano, tamano)
        }
        val icono = ContextCompat.getDrawable(context, categoria.icono)!!.mutate().apply { setTint(color) }

        return LayerDrawable(arrayOf(fondo, icono)).apply {
            val lado = (tamano * 0.56f).toInt()
            setLayerSize(1, lado, lado)
            setLayerGravity(1, Gravity.CENTER)
            setBounds(0, 0, tamano, tamano)
        }
    }
}
