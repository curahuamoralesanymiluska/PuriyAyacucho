package com.example.puriyayacucho.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.puriyayacucho.R
import com.example.puriyayacucho.data.ReservaEntity

class ReservaAdapter(
    private var reservas: List<ReservaEntity>,
    private val onDeleteClick: (ReservaEntity) -> Unit
) : RecyclerView.Adapter<ReservaAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivLugar: ImageView = itemView.findViewById(R.id.ivLugarReserva)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreLugarReserva)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFechaReserva)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstadoReserva)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminarReserva)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reserva, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reserva = reservas[position]

        holder.tvNombre.text = reserva.nombreLugar
        holder.tvFecha.text = "Fecha: ${reserva.fecha}"
        holder.tvEstado.text = reserva.estado
        holder.ivLugar.setImageResource(if (reserva.imagenRes != 0) reserva.imagenRes else R.drawable.logo)

        // Configurar color según estado
        when (reserva.estado) {
            "Pendiente" -> {
                holder.tvEstado.setTextColor(android.graphics.Color.parseColor("#FF9800"))
                holder.tvEstado.setBackgroundResource(R.drawable.status_pending_bg)
            }
            "Completada" -> {
                holder.tvEstado.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
                holder.tvEstado.setBackgroundResource(R.drawable.status_completed_bg)
            }
            "Cancelada" -> {
                holder.tvEstado.setTextColor(android.graphics.Color.parseColor("#F44336"))
                holder.tvEstado.setBackgroundResource(R.drawable.status_cancelled_bg)
            }
        }

        holder.btnEliminar.setOnClickListener { onDeleteClick(reserva) }
    }

    override fun getItemCount(): Int = reservas.size

    fun updateList(newList: List<ReservaEntity>) {
        this.reservas = newList
        notifyDataSetChanged()
    }
}
