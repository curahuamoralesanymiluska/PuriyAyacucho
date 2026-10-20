package com.example.puriyayacucho.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.puriyayacucho.R
import com.example.puriyayacucho.data.FavoritoEntity
import com.google.android.material.button.MaterialButton

class FavoritoAdapter(
    private var favoritos: List<FavoritoEntity>,
    private val onDeleteClick: (FavoritoEntity) -> Unit,
    private val onViewClick: (FavoritoEntity) -> Unit
) : RecyclerView.Adapter<FavoritoAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPlaceImage: ImageView = itemView.findViewById(R.id.ivPlaceImage)
        private val tvPlaceName: TextView = itemView.findViewById(R.id.tvPlaceName)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val btnRemove: ImageView = itemView.findViewById(R.id.btnRemoveFavorite)
        private val btnView: MaterialButton = itemView.findViewById(R.id.btnView)

        fun bind(favorito: FavoritoEntity) {
            tvPlaceName.text = favorito.nombre
            tvCategory.text = favorito.tipo

            // Lógica para cargar imagen local o URL
            val imageToLoad: Any = if (favorito.imagenUrl.isNotEmpty()) favorito.imagenUrl else favorito.imagenRes

            Glide.with(itemView.context)
                .load(imageToLoad)
                .placeholder(R.drawable.logo)
                .centerCrop()
                .into(ivPlaceImage)

            btnRemove.setOnClickListener { onDeleteClick(favorito) }
            btnView.setOnClickListener { onViewClick(favorito) }
            itemView.setOnClickListener { onViewClick(favorito) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favoritos[position])
    }

    override fun getItemCount(): Int = favoritos.size

    fun updateList(newList: List<FavoritoEntity>) {
        favoritos = newList
        notifyDataSetChanged()
    }
}
