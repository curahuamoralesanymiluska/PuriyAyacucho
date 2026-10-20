package com.example.puriyayacucho.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.puriyayacucho.R
import com.example.puriyayacucho.data.CategoriaLugar

/**
 * Tarjetas de "Vive Ayacucho" en Inicio (Iglesias, Museos, Comida, Hoteles...).
 * Cada tarjeta usa el degradado de su categoría y muestra cuántos lugares tiene.
 */
class ModuloAdapter(
    private val modulos: List<CategoriaLugar>,
    /** Cantidad de lugares por categoría (si no se pasa, no se muestra). */
    private val cantidades: Map<CategoriaLugar, Int> = emptyMap(),
    private val onClick: (CategoriaLugar) -> Unit
) : RecyclerView.Adapter<ModuloAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flFondo: FrameLayout = itemView.findViewById(R.id.flModuloFondo)
        val ivIcono: ImageView = itemView.findViewById(R.id.ivModuloIcono)
        val ivMarca: ImageView = itemView.findViewById(R.id.ivModuloMarca)
        val tvNombre: TextView = itemView.findViewById(R.id.tvModuloNombre)
        val tvCantidad: TextView = itemView.findViewById(R.id.tvModuloCantidad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_modulo, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modulo = modulos[position]
        val ctx = holder.itemView.context
        val color = Color.parseColor(modulo.colorHex)
        val colorClaro = Color.parseColor(modulo.colorClaroHex)

        holder.flFondo.background = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(color, colorClaro)
        ).apply { cornerRadius = 24 * ctx.resources.displayMetrics.density }
        // Sombra del mismo color de la tarjeta (Android 9+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            holder.itemView.outlineSpotShadowColor = color
            holder.itemView.outlineAmbientShadowColor = color
        }

        holder.ivIcono.setImageResource(modulo.icono)
        holder.ivMarca.setImageResource(modulo.icono)
        holder.tvNombre.setText(modulo.etiqueta)

        val cantidad = cantidades[modulo]
        if (cantidad != null && cantidad > 0) {
            holder.tvCantidad.visibility = View.VISIBLE
            holder.tvCantidad.text = ctx.resources.getQuantityString(R.plurals.experiencias_lugares, cantidad, cantidad)
        } else {
            holder.tvCantidad.visibility = View.GONE
        }

        holder.itemView.contentDescription = ctx.getString(modulo.etiqueta)
        holder.itemView.setOnClickListener { onClick(modulo) }
    }

    override fun getItemCount(): Int = modulos.size
}
