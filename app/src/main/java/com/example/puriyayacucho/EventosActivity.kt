package com.example.puriyayacucho

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puriyayacucho.adapters.EventoAdapter
import com.example.puriyayacucho.data.EventoFactory
import com.example.puriyayacucho.data.TipoEvento
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/** Lista de eventos con venta de boletos (Todos / Conciertos / Festividades / Teatro). */
class EventosActivity : BaseActivity() {

    private val eventos = EventoFactory.obtenerEventos()
    private lateinit var adapter: EventoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        findViewById<TextView>(R.id.tvListaTitulo).setText(R.string.events_title)
        findViewById<TextView>(R.id.tvListaVacia).setText(R.string.events_empty)
        findViewById<View>(R.id.btnListaVolver).setOnClickListener { finish() }

        adapter = EventoAdapter(eventos) { evento -> CompraActivity.abrir(this, evento.id) }
        findViewById<RecyclerView>(R.id.rvLista).apply {
            layoutManager = LinearLayoutManager(this@EventosActivity)
            adapter = this@EventosActivity.adapter
        }

        configurarFiltros()
    }

    private fun configurarFiltros() {
        findViewById<View>(R.id.hsvListaFiltros).visibility = View.VISIBLE
        val grupo = findViewById<ChipGroup>(R.id.chipGroupListaFiltros)
        val opciones: List<Pair<Int, TipoEvento?>> = listOf(
            R.string.filter_all to null,
            R.string.event_type_concert to TipoEvento.CONCIERTO,
            R.string.event_type_festivity to TipoEvento.FESTIVIDAD,
            R.string.event_type_theater to TipoEvento.TEATRO
        )
        opciones.forEachIndexed { i, (texto, tipo) ->
            val chip = Chip(this).apply {
                id = View.generateViewId()
                setText(texto)
                isCheckable = true
                isCheckedIconVisible = false
                chipBackgroundColor = ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
                    intArrayOf(Color.parseColor("#FF3D5A"), Color.parseColor("#ECECF1"))
                )
                setTextColor(
                    ColorStateList(
                        arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
                        intArrayOf(Color.WHITE, Color.parseColor("#666666"))
                    )
                )
                setOnCheckedChangeListener { _, marcado -> if (marcado) filtrar(tipo) }
            }
            grupo.addView(chip)
            if (i == 0) chip.isChecked = true
        }
    }

    private fun filtrar(tipo: TipoEvento?) {
        val lista = if (tipo == null) eventos else eventos.filter { it.tipo == tipo }
        adapter.actualizar(lista)
        findViewById<View>(R.id.tvListaVacia).visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
    }
}
