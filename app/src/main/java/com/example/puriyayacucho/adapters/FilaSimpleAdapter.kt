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
import androidx.recyclerview.widget.RecyclerView
import com.example.puriyayacucho.R

/** Fila con ícono, título y detalle (Mis recorridos, Mis boletos). */
data class FilaSimple(
    @DrawableRes val icono: Int,
    val colorHex: String,
    val titulo: String,
    val detalle: String
)

class FilaSimpleAdapter(
    private var filas: List<FilaSimple>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<FilaSimpleAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flIcono: FrameLayout = itemView.findViewById(R.id.flFilaIcono)
        val ivIcono: ImageView = itemView.findViewById(R.id.ivFilaIcono)
        val tvTitulo: TextView = itemView.findViewById(R.id.tvFilaTitulo)
        val tvDetalle: TextView = itemView.findViewById(R.id.tvFilaDetalle)
    }

    fun actualizar(nuevas: List<FilaSimple>) {
        filas = nuevas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_fila_simple, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fila = filas[position]
        holder.flIcono.backgroundTintList = ColorStateList.valueOf(Color.parseColor(fila.colorHex))
        holder.ivIcono.setImageResource(fila.icono)
        holder.tvTitulo.text = fila.titulo
        holder.tvDetalle.text = fila.detalle
        holder.itemView.setOnClickListener { onClick(position) }
    }

    override fun getItemCount(): Int = filas.size
}
