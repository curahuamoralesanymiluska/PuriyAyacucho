package com.example.puriyayacucho

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.puriyayacucho.adapters.DestinoGuiaAdapter
import com.example.puriyayacucho.adapters.OpcionDestino
import com.example.puriyayacucho.data.CategoriaLugar
import com.example.puriyayacucho.data.LugarIndexado
import com.example.puriyayacucho.data.LugarMapper
import com.example.puriyayacucho.data.LugarTuristicoFactory
import com.example.puriyayacucho.data.MuseoFactory
import com.example.puriyayacucho.guia.IconoCategoria
import com.example.puriyayacucho.guia.ModoViaje
import com.example.puriyayacucho.guia.Punto
import com.example.puriyayacucho.guia.UbicacionHelper
import com.example.puriyayacucho.guia.Waypoint
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

/**
 * Guía personalizada: el usuario elige a dónde ir y cómo moverse,
 * y la app arma el recorrido (RecorridoActivity).
 */
class GuiaActivity : BaseActivity() {

    companion object {
        const val EXTRA_DESTINO_NOMBRE = "destino_nombre"
        const val EXTRA_DESTINO_LAT = "destino_lat"
        const val EXTRA_DESTINO_LNG = "destino_lng"

        fun abrirConDestino(context: Context, nombre: String, lat: Double, lng: Double) {
            context.startActivity(Intent(context, GuiaActivity::class.java).apply {
                putExtra(EXTRA_DESTINO_NOMBRE, nombre)
                putExtra(EXTRA_DESTINO_LAT, lat)
                putExtra(EXTRA_DESTINO_LNG, lng)
            })
        }
    }

    private lateinit var tvOrigen: TextView
    private lateinit var actvDestino: AutoCompleteTextView
    private lateinit var toggleModo: MaterialButtonToggleGroup

    private var origen = Waypoint("", UbicacionHelper.PLAZA_MAYOR)
    private var destino: Waypoint? = null
    private var lugares: List<LugarIndexado> = emptyList()

    private val pedirPermisos = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            usarMiUbicacion()
        } else {
            Toast.makeText(this, R.string.location_denied, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guia)

        tvOrigen = findViewById(R.id.tvGuiaOrigen)
        actvDestino = findViewById(R.id.actvGuiaDestino)
        toggleModo = findViewById(R.id.toggleModo)

        origen = Waypoint(getString(R.string.guide_main_square), UbicacionHelper.PLAZA_MAYOR)
        tvOrigen.text = origen.nombre

        findViewById<android.view.View>(R.id.btnGuiaVolver).setOnClickListener { finish() }
        findViewById<android.view.View>(R.id.btnGuiaMiUbicacion).setOnClickListener {
            if (UbicacionHelper.tienePermiso(this)) usarMiUbicacion()
            else pedirPermisos.launch(UbicacionHelper.PERMISOS)
        }

        configurarDestinos()
        configurarIconosCercanos()
        configurarRutasPopulares()

        findViewById<android.view.View>(R.id.btnGenerarRecorrido).setOnClickListener { generarRecorrido() }
        findViewById<android.view.View>(R.id.btnMisRecorridos).setOnClickListener {
            startActivity(Intent(this, MisRecorridosActivity::class.java))
        }

        // Si ya hay permiso, usamos la ubicación real como punto de partida
        if (UbicacionHelper.tienePermiso(this)) usarMiUbicacion()
    }

    private fun configurarDestinos() {
        lugares = LugarMapper.todos(this).distinctBy { it.lugar.nombre }
        actvDestino.setAdapter(DestinoGuiaAdapter(this, lugares.map { OpcionDestino(it) }))
        actvDestino.setOnItemClickListener { parent, _, position, _ ->
            (parent.getItemAtPosition(position) as? OpcionDestino)?.lugar?.let { elegido ->
                destino = Waypoint(elegido.lugar.nombre, Punto(elegido.lugar.coordenadas.first, elegido.lugar.coordenadas.second))
                mostrarIconoDestino(elegido.categoria)
            }
        }
        // Si borra o cambia el texto, vuelve el ícono genérico
        actvDestino.doAfterTextChanged { texto ->
            if (texto?.toString() != destino?.nombre) mostrarIconoDestino(null)
        }

        // Destino recibido desde el detalle de un lugar ("CÓMO LLEGAR")
        intent.getStringExtra(EXTRA_DESTINO_NOMBRE)?.let { nombre ->
            val lat = intent.getDoubleExtra(EXTRA_DESTINO_LAT, 0.0)
            val lng = intent.getDoubleExtra(EXTRA_DESTINO_LNG, 0.0)
            destino = Waypoint(nombre, Punto(lat, lng))
            actvDestino.setText(nombre, false)
            mostrarIconoDestino(lugares.firstOrNull { it.lugar.nombre == nombre }?.categoria)
        }
    }

    /**
     * El campo "A dónde" muestra el ícono de lo que elegiste: cubiertos si es comida,
     * copa si es un bar, cama si es un hotel... Sin categoría, el pin de siempre.
     */
    private fun mostrarIconoDestino(categoria: CategoriaLugar?) {
        val campo = findViewById<TextInputLayout>(R.id.tilGuiaDestino)
        if (categoria == null) {
            campo.startIconDrawable = ContextCompat.getDrawable(this, R.drawable.ic_place)
            campo.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#FF3D5A")))
        } else {
            campo.startIconDrawable = ContextCompat.getDrawable(this, categoria.icono)
            campo.setStartIconTintList(ColorStateList.valueOf(Color.parseColor(categoria.colorHex)))
        }
    }

    /** Cada interruptor de "Mostrar cerca de la ruta" lleva el ícono de su categoría. */
    private fun configurarIconosCercanos() {
        val espacio = (14 * resources.displayMetrics.density).toInt()
        listOf(
            R.id.swCercaRestaurantes to CategoriaLugar.RESTAURANTE,
            R.id.swCercaHospedaje to CategoriaLugar.HOTEL,
            R.id.swCercaCultura to CategoriaLugar.TEMPLO,
            R.id.swCercaBares to CategoriaLugar.BAR
        ).forEach { (id, categoria) ->
            findViewById<SwitchMaterial>(id).apply {
                setCompoundDrawablesRelative(IconoCategoria.enCirculo(this@GuiaActivity, categoria), null, null, null)
                compoundDrawablePadding = espacio
            }
        }
    }

    private fun configurarRutasPopulares() {
        // Ruta de las iglesias (a pie): Catedral → La Merced → San Cristóbal
        findViewById<android.view.View>(R.id.cardRutaIglesias).setOnClickListener {
            val templos = LugarTuristicoFactory.obtenerLugaresTuristicosAyacucho(this)
            val paradas = templos.filter { it.id in listOf(1, 3, 2) }
                .sortedBy { listOf(1, 3, 2).indexOf(it.id) }
                .map { Waypoint(it.nombre, Punto(it.coordenadas.first, it.coordenadas.second)) }
            if (paradas.size >= 2) {
                abrirRecorrido(listOf(origen) + paradas, ModoViaje.A_PIE)
            }
        }

        // Wari y Quinua (transporte público)
        findViewById<android.view.View>(R.id.cardRutaWari).setOnClickListener {
            val wari = lugares.firstOrNull { it.categoria == CategoriaLugar.NATURALEZA && it.lugar.nombre.contains("Wari", true) }
            val quinua = MuseoFactory.obtenerMuseosAyacucho(this).firstOrNull { it.nombre.contains("Quinua", true) }
            val paradas = mutableListOf(origen)
            wari?.let { paradas += Waypoint(it.lugar.nombre, Punto(it.lugar.coordenadas.first, it.lugar.coordenadas.second)) }
            quinua?.let { paradas += Waypoint(it.nombre, Punto(it.coordenadas.first, it.coordenadas.second)) }
            if (paradas.size >= 2) abrirRecorrido(paradas, ModoViaje.TRANSPORTE)
        }
    }

    private fun usarMiUbicacion() {
        lifecycleScope.launch {
            val punto = UbicacionHelper.obtenerUbicacion(this@GuiaActivity)
            if (punto != null) {
                origen = Waypoint(getString(R.string.guide_my_location), punto)
                tvOrigen.text = origen.nombre
            } else {
                Toast.makeText(this@GuiaActivity, R.string.location_unavailable, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun modoSeleccionado(): ModoViaje = when (toggleModo.checkedButtonId) {
        R.id.btnModoPie -> ModoViaje.A_PIE
        R.id.btnModoTaxi -> ModoViaje.TAXI
        else -> ModoViaje.TRANSPORTE
    }

    private fun generarRecorrido() {
        // Si escribió el nombre sin elegirlo de la lista, lo buscamos igual
        val texto = actvDestino.text.toString().trim()
        if (destino?.nombre != texto) {
            val encontrado = lugares.firstOrNull { it.lugar.nombre.equals(texto, true) }
                ?: lugares.firstOrNull { texto.isNotEmpty() && it.lugar.nombre.contains(texto, true) }
            destino = encontrado?.let { Waypoint(it.lugar.nombre, Punto(it.lugar.coordenadas.first, it.lugar.coordenadas.second)) }
            encontrado?.let {
                actvDestino.setText(it.lugar.nombre, false)
                mostrarIconoDestino(it.categoria)
            }
        }

        val dest = destino
        if (dest == null) {
            findViewById<TextInputLayout>(R.id.tilGuiaDestino).error = getString(R.string.guide_choose_destination)
            return
        }
        findViewById<TextInputLayout>(R.id.tilGuiaDestino).error = null
        abrirRecorrido(listOf(origen, dest), modoSeleccionado())
    }

    private fun categoriasCercanas(): ArrayList<String> {
        val claves = ArrayList<String>()
        if (findViewById<SwitchMaterial>(R.id.swCercaRestaurantes).isChecked) claves += CategoriaLugar.RESTAURANTE.clave
        if (findViewById<SwitchMaterial>(R.id.swCercaHospedaje).isChecked) {
            claves += CategoriaLugar.HOTEL.clave
            claves += CategoriaLugar.AIRBNB.clave
        }
        if (findViewById<SwitchMaterial>(R.id.swCercaCultura).isChecked) {
            claves += CategoriaLugar.TEMPLO.clave
            claves += CategoriaLugar.MUSEO.clave
        }
        if (findViewById<SwitchMaterial>(R.id.swCercaBares).isChecked) claves += CategoriaLugar.BAR.clave
        return claves
    }

    private fun abrirRecorrido(waypoints: List<Waypoint>, modo: ModoViaje) {
        RecorridoActivity.abrir(this, waypoints, modo, categoriasCercanas())
    }
}
