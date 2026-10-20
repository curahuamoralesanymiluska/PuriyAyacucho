package com.example.puriyayacucho.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.puriyayacucho.PlaceDetailsActivity
import com.example.puriyayacucho.R
import com.example.puriyayacucho.data.LugarNatural

class DescubreAyacuchoAdapter(
    private val lugares: List<LugarNatural>
) : RecyclerView.Adapter<DescubreAyacuchoAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivLugar: ImageView = itemView.findViewById(R.id.ivLugar)
        private val tvEtiqueta: TextView = itemView.findViewById(R.id.tvEtiqueta)
        private val tvNombreLugar: TextView = itemView.findViewById(R.id.tvNombreLugar)
        private val tvDescripcionCorta: TextView = itemView.findViewById(R.id.tvDescripcionCorta)

        fun bind(lugar: LugarNatural) {
            Glide.with(itemView.context)
                .load(lugar.imagen)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_nature)
                .into(ivLugar)

            tvNombreLugar.text = lugar.nombre
            tvDescripcionCorta.text = lugar.descripcion
            tvEtiqueta.text = "Aventura Natural"

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PlaceDetailsActivity::class.java)
                intent.putExtra("placeId", lugar.id + 3000)
                intent.putExtra("placeName", lugar.nombre)

                // Información completa para la descripción
                val infoCompleta = "${lugar.descripcion}\n\n" +
                        "Actividades: ${lugar.actividades.joinToString(", ")}\n" +
                        "Acceso: ${lugar.acceso}"

                intent.putExtra("description", infoCompleta)
                intent.putExtra("category", "Naturaleza")
                intent.putExtra("location", lugar.ubicacion)
                intent.putExtra("recommendation", "Mejor época: ${lugar.mejorEpoca}")
                intent.putExtra("hours", "Altitud: ${lugar.altitud}")
                intent.putExtra("latitude", lugar.coordenadas.first)
                intent.putExtra("longitude", lugar.coordenadas.second)
                intent.putExtra("videoUrl", lugar.videoUrl)

                val img = lugar.imagen
                if (img is Int) intent.putExtra("imageRes", img)
                else if (img is String) intent.putExtra("imageUrl", img)

                // Galería
                val galleryRes = ArrayList<Int>()
                val galleryUrls = ArrayList<String>()
                lugar.galeria.forEach { item ->
                    if (item is Int) galleryRes.add(item)
                    else if (item is String) galleryUrls.add(item)
                }
                intent.putIntegerArrayListExtra("galleryImages", galleryRes)
                intent.putStringArrayListExtra("galleryUrls", galleryUrls)

                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_descubre_ayacucho, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lugares[position])
    }

    override fun getItemCount(): Int = lugares.size
}
