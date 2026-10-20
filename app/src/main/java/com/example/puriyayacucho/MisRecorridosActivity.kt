package com.example.puriyayacucho

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puriyayacucho.adapters.FilaSimple
import com.example.puriyayacucho.adapters.FilaSimpleAdapter
import com.example.puriyayacucho.data.AppDatabase
import com.example.puriyayacucho.data.CategoriaLugar
import com.example.puriyayacucho.data.RecorridoEntity
import com.example.puriyayacucho.guia.ModoViaje
import kotlinx.coroutines.launch

/** Lista de recorridos guardados. Al tocar uno se vuelve a abrir en el mapa. */
class MisRecorridosActivity : BaseActivity() {

    private var recorridos: List<RecorridoEntity> = emptyList()
    private lateinit var adapter: FilaSimpleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        findViewById<TextView>(R.id.tvListaTitulo).setText(R.string.my_routes)
        findViewById<TextView>(R.id.tvListaVacia).setText(R.string.my_routes_empty)
        findViewById<View>(R.id.btnListaVolver).setOnClickListener { finish() }

        adapter = FilaSimpleAdapter(emptyList()) { pos ->
            recorridos.getOrNull(pos)?.let { abrir(it) }
        }
        findViewById<RecyclerView>(R.id.rvLista).apply {
            layoutManager = LinearLayoutManager(this@MisRecorridosActivity)
            adapter = this@MisRecorridosActivity.adapter
        }
    }

    override fun onResume() {
        super.onResume()
        val correo = getSharedPreferences("usuario_sesion", MODE_PRIVATE).getString("correo_usuario", "") ?: ""
        lifecycleScope.launch {
            recorridos = AppDatabase.getInstance(this@MisRecorridosActivity).recorridoDao().obtenerRecorridos(correo)
            adapter.actualizar(recorridos.map { r ->
                val paradas = runCatching { RecorridoActivity.waypointsDesdeJson(r.waypointsJson).size - 2 }.getOrDefault(0)
                FilaSimple(
                    icono = R.drawable.ic_route,
                    colorHex = CategoriaLugar.TEMPLO.colorHex,
                    titulo = r.titulo,
                    detalle = "${r.fecha} · ${nombreModo(r.modo)}" +
                        if (paradas > 0) " · " + getString(R.string.my_routes_stops, paradas) else ""
                )
            })
            findViewById<View>(R.id.tvListaVacia).visibility = if (recorridos.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun nombreModo(modo: String): String = when (modo) {
        ModoViaje.A_PIE.name -> getString(R.string.mode_walk)
        ModoViaje.TAXI.name -> getString(R.string.mode_taxi)
        else -> getString(R.string.mode_transport)
    }

    private fun abrir(r: RecorridoEntity) {
        val waypoints = runCatching { RecorridoActivity.waypointsDesdeJson(r.waypointsJson) }.getOrNull() ?: return
        if (waypoints.size < 2) return
        val modo = runCatching { ModoViaje.valueOf(r.modo) }.getOrDefault(ModoViaje.TRANSPORTE)
        RecorridoActivity.abrir(
            this, waypoints, modo,
            arrayListOf(CategoriaLugar.RESTAURANTE.clave, CategoriaLugar.HOTEL.clave)
        )
    }
}
