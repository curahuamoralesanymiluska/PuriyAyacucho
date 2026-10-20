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
import com.example.puriyayacucho.data.Museo

class MuseoAdapter(
    private val museos: List<Museo>
) : RecyclerView.Adapter<MuseoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPlaceImage: ImageView = itemView.findViewById(R.id.ivPlaceImage)
        private val tvNombre: TextView = itemView.findViewById(R.id.tvPlaceName)
        private val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        private val tvHours: TextView = itemView.findViewById(R.id.tvHours)

        fun bind(museo: Museo) {
            Glide.with(itemView.context)
                .load(museo.imagen)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_museum)
                .centerCrop()
                .into(ivPlaceImage)

            tvNombre.text = museo.nombre
            tvDescripcion.text = museo.descripcion
            tvLocation.text = museo.ubicacion
            tvHours.text = museo.horario

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PlaceDetailsActivity::class.java).apply {
                    putExtra("placeId", museo.id + 2000)
                    putExtra("placeName", museo.nombre)
                    putExtra("description", museo.descripcion)
                    putExtra("category", "Museo")
                    putExtra("location", museo.ubicacion)

                    // Manejo seguro de imagen
                    val img = museo.imagen
                    if (img is Int) {
                        putExtra("imageRes", img.toInt())
                    } else if (img is String) {
                        putExtra("imageUrl", img.toString())
                    }

                    putExtra("hours", museo.horario)
                    putExtra("latitude", museo.coordenadas.first)
                    putExtra("longitude", museo.coordenadas.second)
                    putExtra("videoUrl", museo.videoUrl)

                    // Manejo seguro de galería
                    val galleryRes = ArrayList<Int>()
                    val galleryUrls = ArrayList<String>()
                    museo.galeria.forEach { item ->
                        if (item is Int) galleryRes.add(item.toInt())
                        else if (item is String) galleryUrls.add(item.toString())
                    }
                    putExtra("galleryImages", galleryRes)
                    putExtra("galleryUrls", galleryUrls)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_place_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(museos[position])
    }

    override fun getItemCount(): Int = museos.size
}
