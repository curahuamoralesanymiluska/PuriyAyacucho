package com.example.puriyayacucho.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.puriyayacucho.R
import com.example.puriyayacucho.data.ComidaTipica

class ComidaAdapter(
    private val comidas: List<ComidaTipica>,
    private val onItemClick: (ComidaTipica) -> Unit
) : RecyclerView.Adapter<ComidaAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivComida: ImageView = itemView.findViewById(R.id.ivComida)
        private val tvNombreComida: TextView = itemView.findViewById(R.id.tvNombreComida)

        fun bind(comida: ComidaTipica) {
            Glide.with(itemView.context)
                .load(comida.imagen)
                .placeholder(R.drawable.ic_food)
                .into(ivComida)

            tvNombreComida.text = comida.nombre

            itemView.setOnClickListener { onItemClick(comida) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comida_horizontal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(comidas[position])
    }

    override fun getItemCount(): Int = comidas.size
}
