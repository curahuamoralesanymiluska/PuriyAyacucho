package com.example.puriyayacucho.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.puriyayacucho.DetalleNavigator
import com.example.puriyayacucho.R
import com.example.puriyayacucho.data.LugarTuristico

/** Una fila de la lista (hoteles, Airbnb, bares). */
data class FilaLugar(
    val lugar: LugarTuristico,
    val subtitulo: String,
    val colorSubtitulo: String,
    val rating: Float?,
    val precio: String,
    val extra: String = ""
)

/**
 * Tarjeta horizontal con miniatura (mismo estilo que item_restaurant_card).
 * Al tocarla abre el detalle del lugar.
 */
class LugarListaAdapter : ListAdapter<FilaLugar, LugarListaAdapter.ViewHolder>(Diff) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImagen: ImageView = itemView.findViewById(R.id.ivLugarImagen)
        private val tvNombre: TextView = itemView.findViewById(R.id.tvLugarNombre)
        private val tvSubtitulo: TextView = itemView.findViewById(R.id.tvLugarSubtitulo)
        private val tvRating: TextView = itemView.findViewById(R.id.tvLugarRating)
        private val tvPrecio: TextView = itemView.findViewById(R.id.tvLugarPrecio)
        private val tvExtra: TextView = itemView.findViewById(R.id.tvLugarExtra)

        fun bind(fila: FilaLugar) {
            Glide.with(itemView.context)
                .load(fila.lugar.imagen)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .centerCrop()
                .into(ivImagen)

            tvNombre.text = fila.lugar.nombre
            tvSubtitulo.text = fila.subtitulo
            tvSubtitulo.setTextColor(Color.parseColor(fila.colorSubtitulo))
            tvRating.text = fila.rating?.let { it.toString() } ?: ""
            tvRating.visibility = if (fila.rating == null) View.GONE else View.VISIBLE
            tvPrecio.text = fila.precio
            tvExtra.text = fila.extra
            tvExtra.visibility = if (fila.extra.isEmpty()) View.GONE else View.VISIBLE

            itemView.setOnClickListener { DetalleNavigator.abrir(itemView.context, fila.lugar) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lugar_lista, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    private object Diff : DiffUtil.ItemCallback<FilaLugar>() {
        override fun areItemsTheSame(a: FilaLugar, b: FilaLugar) = a.lugar.id == b.lugar.id
        override fun areContentsTheSame(a: FilaLugar, b: FilaLugar) = a == b
    }
}
