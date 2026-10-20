package com.example.puriyayacucho

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.puriyayacucho.data.Comodidad

class ComodidadesAdapter(private val comodidades: List<Comodidad>) :
    RecyclerView.Adapter<ComodidadesAdapter.ComodidadViewHolder>() {

    class ComodidadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcono: ImageView = view.findViewById(R.id.ivIcono)
        val tvEtiqueta: TextView = view.findViewById(R.id.tvEtiqueta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComodidadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comodidad, parent, false)
        return ComodidadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComodidadViewHolder, position: Int) {
        val comodidad = comodidades[position]
        holder.ivIcono.setImageResource(comodidad.icono)
        holder.tvEtiqueta.setText(comodidad.etiqueta)
    }

    override fun getItemCount() = comodidades.size
}
