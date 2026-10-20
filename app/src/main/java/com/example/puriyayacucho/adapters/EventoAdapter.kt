package com.example.puriyayacucho.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.puriyayacucho.R
import com.example.puriyayacucho.data.EventoBoleto
import java.util.Locale

class EventoAdapter(
    private var eventos: List<EventoBoleto>,
    private val onComprar: (EventoBoleto) -> Unit
) : RecyclerView.Adapter<EventoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImagen: ImageView = itemView.findViewById(R.id.ivEventoImagen)
        val tvDia: TextView = itemView.findViewById(R.id.tvEventoDia)
        val tvMes: TextView = itemView.findViewById(R.id.tvEventoMes)
        val tvTipo: TextView = itemView.findViewById(R.id.tvEventoTipo)
        val tvNombre: TextView = itemView.findViewById(R.id.tvEventoNombre)
        val tvLugar: TextView = itemView.findViewById(R.id.tvEventoLugar)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvEventoPrecio)
        val btnComprar: View = itemView.findViewById(R.id.btnEventoComprar)
    }

    fun actualizar(nuevos: List<EventoBoleto>) {
        eventos = nuevos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_evento_boleto, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val evento = eventos[position]
        val ctx = holder.itemView.context
        Glide.with(ctx).load(evento.imagen).placeholder(R.drawable.ph_evento).centerCrop().into(holder.ivImagen)

        val partes = evento.fecha.split("/")
        holder.tvDia.text = partes.getOrNull(0) ?: ""
        val meses = ctx.resources.getStringArray(R.array.meses_cortos)
        holder.tvMes.text = partes.getOrNull(1)?.toIntOrNull()?.let { meses.getOrNull(it - 1) } ?: ""

        holder.tvTipo.setText(evento.tipo.etiqueta)
        holder.tvTipo.backgroundTintList = ColorStateList.valueOf(Color.parseColor(evento.tipo.colorHex))
        holder.tvNombre.text = evento.nombre
        holder.tvLugar.text = evento.lugar
        holder.tvPrecio.text = ctx.getString(R.string.event_from_price, String.format(Locale.US, "%.2f", evento.precioDesde))

        holder.btnComprar.setOnClickListener { onComprar(evento) }
        holder.itemView.setOnClickListener { onComprar(evento) }
    }

    override fun getItemCount(): Int = eventos.size
}
