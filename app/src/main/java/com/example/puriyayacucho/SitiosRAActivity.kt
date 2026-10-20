package com.example.puriyayacucho

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puriyayacucho.data.SitioRA
import com.example.puriyayacucho.data.SitioRAFactory
import com.example.puriyayacucho.guia.Geo
import com.example.puriyayacucho.guia.UbicacionHelper
import com.example.puriyayacucho.guia.formatearDistancia
import com.example.puriyayacucho.ra.AccesoRA
import com.example.puriyayacucho.ra.ReconstruccionView

/**
 * "Viaja al pasado": muestra los sitios con Realidad Aumentada (Wari, Quinua y
 * Vilcashuamán) con una vista previa animada de la reconstrucción.
 * Se abre desde la tarjeta de RA en Inicio.
 */
class SitiosRAActivity : BaseActivity() {

    private val accesoRA = AccesoRA(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sitios_ra)
        window.statusBarColor = Color.parseColor("#1E0F3C")

        findViewById<View>(R.id.btnSitiosRAVolver).setOnClickListener { finish() }

        findViewById<RecyclerView>(R.id.rvSitiosRA).apply {
            layoutManager = LinearLayoutManager(this@SitiosRAActivity)
            adapter = SitioRAAdapter(
                SitioRAFactory.sitios,
                onVerRA = { accesoRA.iniciar(it) },
                onComoLlegar = { sitio ->
                    GuiaActivity.abrirConDestino(
                        this@SitiosRAActivity, getString(sitio.nombre), sitio.punto.lat, sitio.punto.lng
                    )
                }
            )
            scheduleLayoutAnimation()
        }
    }
}

/** Descripción corta de cada sitio para la tarjeta. */
private fun descripcionCorta(sitio: SitioRA): Int = when (sitio.id) {
    1 -> R.string.ar_site_wari_short
    2 -> R.string.ar_site_quinua_short
    else -> R.string.ar_site_vilcas_short
}

private class SitioRAAdapter(
    private val sitios: List<SitioRA>,
    private val onVerRA: (SitioRA) -> Unit,
    private val onComoLlegar: (SitioRA) -> Unit
) : RecyclerView.Adapter<SitioRAAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val holograma: ReconstruccionView = itemView.findViewById(R.id.hologramaSitio)
        val tvNombre: TextView = itemView.findViewById(R.id.tvSitioNombre)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvSitioDescripcion)
        val tvDistancia: TextView = itemView.findViewById(R.id.tvSitioDistancia)
        val btnVerRA: View = itemView.findViewById(R.id.btnSitioVerRA)
        val btnComoLlegar: View = itemView.findViewById(R.id.btnSitioComoLlegar)
        var animacion: ValueAnimator? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sitio_ra, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sitio = sitios[position]
        val ctx = holder.itemView.context
        holder.holograma.tipo = sitio.tipo
        holder.tvNombre.setText(sitio.nombre)
        holder.tvDescripcion.setText(descripcionCorta(sitio))
        val metros = Geo.distanciaMetros(UbicacionHelper.PLAZA_MAYOR, sitio.punto)
        holder.tvDistancia.text = ctx.getString(R.string.ar_site_distance, formatearDistancia(metros))
        holder.btnVerRA.setOnClickListener { onVerRA(sitio) }
        holder.btnComoLlegar.setOnClickListener { onComoLlegar(sitio) }
    }

    // La reconstrucción "sube y baja" solo mientras la tarjeta está en pantalla
    override fun onViewAttachedToWindow(holder: ViewHolder) {
        holder.animacion?.cancel()
        holder.animacion = ValueAnimator.ofFloat(0.1f, 1f).apply {
            duration = 2800
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { holder.holograma.progreso = it.animatedValue as Float }
            start()
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        holder.animacion?.cancel()
        holder.animacion = null
    }

    override fun getItemCount() = sitios.size
}
