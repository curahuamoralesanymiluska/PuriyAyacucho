package com.example.puriyayacucho.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.example.puriyayacucho.R
import com.example.puriyayacucho.data.LugarIndexado
import com.example.puriyayacucho.guia.IconoCategoria
import java.text.Normalizer

/** Opción de la lista de destinos. [toString] es el texto que queda escrito al elegirla. */
class OpcionDestino(val lugar: LugarIndexado) {
    override fun toString(): String = lugar.lugar.nombre
}

/**
 * Lista desplegable de destinos de la Guía: cada lugar con el ícono y el color de su
 * categoría. Busca sin importar tildes ni mayúsculas ("cristobal" encuentra "San Cristóbal").
 */
class DestinoGuiaAdapter(
    private val context: Context,
    private val todas: List<OpcionDestino>
) : BaseAdapter(), Filterable {

    private var visibles: List<OpcionDestino> = todas

    override fun getCount(): Int = visibles.size
    override fun getItem(position: Int): OpcionDestino = visibles[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val vista = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_destino_guia, parent, false)
        val opcion = getItem(position)
        val categoria = opcion.lugar.categoria
        vista.findViewById<ImageView>(R.id.ivDestinoIcono).setImageDrawable(IconoCategoria.enCirculo(context, categoria))
        vista.findViewById<TextView>(R.id.tvDestinoNombre).text = opcion.lugar.lugar.nombre
        vista.findViewById<TextView>(R.id.tvDestinoCategoria).apply {
            setText(categoria.etiqueta)
            setTextColor(Color.parseColor(categoria.colorHex))
        }
        return vista
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(texto: CharSequence?): FilterResults {
            val buscado = normalizar(texto?.toString().orEmpty())
            val lista = if (buscado.isEmpty()) todas
            else todas.filter { normalizar(it.lugar.lugar.nombre).contains(buscado) }
                .sortedBy { if (normalizar(it.lugar.lugar.nombre).startsWith(buscado)) 0 else 1 }
            return FilterResults().apply {
                values = lista
                count = lista.size
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(texto: CharSequence?, resultados: FilterResults?) {
            visibles = (resultados?.values as? List<OpcionDestino>) ?: todas
            if (visibles.isEmpty()) notifyDataSetInvalidated() else notifyDataSetChanged()
        }

        override fun convertResultToString(resultado: Any?): CharSequence =
            (resultado as? OpcionDestino)?.toString() ?: ""
    }

    companion object {
        /** "San Cristóbal" -> "san cristobal" */
        fun normalizar(texto: String): String =
            Normalizer.normalize(texto.trim().lowercase(), Normalizer.Form.NFD)
                .replace(Regex("\\p{Mn}+"), "")
    }
}
