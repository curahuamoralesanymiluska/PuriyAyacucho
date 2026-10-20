package com.example.puriyayacucho.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.puriyayacucho.R
import com.example.puriyayacucho.data.Festividad

class FestividadCalendarAdapter(
    private var festividades: List<Festividad>,
    private val onItemClick: (Festividad) -> Unit
) : RecyclerView.Adapter<FestividadCalendarAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDayNumber: TextView = view.findViewById(R.id.tvDayNumber)
        val tvEventName: TextView = view.findViewById(R.id.tvEventName)
        val tvEventLocation: TextView = view.findViewById(R.id.tvEventLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_festividad_calendar, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val festividad = festividades[position]
        holder.tvDayNumber.text = festividad.dia.toString()
        holder.tvEventName.text = festividad.nombre
        holder.tvEventLocation.text = festividad.ubicacion

        holder.itemView.setOnClickListener { onItemClick(festividad) }
    }

    override fun getItemCount() = festividades.size

    fun updateData(newFestividades: List<Festividad>) {
        festividades = newFestividades
        notifyDataSetChanged()
    }
}
