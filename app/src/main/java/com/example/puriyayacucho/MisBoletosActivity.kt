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
import com.example.puriyayacucho.data.BoletoEntity
import kotlinx.coroutines.launch
import java.util.Locale

/** Boletos comprados por el usuario. */
class MisBoletosActivity : BaseActivity() {

    private var boletos: List<BoletoEntity> = emptyList()
    private lateinit var adapter: FilaSimpleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        findViewById<TextView>(R.id.tvListaTitulo).setText(R.string.my_tickets)
        findViewById<TextView>(R.id.tvListaVacia).setText(R.string.my_tickets_empty)
        findViewById<View>(R.id.btnListaVolver).setOnClickListener { finish() }

        adapter = FilaSimpleAdapter(emptyList()) { pos ->
            boletos.getOrNull(pos)?.let { BoletoActivity.abrir(this, it.id) }
        }
        findViewById<RecyclerView>(R.id.rvLista).apply {
            layoutManager = LinearLayoutManager(this@MisBoletosActivity)
            adapter = this@MisBoletosActivity.adapter
        }
    }

    override fun onResume() {
        super.onResume()
        val correo = getSharedPreferences("usuario_sesion", MODE_PRIVATE).getString("correo_usuario", "") ?: ""
        lifecycleScope.launch {
            boletos = AppDatabase.getInstance(this@MisBoletosActivity).boletoDao().obtenerBoletos(correo)
            adapter.actualizar(boletos.map { b ->
                FilaSimple(
                    icono = R.drawable.ic_ticket,
                    colorHex = "#FF3D5A",
                    titulo = b.nombreEvento,
                    detalle = "${b.fecha} · ${b.hora} · ${b.cantidad} ${b.tipoEntrada} · " +
                        String.format(Locale.US, "S/ %.2f", b.total)
                )
            })
            findViewById<View>(R.id.tvListaVacia).visibility = if (boletos.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}
