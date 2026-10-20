package com.example.puriyayacucho.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.puriyayacucho.PlaceDetailsActivity
import com.example.puriyayacucho.R
import com.example.puriyayacucho.data.Restaurante

class RestauranteAdapter(
    private val restaurantes: List<Restaurante>
) : RecyclerView.Adapter<RestauranteAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivRestaurantImage: ImageView = itemView.findViewById(R.id.ivRestaurantImage)
        private val tvNombre: TextView = itemView.findViewById(R.id.tvRestaurantName)
        private val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        private val tvFoodType: TextView = itemView.findViewById(R.id.tvFoodType)
        private val tvDeliveryTime: TextView = itemView.findViewById(R.id.tvDeliveryTime)
        private val btnReservar: Button? = itemView.findViewById(R.id.btnReservar)

        fun bind(restaurante: Restaurante) {
            Glide.with(itemView.context)
                .load(restaurante.imagen)
                .placeholder(R.drawable.logo)
                .centerCrop()
                .into(ivRestaurantImage)

            tvNombre.text = restaurante.nombre
            tvRating.text = restaurante.rating.toString()
            tvFoodType.text = "Gastronomía local"
            tvDeliveryTime.text = restaurante.precio

            val openDetails = {
                val intent = Intent(itemView.context, PlaceDetailsActivity::class.java).apply {
                    putExtra("placeId", restaurante.id + 1000)
                    putExtra("placeName", restaurante.nombre)
                    putExtra("description", restaurante.descripcion)
                    putExtra("category", "Restaurante")
                    putExtra("location", restaurante.ubicacion)

                    val img = restaurante.imagen
                    if (img is Int) {
                        putExtra("imageRes", img.toInt())
                    } else if (img is String) {
                        putExtra("imageUrl", img.toString())
                    }

                    putExtra("hours", restaurante.horario)
                    putExtra("latitude", restaurante.coordenadas.first)
                    putExtra("longitude", restaurante.coordenadas.second)
                    putExtra("videoUrl", restaurante.videoUrl)

                    val galleryRes = ArrayList<Int>()
                    val galleryUrls = ArrayList<String>()
                    restaurante.galeria.forEach { item ->
                        if (item is Int) galleryRes.add(item)
                        else if (item is String) galleryUrls.add(item)
                    }
                    putExtra("galleryImages", galleryRes)
                    putExtra("galleryUrls", galleryUrls)
                }
                itemView.context.startActivity(intent)
            }

            itemView.setOnClickListener { openDetails() }
            btnReservar?.setOnClickListener { openDetails() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(restaurantes[position])
    }

    override fun getItemCount(): Int = restaurantes.size
}
