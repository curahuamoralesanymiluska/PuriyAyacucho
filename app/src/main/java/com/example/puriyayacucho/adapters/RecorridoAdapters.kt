package com.example.puriyayacucho.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.puriyayacucho.DetalleNavigator
import com.example.puriyayacucho.R
import com.example.puriyayacucho.data.LugarIndexado
import com.example.puriyayacucho.guia.IconoCategoria
import com.example.puriyayacucho.guia.formatearDistancia

/** Un paso ya traducido y listo para mostrar en la línea de tiempo. */
data class PasoUi(
    @DrawableRes val icono: Int,
    val colorHex: String,
    val titulo: String,
    val detalle: String
)

class PasoAdapter : ListAdapter<PasoUi, PasoAdapter.ViewHolder>(object : DiffUtil.ItemCallback<PasoUi>() {
    override fun areItemsTheSame(a: PasoUi, b: PasoUi) = a == b
    override fun areContentsTheSame(a: PasoUi, b: PasoUi) = a == b
}) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flIcono: FrameLayout = itemView.findViewById(R.id.flPasoIcono)
        val ivIcono: ImageView = itemView.findViewById(R.id.ivPasoIcono)
        val vLinea: View = itemView.findViewById(R.id.vPasoLinea)
        val tvTitulo: TextView = itemView.findViewById(R.id.tvPasoTitulo)
        val tvDetalle: TextView = itemView.findViewById(R.id.tvPasoDetalle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_paso, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val paso = getItem(position)
        val color = Color.parseColor(paso.colorHex)
        holder.flIcono.backgroundTintList = ColorStateList.valueOf(color)
        holder.ivIcono.setImageResource(paso.icono)
        holder.vLinea.setBackgroundColor(color)
        holder.vLinea.alpha = 0.35f
        holder.vLinea.visibility = if (position == getItemCount() - 1) View.INVISIBLE else View.VISIBLE
        holder.tvTitulo.text = paso.titulo
        holder.tvDetalle.text = paso.detalle
        holder.tvDetalle.visibility = if (paso.detalle.isEmpty()) View.GONE else View.VISIBLE
    }
}

/** Un lugar cerca de la ruta que se puede agregar como parada. */
data class CercanoUi(
    val item: LugarIndexado,
    val distanciaMetros: Double,
    val agregado: Boolean
)

class ParadaCercanaAdapter(
    private val onAgregar: (CercanoUi) -> Unit
) : ListAdapter<CercanoUi, ParadaCercanaAdapter.ViewHolder>(object : DiffUtil.ItemCallback<CercanoUi>() {
    override fun areItemsTheSame(a: CercanoUi, b: CercanoUi) = a.item.lugar.id == b.item.lugar.id
    override fun areContentsTheSame(a: CercanoUi, b: CercanoUi) = a == b
}) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImagen: ImageView = itemView.findViewById(R.id.ivCercanoImagen)
        val tvNombre: TextView = itemView.findViewById(R.id.tvCercanoNombre)
        val tvCategoria: TextView = itemView.findViewById(R.id.tvCercanoCategoria)
        val tvDistancia: TextView = itemView.findViewById(R.id.tvCercanoDistancia)
        val btnAgregar: ImageView = itemView.findViewById(R.id.btnCercanoAgregar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_parada_cercana, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cercano = getItem(position)
        val lugar = cercano.item.lugar
        Glide.with(holder.itemView.context)
            .load(lugar.imagen)
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .centerCrop()
            .into(holder.ivImagen)
        holder.tvNombre.text = lugar.nombre
        holder.tvCategoria.text = lugar.tipo
        holder.tvCategoria.setTextColor(Color.parseColor(cercano.item.categoria.colorHex))
        // Etiqueta con el ícono de la categoría (cubiertos, copa, cama...)
        val ctx = holder.itemView.context
        holder.tvCategoria.setCompoundDrawablesRelative(IconoCategoria.enCirculo(ctx, cercano.item.categoria, 20), null, null, null)
        holder.tvCategoria.compoundDrawablePadding = (5 * ctx.resources.displayMetrics.density).toInt()
        holder.tvDistancia.text = formatearDistancia(cercano.distanciaMetros)
        holder.btnAgregar.setImageResource(if (cercano.agregado) R.drawable.ic_check else R.drawable.ic_agregar)
        holder.btnAgregar.isEnabled = !cercano.agregado
        holder.btnAgregar.setOnClickListener { onAgregar(cercano) }
        holder.itemView.setOnClickListener { DetalleNavigator.abrir(holder.itemView.context, lugar) }
    }
}
