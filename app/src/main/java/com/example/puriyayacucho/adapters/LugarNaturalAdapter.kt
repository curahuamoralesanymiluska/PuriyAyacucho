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
import com.example.puriyayacucho.VideoActivity
import com.example.puriyayacucho.data.LugarNatural

class LugarNaturalAdapter(
    private val lugares: List<LugarNatural>
) : RecyclerView.Adapter<LugarNaturalAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPlaceImage: ImageView = itemView.findViewById(R.id.ivPlaceImage)
        private val tvNombre: TextView = itemView.findViewById(R.id.tvPlaceName)
        private val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        private val tvHours: TextView = itemView.findViewById(R.id.tvHours)
        private val btnDetails: Button = itemView.findViewById(R.id.btnDetails)
        private val btnVideo: Button = itemView.findViewById(R.id.btnVideo)

        fun bind(lugar: LugarNatural) {
            Glide.with(itemView.context)
                .load(lugar.imagen)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_nature)
                .centerCrop()
                .into(ivPlaceImage)

            tvNombre.text = lugar.nombre
            tvDescripcion.text = lugar.descripcion
            tvLocation.text = lugar.ubicacion
            tvHours.text = "Altitud: ${lugar.altitud}"

            val openDetailsIntent = {
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

                // Resolución segura de ambigüedad de tipos para putExtra
                val img = lugar.imagen
                if (img is Int) {
                    val resId: Int = img
                    intent.putExtra("imageRes", resId)
                } else if (img is String) {
                    val url: String = img
                    intent.putExtra("imageUrl", url)
                }

                // Transferencia segura de galería
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

            btnDetails.setOnClickListener { openDetailsIntent() }
            itemView.setOnClickListener { openDetailsIntent() }

            btnVideo.setOnClickListener {
                if (lugar.videoUrl.isNotEmpty()) {
                    val intent = Intent(itemView.context, VideoActivity::class.java)
                    intent.putExtra("videoId", lugar.videoUrl)
                    intent.putExtra("title", lugar.nombre)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lugares[position])
    }

    override fun getItemCount(): Int = lugares.size
}
