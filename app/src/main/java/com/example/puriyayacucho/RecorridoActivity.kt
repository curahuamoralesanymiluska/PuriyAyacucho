package com.example.puriyayacucho

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puriyayacucho.adapters.CercanoUi
import com.example.puriyayacucho.adapters.ParadaCercanaAdapter
import com.example.puriyayacucho.adapters.PasoAdapter
import com.example.puriyayacucho.adapters.PasoUi
import com.example.puriyayacucho.data.AppDatabase
import com.example.puriyayacucho.data.CategoriaLugar
import com.example.puriyayacucho.data.LugarIndexado
import com.example.puriyayacucho.data.LugarMapper
import com.example.puriyayacucho.data.RecorridoEntity
import com.example.puriyayacucho.data.TransporteFactory
import com.example.puriyayacucho.guia.EstiloTramo
import com.example.puriyayacucho.guia.Geo
import com.example.puriyayacucho.guia.MarcadoresMapa
import com.example.puriyayacucho.guia.RutaGeometria
import com.example.puriyayacucho.guia.ModoViaje
import com.example.puriyayacucho.guia.Paso
import com.example.puriyayacucho.guia.PlanRuta
import com.example.puriyayacucho.guia.Punto
import com.example.puriyayacucho.guia.RutaPlanner
import com.example.puriyayacucho.guia.RutasCalles
import com.example.puriyayacucho.guia.TipoPaso
import com.example.puriyayacucho.guia.Waypoint
import com.example.puriyayacucho.guia.formatearDistancia
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.RoundCap
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Muestra el recorrido generado: mapa con la ruta, pasos (línea de tiempo),
 * tarifa estimada y lugares cerca de la ruta para agregarlos como paradas.
 */
class RecorridoActivity : BaseActivity() {

    companion object {
        private const val EXTRA_NOMBRES = "wp_nombres"
        private const val EXTRA_LATS = "wp_lats"
        private const val EXTRA_LNGS = "wp_lngs"
        private const val EXTRA_MODO = "modo"
        private const val EXTRA_CATEGORIAS = "categorias"
        private const val RADIO_CERCANOS_M = 500.0
        private const val RADIO_PARADEROS_M = 400.0

        fun abrir(context: Context, waypoints: List<Waypoint>, modo: ModoViaje, categorias: ArrayList<String>) {
            context.startActivity(Intent(context, RecorridoActivity::class.java).apply {
                putStringArrayListExtra(EXTRA_NOMBRES, ArrayList(waypoints.map { it.nombre }))
                putExtra(EXTRA_LATS, waypoints.map { it.punto.lat }.toDoubleArray())
                putExtra(EXTRA_LNGS, waypoints.map { it.punto.lng }.toDoubleArray())
                putExtra(EXTRA_MODO, modo.name)
                putStringArrayListExtra(EXTRA_CATEGORIAS, categorias)
            })
        }

        /** Convierte los puntos a JSON para guardarlos en Room. */
        fun waypointsAJson(waypoints: List<Waypoint>): String = JSONArray().apply {
            waypoints.forEach { put(JSONObject().put("n", it.nombre).put("lat", it.punto.lat).put("lng", it.punto.lng)) }
        }.toString()

        fun waypointsDesdeJson(json: String): List<Waypoint> {
            val arr = JSONArray(json)
            return (0 until arr.length()).map {
                val o = arr.getJSONObject(it)
                Waypoint(o.getString("n"), Punto(o.getDouble("lat"), o.getDouble("lng")))
            }
        }
    }

    private val planner = RutaPlanner(TransporteFactory.lineas, TransporteFactory.paraderos)
    private val pasoAdapter = PasoAdapter()
    private lateinit var cercanoAdapter: ParadaCercanaAdapter

    private var waypoints = mutableListOf<Waypoint>()
    private var modo = ModoViaje.TRANSPORTE
    private val categoriasActivas = mutableSetOf<CategoriaLugar>()
    private var plan: PlanRuta? = null
    private var mapa: GoogleMap? = null
    private var todosLosLugares: List<LugarIndexado> = emptyList()
    private var cercanos: List<CercanoUi> = emptyList()
    /** En "Para en el camino" los lugares cercanos llevan etiqueta con nombre; en "Pasos", solo el ícono. */
    private var mostrarCercanosEnMapa = false
    private val marcadoresCercanos = mutableListOf<Marker>()
    /** Trazo por calles de cada tramo (llega de internet unos segundos después). */
    private var trazosCalles: Map<Int, List<Punto>> = emptyMap()
    private var buscarCalles: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recorrido)

        if (!leerExtras()) {
            finish()
            return
        }
        todosLosLugares = LugarMapper.todos(this)

        findViewById<View>(R.id.btnRecorridoVolver).setOnClickListener { finish() }
        findViewById<View>(R.id.btnIniciarRecorrido).setOnClickListener { iniciarRecorrido() }

        findViewById<RecyclerView>(R.id.rvPasos).apply {
            layoutManager = LinearLayoutManager(this@RecorridoActivity)
            adapter = pasoAdapter
        }
        cercanoAdapter = ParadaCercanaAdapter { agregarParada(it) }
        findViewById<RecyclerView>(R.id.rvCercanos).apply {
            layoutManager = LinearLayoutManager(this@RecorridoActivity)
            adapter = cercanoAdapter
        }

        configurarPestanas()
        configurarChipsCercanos()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapRecorrido) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            mapa = googleMap
            googleMap.uiSettings.isZoomControlsEnabled = false
            googleMap.uiSettings.isMapToolbarEnabled = false
            runCatching {
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapa_estilo_puriy))
            }
            googleMap.setOnInfoWindowClickListener { marker ->
                (marker.tag as? LugarIndexado)?.let { DetalleNavigator.abrir(this, it.lugar) }
            }
            dibujarMapa()
        }

        recalcular()
    }

    private fun leerExtras(): Boolean {
        val nombres = intent.getStringArrayListExtra(EXTRA_NOMBRES) ?: return false
        val lats = intent.getDoubleArrayExtra(EXTRA_LATS) ?: return false
        val lngs = intent.getDoubleArrayExtra(EXTRA_LNGS) ?: return false
        if (nombres.size < 2 || lats.size != nombres.size || lngs.size != nombres.size) return false
        waypoints = nombres.indices.map { Waypoint(nombres[it], Punto(lats[it], lngs[it])) }.toMutableList()
        modo = runCatching { ModoViaje.valueOf(intent.getStringExtra(EXTRA_MODO) ?: "") }.getOrDefault(ModoViaje.TRANSPORTE)
        categoriasActivas.clear()
        categoriasActivas.addAll(
            (intent.getStringArrayListExtra(EXTRA_CATEGORIAS) ?: arrayListOf()).mapNotNull { CategoriaLugar.desdeClave(it) }
        )
        return true
    }

    private fun configurarPestanas() {
        val tabs = findViewById<TabLayout>(R.id.tabsRecorrido)
        tabs.addTab(tabs.newTab().setText(R.string.route_tab_steps))
        tabs.addTab(tabs.newTab().setText(R.string.route_tab_nearby))
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val verPasos = (tab?.position ?: 0) == 0
                findViewById<View>(R.id.rvPasos).visibility = if (verPasos) View.VISIBLE else View.GONE
                findViewById<View>(R.id.layoutCercanos).visibility = if (verPasos) View.GONE else View.VISIBLE
                mostrarCercanosEnMapa = !verPasos
                dibujarCercanosEnMapa()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    /** Chips para filtrar los lugares cercanos (Restaurantes, Hoteles, Airbnb, Bares...). */
    private fun configurarChipsCercanos() {
        val grupo = findViewById<ChipGroup>(R.id.chipGroupCercanos)
        val opciones = listOf(
            CategoriaLugar.RESTAURANTE, CategoriaLugar.HOTEL, CategoriaLugar.AIRBNB,
            CategoriaLugar.BAR, CategoriaLugar.TEMPLO, CategoriaLugar.MUSEO
        )
        if (categoriasActivas.isEmpty()) categoriasActivas.addAll(listOf(CategoriaLugar.RESTAURANTE, CategoriaLugar.HOTEL))
        opciones.forEach { cat ->
            val color = Color.parseColor(cat.colorHex)
            val chip = Chip(this).apply {
                text = getString(cat.etiqueta)
                isCheckable = true
                isChecked = cat in categoriasActivas
                isCheckedIconVisible = false
                chipIcon = ContextCompat.getDrawable(this@RecorridoActivity, cat.icono)
                isChipIconVisible = true
                chipIconSize = resources.displayMetrics.density * 16
                chipIconTint = ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
                    intArrayOf(Color.WHITE, color)
                )
                chipBackgroundColor = ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
                    intArrayOf(color, Color.parseColor("#ECECF1"))
                )
                setTextColor(
                    ColorStateList(
                        arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
                        intArrayOf(Color.WHITE, Color.parseColor("#666666"))
                    )
                )
                setOnCheckedChangeListener { _, marcado ->
                    if (marcado) categoriasActivas.add(cat) else categoriasActivas.remove(cat)
                    actualizarCercanos()
                    dibujarCercanosEnMapa()
                }
            }
            grupo.addView(chip)
        }
    }

    /** Vuelve a calcular la ruta (al abrir la pantalla o al agregar una parada). */
    private fun recalcular() {
        val nuevoPlan = planner.planificar(waypoints, modo)
        plan = nuevoPlan

        findViewById<TextView>(R.id.tvRecorridoDestino).text = waypoints.last().nombre
        findViewById<TextView>(R.id.tvRecorridoTarifa).text =
            if (nuevoPlan.tarifaTotal == 0.0) getString(R.string.route_free) else formatearSoles(nuevoPlan.tarifaTotal)
        findViewById<TextView>(R.id.tvRecorridoTiempo).text = formatearTiempo(nuevoPlan.minutosTotales)
        findViewById<View>(R.id.tvRecorridoAviso).visibility =
            if (nuevoPlan.sinTransporteDisponible) View.VISIBLE else View.GONE

        pasoAdapter.submitList(nuevoPlan.pasos.map { pasoAUi(it) })
        trazosCalles = emptyMap()
        actualizarCercanos()
        dibujarMapa()

        // Mientras tanto se ve la ruta aproximada; cuando llega el trazo por calles, se redibuja
        buscarCalles?.cancel()
        buscarCalles = lifecycleScope.launch {
            val trazos = RutasCalles.trazos(nuevoPlan)
            if (trazos.isNotEmpty() && plan === nuevoPlan) {
                trazosCalles = trazos
                actualizarCercanos()
                dibujarMapa()
            }
        }
    }

    private fun pasoAUi(paso: Paso): PasoUi = when (paso.tipo) {
        TipoPaso.CAMINAR -> PasoUi(
            R.drawable.ic_walk, MarcadoresMapa.COLOR_CAMINAR,
            getString(R.string.step_walk_to, paso.destinoNombre),
            "${formatearTiempo(paso.minutos)} · ${formatearDistancia(paso.metros)}"
        )
        TipoPaso.TRANSPORTE -> PasoUi(
            R.drawable.ic_bus, MarcadoresMapa.COLOR_COMBI,
            getString(R.string.step_take_line, paso.linea?.nombre ?: ""),
            getString(
                R.string.step_line_detail,
                paso.paraderoSubida?.nombre ?: "",
                paso.paraderoBajada?.nombre ?: "",
                formatearTiempo(paso.minutos),
                formatearSoles(paso.linea?.tarifa ?: 0.0)
            ) + (paso.linea?.nota?.takeIf { it.isNotBlank() }?.let { "\n$it" } ?: "")
        )
        TipoPaso.TAXI -> PasoUi(
            R.drawable.ic_taxi, MarcadoresMapa.COLOR_TAXI,
            getString(R.string.step_taxi_to, paso.destinoNombre),
            "${formatearTiempo(paso.minutos)} · ${formatearDistancia(paso.metros)} · ${getString(R.string.step_approx)} ${formatearSoles(planner.tarifaTaxi(paso.metros))}"
        )
        TipoPaso.PARADA -> PasoUi(
            iconoLugar(paso.destinoNombre, R.drawable.ic_place), colorLugar(paso.destinoNombre, "#7B4DFF"),
            getString(R.string.step_stop, paso.destinoNombre),
            getString(R.string.step_stop_detail)
        )
        TipoPaso.LLEGADA -> PasoUi(
            iconoLugar(paso.destinoNombre, R.drawable.ic_bandera), colorLugar(paso.destinoNombre, MarcadoresMapa.COLOR_MARCA),
            getString(R.string.step_arrive, paso.destinoNombre),
            getString(R.string.step_arrive_detail)
        )
    }

    /** Lugares de las categorías elegidas a 500 m o menos de cualquier punto de la ruta. */
    private fun actualizarCercanos() {
        val planActual = plan ?: return
        val puntosRuta = planActual.tramos.indices.flatMap { i -> trazosCalles[i] ?: planActual.tramos[i].puntos }
        val rutaDensa = Geo.densificar(puntosRuta)
        val nombresEnRuta = waypoints.map { it.nombre }.toSet()
        cercanos = todosLosLugares
            .filter { it.categoria in categoriasActivas }
            .map { li ->
                val p = Punto(li.lugar.coordenadas.first, li.lugar.coordenadas.second)
                CercanoUi(li, Geo.distanciaMinimaARuta(p, rutaDensa), li.lugar.nombre in nombresEnRuta)
            }
            .filter { it.distanciaMetros <= RADIO_CERCANOS_M }
            .sortedBy { it.distanciaMetros }
            .take(30)

        cercanoAdapter.submitList(cercanos)
        findViewById<View>(R.id.tvSinCercanos).visibility = if (cercanos.isEmpty()) View.VISIBLE else View.GONE
    }

    /** Agrega un lugar como parada antes del destino final y recalcula. */
    private fun agregarParada(cercano: CercanoUi) {
        val lugar = cercano.item.lugar
        if (waypoints.any { it.nombre == lugar.nombre }) return
        val nueva = Waypoint(lugar.nombre, Punto(lugar.coordenadas.first, lugar.coordenadas.second))

        // Insertar la parada en el tramo donde queda más cerca
        val indice = (1 until waypoints.size).minByOrNull { i ->
            val tramo = Geo.densificar(listOf(waypoints[i - 1].punto, waypoints[i].punto))
            Geo.distanciaMinimaARuta(nueva.punto, tramo)
        } ?: (waypoints.size - 1)
        waypoints.add(indice, nueva)

        Toast.makeText(this, getString(R.string.route_stop_added, lugar.nombre), Toast.LENGTH_SHORT).show()
        recalcular()
    }

    private fun dibujarMapa() {
        val map = mapa ?: return
        val planActual = plan ?: return
        map.clear()
        marcadoresCercanos.clear()

        val limites = LatLngBounds.Builder()
        // Cada tramo corresponde, en orden, a un paso de movimiento (caminar, combi o taxi)
        val movimientos = planActual.pasos.filter {
            it.tipo == TipoPaso.CAMINAR || it.tipo == TipoPaso.TRANSPORTE || it.tipo == TipoPaso.TAXI
        }
        planActual.tramos.forEachIndexed { indice, tramo ->
            val puntos = (trazosCalles[indice] ?: RutaGeometria.trazo(tramo)).map { LatLng(it.lat, it.lng) }
            puntos.forEach { limites.include(it) }
            val esCaminata = tramo.estilo == EstiloTramo.CAMINATA
            val color = when {
                esCaminata -> MarcadoresMapa.COLOR_CAMINAR
                movimientos.getOrNull(indice)?.tipo == TipoPaso.TAXI -> MarcadoresMapa.COLOR_TAXI
                else -> MarcadoresMapa.COLOR_COMBI
            }
            if (esCaminata) {
                // A pie: puntitos azules
                map.addPolyline(
                    PolylineOptions().addAll(puntos).width(14f).color(Color.parseColor(color))
                        .pattern(listOf(Dot(), Gap(18f))).zIndex(2f)
                )
            } else {
                // En vehículo: línea con borde blanco, más fácil de seguir
                map.addPolyline(
                    PolylineOptions().addAll(puntos).width(22f).color(Color.WHITE)
                        .startCap(RoundCap()).endCap(RoundCap()).jointType(JointType.ROUND).zIndex(1f)
                )
                map.addPolyline(
                    PolylineOptions().addAll(puntos).width(13f).color(Color.parseColor(color))
                        .startCap(RoundCap()).endCap(RoundCap()).jointType(JointType.ROUND).zIndex(2f)
                )
            }
        }
        actualizarLeyenda(planActual)
        findViewById<View>(R.id.tvCreditoCalles)?.visibility = if (trazosCalles.isEmpty()) View.GONE else View.VISIBLE

        // Origen ("Tú"), paradas y destino con el ícono de su categoría
        waypoints.forEachIndexed { i, wp ->
            val categoria = MarcadoresMapa.categoriaDe(todosLosLugares, wp.nombre)
            val icono = when {
                i == 0 -> MarcadoresMapa.puntoTu(this)
                categoria != null -> MarcadoresMapa.pinCategoria(this, categoria, grande = i == waypoints.lastIndex)
                i == waypoints.lastIndex -> MarcadoresMapa.pinDestino(this)
                else -> MarcadoresMapa.pin(this, R.drawable.ic_place, Color.parseColor("#7B4DFF"), 36f)
            }
            val marcador = map.addMarker(
                MarkerOptions().position(LatLng(wp.punto.lat, wp.punto.lng)).title(wp.nombre)
                    .icon(icono)
                    .anchor(0.5f, if (i == 0) 0.5f else 1f)
                    .zIndex(if (i == waypoints.lastIndex) 5f else 4f)
            )
            // El nombre del destino se ve sin tener que tocar el pin
            if (i == waypoints.lastIndex) marcador?.showInfoWindow()
        }

        // Paraderos donde sube y baja: etiqueta naranja con el bus y el nombre
        val usados = mutableSetOf<Int>()
        planActual.pasos.filter { it.tipo == TipoPaso.TRANSPORTE }.forEach { paso ->
            listOfNotNull(
                paso.paraderoSubida?.let { it to R.string.map_board_here },
                paso.paraderoBajada?.let { it to R.string.map_get_off_here }
            ).forEach { (par, texto) ->
                usados += par.id
                map.addMarker(
                    MarkerOptions().position(LatLng(par.punto.lat, par.punto.lng)).title(par.nombre)
                        .snippet(paso.linea?.nombre)
                        .icon(MarcadoresMapa.etiqueta(this, getString(texto, par.nombre), R.drawable.ic_bus, Color.parseColor(MarcadoresMapa.COLOR_COMBI)))
                        .anchor(0.5f, 1f)
                        .zIndex(6f)
                )
            }
        }
        // Otros paraderos cerca de la ruta (cuadrito con bus)
        val rutaDensa = Geo.densificar(planActual.tramos.indices.flatMap { i -> trazosCalles[i] ?: planActual.tramos[i].puntos })
        TransporteFactory.paraderos
            .filter { it.id !in usados && Geo.distanciaMinimaARuta(it.punto, rutaDensa) <= RADIO_PARADEROS_M }
            .take(8)
            .forEach { par ->
                val lineas = TransporteFactory.lineas.filter { it.id in par.lineas }.joinToString { it.nombre }
                map.addMarker(
                    MarkerOptions().position(LatLng(par.punto.lat, par.punto.lng)).title(par.nombre)
                        .snippet(lineas)
                        .icon(MarcadoresMapa.paradero(this))
                        .anchor(0.5f, 0.5f)
                        .zIndex(3f)
                )
            }

        dibujarCercanosEnMapa()

        val vista = findViewById<View>(R.id.mapRecorrido)
        vista.post {
            runCatching {
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(limites.build(), 140))
            }
        }
    }

    /**
     * Lugares cercanos a la ruta con el ícono de su categoría (cubiertos, copa, cama...).
     * En "Pasos" se ven como pines pequeños; en "Para en el camino", con etiqueta y nombre.
     */
    private fun dibujarCercanosEnMapa() {
        val map = mapa ?: return
        marcadoresCercanos.forEach { it.remove() }
        marcadoresCercanos.clear()
        cercanos.filterNot { it.agregado }.forEach { c ->
            val categoria = c.item.categoria
            val icono = if (mostrarCercanosEnMapa) {
                MarcadoresMapa.etiqueta(this, c.item.lugar.nombre, categoria.icono, Color.parseColor(categoria.colorHex))
            } else {
                MarcadoresMapa.pinPequeno(this, categoria)
            }
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(c.item.lugar.coordenadas.first, c.item.lugar.coordenadas.second))
                    .title(c.item.lugar.nombre)
                    .snippet(getString(R.string.route_tap_for_details))
                    .icon(icono)
                    .anchor(0.5f, 1f)
                    .zIndex(if (mostrarCercanosEnMapa) 4.5f else 2.5f)
            )?.let { marker ->
                marker.tag = c.item
                marcadoresCercanos.add(marker)
            }
        }
    }

    /** Muestra en la leyenda solo los tipos de línea que tiene este recorrido. */
    private fun actualizarLeyenda(planActual: PlanRuta) {
        val tipos = planActual.pasos.map { it.tipo }.toSet()
        findViewById<View>(R.id.tvLeyendaCaminar)?.visibility = if (TipoPaso.CAMINAR in tipos) View.VISIBLE else View.GONE
        findViewById<View>(R.id.tvLeyendaCombi)?.visibility = if (TipoPaso.TRANSPORTE in tipos) View.VISIBLE else View.GONE
        findViewById<View>(R.id.tvLeyendaTaxi)?.visibility = if (TipoPaso.TAXI in tipos) View.VISIBLE else View.GONE
    }

    @androidx.annotation.DrawableRes
    private fun iconoLugar(nombre: String, @androidx.annotation.DrawableRes porDefecto: Int): Int =
        MarcadoresMapa.categoriaDe(todosLosLugares, nombre)?.icono ?: porDefecto

    private fun colorLugar(nombre: String, porDefecto: String): String =
        MarcadoresMapa.categoriaDe(todosLosLugares, nombre)?.colorHex ?: porDefecto

    private fun iniciarRecorrido() {
        if (plan == null) return

        // Guardar el recorrido para verlo luego en "Mis recorridos"
        val correo = getSharedPreferences("usuario_sesion", MODE_PRIVATE).getString("correo_usuario", "") ?: ""
        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        lifecycleScope.launch {
            AppDatabase.getInstance(this@RecorridoActivity).recorridoDao().insertar(
                RecorridoEntity(
                    usuarioCorreo = correo,
                    titulo = waypoints.last().nombre,
                    modo = modo.name,
                    waypointsJson = waypointsAJson(waypoints),
                    fecha = fecha
                )
            )
        }

        // El viaje se sigue dentro de la app: una personita avanza por la ruta
        ViajeActivity.abrir(this, waypoints, modo, categoriasActivas.map { it.clave })
    }

    private fun formatearSoles(soles: Double) = String.format(Locale.US, "S/ %.2f", soles)

    private fun formatearTiempo(min: Int): String =
        if (min < 60) getString(R.string.time_minutes, min)
        else getString(R.string.time_hours_minutes, min / 60, min % 60)
}
