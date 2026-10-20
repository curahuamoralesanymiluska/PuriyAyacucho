package com.example.puriyayacucho

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.provider.Settings
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.lifecycleScope
import com.example.puriyayacucho.data.CategoriaLugar
import com.example.puriyayacucho.data.LugarIndexado
import com.example.puriyayacucho.data.LugarMapper
import com.example.puriyayacucho.data.TransporteFactory
import com.example.puriyayacucho.guia.ConfetiView
import com.example.puriyayacucho.guia.EstiloTramo
import com.example.puriyayacucho.guia.Geo
import com.example.puriyayacucho.guia.MarcadoresMapa
import com.example.puriyayacucho.guia.ModoViaje
import com.example.puriyayacucho.guia.Paso
import com.example.puriyayacucho.guia.PlanRuta
import com.example.puriyayacucho.guia.ProgresoViajeView
import com.example.puriyayacucho.guia.Punto
import com.example.puriyayacucho.guia.Recorrido
import com.example.puriyayacucho.guia.RutaPlanner
import com.example.puriyayacucho.guia.RutasCalles
import com.example.puriyayacucho.guia.TipoPaso
import com.example.puriyayacucho.guia.UbicacionHelper
import com.example.puriyayacucho.guia.Viaje
import com.example.puriyayacucho.guia.Waypoint
import com.example.puriyayacucho.guia.formatearDistancia
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * El viaje dentro de la app, **en vivo con el GPS del celular**: la personita (o la combi o
 * el taxi) va donde tú vas. Si caminas, camina; si te detienes, se queda quieta; al llegar
 * salta con confeti. No hay simulación: lo que se ve en el mapa es tu movimiento real.
 *
 * - Al abrir pide el permiso de ubicación y, si el GPS está apagado, el diálogo del sistema
 *   para encenderlo.
 * - Si la primera ubicación está lejos del inicio de la ruta (por ejemplo, la ruta salía de la
 *   Plaza Mayor y tú estás en otro lugar), la ruta se vuelve a calcular desde donde estás.
 * - Cerca de la ruta la personita va pegada a la línea; si te alejas, se muestra donde estás
 *   y un aviso para volver.
 */
class ViajeActivity : BaseActivity() {

    companion object {
        private const val EXTRA_NOMBRES = "viaje_nombres"
        private const val EXTRA_LATS = "viaje_lats"
        private const val EXTRA_LNGS = "viaje_lngs"
        private const val EXTRA_MODO = "viaje_modo"
        private const val EXTRA_CATEGORIAS = "viaje_categorias"
        private const val RADIO_CERCANOS_M = 300.0

        /** A esta distancia del destino cuenta como "llegaste". */
        private const val METROS_LLEGADA = 40.0
        /** Hasta esta distancia la personita se dibuja sobre la línea de la ruta. */
        private const val METROS_PEGAR_A_RUTA = 40.0
        /** Más lejos que esto, el avance se detiene y se avisa que te saliste de la ruta. */
        private const val METROS_FUERA_DE_RUTA = 150.0
        /** Si la primera ubicación está más lejos que esto del inicio, se recalcula la ruta. */
        private const val METROS_REPLANIFICAR = 200.0
        /** Solo se recalcula si el destino está a menos de esto (para no planear desde otra ciudad). */
        private const val METROS_MAX_REPLANIFICAR = 150_000.0
        /** Ubicaciones con un error mayor que este no mueven a la personita. */
        private const val PRECISION_MAXIMA_M = 80f
        /** Velocidad desde la que se considera que te estás moviendo (≈ 2.5 km/h). */
        private const val VELOCIDAD_MOVIMIENTO = 0.7
        private const val MS_SIN_MOVERSE = 3_500L
        private const val MS_SIN_SENAL = 15_000L

        private const val VERDE = "#12B886"
        private const val AMBAR = "#E69A00"
        private const val GRIS = "#A5A5B4"
        private const val ROJO = "#FF3355"

        /** [categorias]: claves de [CategoriaLugar] que se muestran cerca de la ruta (comida, bares...). */
        fun abrir(context: Context, waypoints: List<Waypoint>, modo: ModoViaje, categorias: List<String> = emptyList()) {
            context.startActivity(Intent(context, ViajeActivity::class.java).apply {
                putStringArrayListExtra(EXTRA_NOMBRES, ArrayList(waypoints.map { it.nombre }))
                putExtra(EXTRA_LATS, waypoints.map { it.punto.lat }.toDoubleArray())
                putExtra(EXTRA_LNGS, waypoints.map { it.punto.lng }.toDoubleArray())
                putExtra(EXTRA_MODO, modo.name)
                putStringArrayListExtra(EXTRA_CATEGORIAS, ArrayList(categorias))
            })
        }
    }

    /** Lo que le falta al viaje para poder seguirte. */
    private enum class Aviso { NINGUNO, SIN_PERMISO, GPS_APAGADO }

    private val planner = RutaPlanner(TransporteFactory.lineas, TransporteFactory.paraderos)
    private lateinit var waypoints: List<Waypoint>
    private lateinit var modo: ModoViaje
    private lateinit var plan: PlanRuta
    private lateinit var viaje: Viaje
    private var todosLosLugares: List<LugarIndexado> = emptyList()
    /** Nombre de la parada intermedia que hay al terminar cada tramo (si hay). */
    private val paradaTrasSegmento = mutableMapOf<Int, String>()

    private lateinit var progresoView: ProgresoViajeView
    private lateinit var tvRestante: TextView
    private lateinit var tvDistancia: TextView
    private lateinit var tvPorcentaje: TextView
    private lateinit var tvEstadoGps: TextView
    private lateinit var btnActivar: MaterialButton
    private lateinit var btnCentrar: View

    private var mapa: GoogleMap? = null
    private var avatar: Marker? = null
    private var rastro: Polyline? = null
    private var trazoCalles: Job? = null

    private var minutoActual = 0.0
    private var indiceActual = -1
    private var llego = false
    private var seguirCamara = true
    private var aviso = Aviso.NINGUNO

    // GPS
    private var siguiendo = false
    private var permisoPedido = false
    private var gpsRechazado = false
    private var yaReplanifico = false
    private var ultimaUbicacion: Punto? = null
    private var ultimaUbicacionMs = 0L
    /** Última vez que llegó una señal del GPS (aunque sea imprecisa). */
    private var ultimaSenalMs = 0L
    private var ultimoMovimientoMs = 0L
    private var distanciaARuta = 0.0
    /** Dónde se dibuja la personita ahora (sobre la ruta o donde estás). */
    private var posicionMostrada: Punto? = null
    private var pegadaARuta = true
    private var animadorPosicion: ValueAnimator? = null

    // Animación de la personita en el mapa (cambia de cuadro unas 9 veces por segundo)
    private val handler = Handler(Looper.getMainLooper())
    private var cuadro = 0
    private var haciaIzquierda = false
    private var medioActual = ProgresoViajeView.Medio.A_PIE
    private var barraEnMovimiento = false
    private val animarPersonita = object : Runnable {
        override fun run() {
            val seMueve = seEstaMoviendo()
            if (seMueve || llego) {
                cuadro = (cuadro + 1) % MarcadoresMapa.CUADROS_PERSONITA
                actualizarIconoPersonita()
            } else if (cuadro != 0) {
                // Quieta: de pie, sin dar pasos
                cuadro = 0
                actualizarIconoPersonita()
            }
            val barra = seMueve && !llego
            if (barra != barraEnMovimiento) {
                barraEnMovimiento = barra
                progresoView.enMovimiento = barra
            }
            handler.postDelayed(this, 110)
        }
    }
    private val vigilarSenal = object : Runnable {
        override fun run() {
            if (siguiendo && !llego && ultimaSenalMs > 0 &&
                SystemClock.elapsedRealtime() - ultimaSenalMs > MS_SIN_SENAL
            ) {
                mostrarEstadoGps(getString(R.string.trip_gps_lost), ROJO)
            }
            handler.postDelayed(this, 5_000)
        }
    }

    private val clienteUbicacion by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val pedidoUbicacion: LocationRequest by lazy {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2_000L)
            .setMinUpdateIntervalMillis(1_000L)
            .build()
    }
    private val callbackUbicacion = object : LocationCallback() {
        override fun onLocationResult(resultado: LocationResult) {
            resultado.lastLocation?.let { alRecibirUbicacion(it) }
        }
    }
    private val pedirUbicacion = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) comprobarGpsYSeguir()
        else mostrarAviso(Aviso.SIN_PERMISO)
    }
    private val encenderGps = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { resultado ->
        if (resultado.resultCode == RESULT_OK) {
            empezarASeguir()
        } else {
            gpsRechazado = true
            mostrarAviso(Aviso.GPS_APAGADO)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viaje)
        // La pantalla no se apaga mientras viajas (y mientras lo muestras en público)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        waypoints = leerWaypoints() ?: run {
            finish()
            return
        }
        modo = runCatching { ModoViaje.valueOf(intent.getStringExtra(EXTRA_MODO) ?: "") }
            .getOrDefault(ModoViaje.TRANSPORTE)
        plan = planner.planificar(waypoints, modo)
        viaje = Viaje(plan)
        todosLosLugares = LugarMapper.todos(this)

        progresoView = findViewById(R.id.progresoViaje)
        tvRestante = findViewById(R.id.tvViajeRestante)
        tvDistancia = findViewById(R.id.tvViajeDistancia)
        tvPorcentaje = findViewById(R.id.tvViajePorcentaje)
        tvEstadoGps = findViewById(R.id.tvViajeModo)
        btnActivar = findViewById(R.id.btnViajeGps)
        btnCentrar = findViewById(R.id.btnViajeCentrar)

        findViewById<View>(R.id.btnViajeCerrar).setOnClickListener { confirmarTerminar() }
        findViewById<View>(R.id.btnViajeTerminar).setOnClickListener { confirmarTerminar() }
        btnActivar.setOnClickListener { alTocarActivar() }
        btnCentrar.setOnClickListener {
            seguirCamara = true
            btnCentrar.visibility = View.GONE
            posicionMostrada?.let { moverCamara(it, animado = true) }
        }
        findViewById<View>(R.id.btnLlegadaTerminar).setOnClickListener { finish() }

        configurarBarra()
        aplicarMinuto(0.0, null)
        mostrarEstadoGps(getString(R.string.trip_gps_searching), GRIS)
        mostrarDetalle(getString(R.string.trip_waiting_gps))
        buscarTrazoPorCalles()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapViaje) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            mapa = googleMap
            googleMap.uiSettings.isMapToolbarEnabled = false
            googleMap.uiSettings.isCompassEnabled = false
            runCatching { googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapa_estilo_puriy)) }
            googleMap.setOnCameraMoveStartedListener { motivo ->
                if (motivo == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE && !llego) {
                    seguirCamara = false
                    btnCentrar.visibility = View.VISIBLE
                }
            }
            ajustarRellenoMapa(googleMap)
            dibujarRuta(googleMap)
            val inicio = posicionMostrada ?: viaje.estadoEn(0.0).punto
            googleMap.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(LatLng(inicio.lat, inicio.lng)).zoom(16.5f).tilt(40f).build()
                )
            )
            aplicarMinuto(minutoActual, if (pegadaARuta) null else posicionMostrada)
        }

        // Sin permiso, se pide al entrar; con permiso, onStart empieza a seguirte
        if (!UbicacionHelper.tienePermiso(this)) {
            permisoPedido = true
            if (savedInstanceState == null) pedirUbicacion.launch(UbicacionHelper.PERMISOS)
            else mostrarAviso(Aviso.SIN_PERMISO)
        }
    }

    private fun leerWaypoints(): List<Waypoint>? {
        val nombres = intent.getStringArrayListExtra(EXTRA_NOMBRES) ?: return null
        val lats = intent.getDoubleArrayExtra(EXTRA_LATS) ?: return null
        val lngs = intent.getDoubleArrayExtra(EXTRA_LNGS) ?: return null
        if (nombres.size < 2 || lats.size != nombres.size || lngs.size != nombres.size) return null
        return nombres.indices.map { Waypoint(nombres[it], Punto(lats[it], lngs[it])) }
    }

    // ---------- Barra de progreso con la personita ----------

    private fun configurarBarra() {
        paradaTrasSegmento.clear()
        val total = viaje.totalMin.coerceAtLeast(1.0)
        progresoView.tramos = viaje.segmentos.map { seg ->
            ProgresoViajeView.TramoBarra(
                (seg.inicioMin / total).toFloat(), (seg.finMin / total).toFloat(), colorDePaso(seg.paso), medioDe(seg.paso, seg.estilo)
            )
        }

        // Hitos: "Tú" al inicio, paraderos, paradas y el destino con su ícono de categoría
        val hitos = mutableListOf(
            ProgresoViajeView.Hito(0f, R.drawable.ic_persona, Color.parseColor(MarcadoresMapa.COLOR_PUNTO_TU))
        )
        var segmento = -1
        plan.pasos.forEachIndexed { i, paso ->
            if (esMovimiento(paso)) {
                segmento++
                // Entre dos movimientos seguidos hay un paradero (subir o bajar de la combi)
                if (plan.pasos.getOrNull(i + 1)?.let { esMovimiento(it) } == true) {
                    hitos += ProgresoViajeView.Hito(
                        finDeSegmento(segmento, total), R.drawable.ic_bus, Color.parseColor(MarcadoresMapa.COLOR_COMBI)
                    )
                }
            } else {
                val categoria = MarcadoresMapa.categoriaDe(todosLosLugares, paso.destinoNombre)
                val icono = categoria?.icono ?: if (paso.tipo == TipoPaso.LLEGADA) R.drawable.ic_bandera else R.drawable.ic_place
                val color = categoria?.let { Color.parseColor(it.colorHex) } ?: Color.parseColor(MarcadoresMapa.COLOR_MARCA)
                hitos += ProgresoViajeView.Hito(finDeSegmento(segmento, total), icono, color)
                if (paso.tipo == TipoPaso.PARADA) paradaTrasSegmento[segmento] = paso.destinoNombre
            }
        }
        progresoView.hitos = hitos
    }

    private fun finDeSegmento(indice: Int, total: Double): Float =
        ((viaje.segmentos.getOrNull(indice)?.finMin ?: 0.0) / total).toFloat()

    // ---------- Mapa ----------

    private fun ajustarRellenoMapa(googleMap: GoogleMap) {
        val arriba = findViewById<View>(R.id.bloqueSuperiorViaje)
        val panel = findViewById<View>(R.id.panelViaje)
        panel.post { googleMap.setPadding(0, arriba.height, 0, panel.height) }
    }

    private fun dibujarRuta(map: GoogleMap) {
        // La ruta completa, suave; encima se dibuja el "rastro" de lo ya recorrido
        viaje.segmentos.forEach { seg ->
            val puntos = seg.recorrido.puntos.map { LatLng(it.lat, it.lng) }
            val color = colorDePaso(seg.paso)
            if (seg.estilo == EstiloTramo.CAMINATA) {
                map.addPolyline(
                    PolylineOptions().addAll(puntos).width(14f).color(color)
                        .pattern(listOf(Dot(), Gap(18f))).zIndex(1f)
                )
            } else {
                map.addPolyline(
                    PolylineOptions().addAll(puntos).width(22f).color(Color.WHITE)
                        .startCap(RoundCap()).endCap(RoundCap()).jointType(JointType.ROUND).zIndex(1f)
                )
                map.addPolyline(
                    PolylineOptions().addAll(puntos).width(13f).color(conAlfa(color, 140))
                        .startCap(RoundCap()).endCap(RoundCap()).jointType(JointType.ROUND).zIndex(2f)
                )
            }
        }
        rastro = map.addPolyline(
            PolylineOptions().width(13f).color(Color.parseColor(MarcadoresMapa.COLOR_MARCA))
                .startCap(RoundCap()).endCap(RoundCap()).jointType(JointType.ROUND).zIndex(3f)
        )

        // Paradas y destino con el ícono de su categoría
        waypoints.drop(1).forEachIndexed { i, wp ->
            val esDestino = i == waypoints.size - 2
            val categoria = MarcadoresMapa.categoriaDe(todosLosLugares, wp.nombre)
            val icono = when {
                categoria != null -> MarcadoresMapa.pinCategoria(this, categoria, grande = esDestino)
                esDestino -> MarcadoresMapa.pinDestino(this)
                else -> MarcadoresMapa.pin(this, R.drawable.ic_place, Color.parseColor("#7B4DFF"), 36f)
            }
            map.addMarker(
                MarkerOptions().position(LatLng(wp.punto.lat, wp.punto.lng)).title(wp.nombre)
                    .icon(icono).anchor(0.5f, 1f).zIndex(4f)
            )
        }
        // Paraderos donde sube y baja, con etiqueta
        plan.pasos.filter { it.tipo == TipoPaso.TRANSPORTE }.forEach { paso ->
            listOfNotNull(
                paso.paraderoSubida?.let { it to R.string.map_board_here },
                paso.paraderoBajada?.let { it to R.string.map_get_off_here }
            ).forEach { (par, texto) ->
                map.addMarker(
                    MarkerOptions().position(LatLng(par.punto.lat, par.punto.lng)).title(par.nombre)
                        .icon(MarcadoresMapa.etiqueta(this, getString(texto, par.nombre), R.drawable.ic_bus, Color.parseColor(MarcadoresMapa.COLOR_COMBI)))
                        .anchor(0.5f, 1f).zIndex(5f)
                )
            }
        }
        dibujarCercanos(map)

        val inicio = posicionMostrada ?: viaje.estadoEn(minutoActual).punto
        avatar = map.addMarker(
            MarkerOptions().position(LatLng(inicio.lat, inicio.lng))
                .icon(MarcadoresMapa.personita(this, medioActual, cuadro, haciaIzquierda, saltando = llego))
                .anchor(MarcadoresMapa.ANCLA_PERSONITA_X, MarcadoresMapa.ANCLA_PERSONITA_Y)
                // Semitransparente hasta que llegue tu primera ubicación
                .alpha(if (ultimaUbicacion == null && !llego) 0.45f else 1f)
                .zIndex(10f)
        )
    }

    private fun actualizarIconoPersonita() {
        // Los íconos del mapa solo se pueden crear cuando el mapa ya está listo
        val marcador = avatar ?: return
        marcador.setIcon(MarcadoresMapa.personita(this, medioActual, cuadro, haciaIzquierda, saltando = llego))
    }

    /** La ruta se ve al instante; si llega el trazo por calles, se redibuja siguiendo las calles. */
    private fun buscarTrazoPorCalles() {
        trazoCalles?.cancel()
        val planPedido = plan
        trazoCalles = lifecycleScope.launch {
            val trazos = RutasCalles.trazos(planPedido)
            // Si mientras tanto la ruta se recalculó, este trazo ya no sirve
            if (trazos.isEmpty() || isFinishing || planPedido !== plan) return@launch
            // Los minutos de cada tramo no cambian, así que la personita sigue en el mismo momento
            viaje = Viaje(plan, trazos)
            findViewById<View>(R.id.tvViajeCreditoCalles).visibility = View.VISIBLE
            redibujarMapa()
        }
    }

    private fun redibujarMapa() {
        val map = mapa ?: return
        animadorPosicion?.cancel()
        map.clear()
        avatar = null
        rastro = null
        dibujarRuta(map)
        aplicarMinuto(minutoActual, if (pegadaARuta) null else posicionMostrada)
    }

    /**
     * Restaurantes, bares, hoteles... que elegiste en la Guía y quedan a 300 m o menos de la ruta:
     * pasas junto a ellos. Se ven como pines pequeños con su ícono.
     */
    private fun dibujarCercanos(map: GoogleMap) {
        val categorias = (intent.getStringArrayListExtra(EXTRA_CATEGORIAS) ?: arrayListOf())
            .mapNotNull { CategoriaLugar.desdeClave(it) }.toSet()
        if (categorias.isEmpty()) return
        val ruta = Geo.densificar(viaje.puntos)
        val enRuta = waypoints.map { it.nombre }.toSet()
        todosLosLugares
            .filter { it.categoria in categorias && it.lugar.nombre !in enRuta }
            .map { it to Geo.distanciaMinimaARuta(Punto(it.lugar.coordenadas.first, it.lugar.coordenadas.second), ruta) }
            .filter { it.second <= RADIO_CERCANOS_M }
            .sortedBy { it.second }
            .take(20)
            .forEach { (li, _) ->
                map.addMarker(
                    MarkerOptions().position(LatLng(li.lugar.coordenadas.first, li.lugar.coordenadas.second))
                        .title(li.lugar.nombre)
                        .snippet(getString(li.categoria.etiqueta))
                        .icon(MarcadoresMapa.pinPequeno(this, li.categoria))
                        .anchor(0.5f, 1f).zIndex(2.5f)
                )
            }
    }

    private fun moverCamara(p: Punto, animado: Boolean = false) {
        val map = mapa ?: return
        val destino = CameraUpdateFactory.newLatLng(LatLng(p.lat, p.lng))
        if (animado) map.animateCamera(destino) else map.moveCamera(destino)
    }

    // ---------- Avance del viaje ----------

    /**
     * Pone el viaje en ese minuto (barra, rastro, instrucción, tiempo que falta) y dibuja a la
     * personita en [posicion]; si es null, sobre la ruta en ese minuto.
     */
    private fun aplicarMinuto(minuto: Double, posicion: Punto?) {
        minutoActual = minuto.coerceIn(0.0, viaje.totalMin)
        val estado = viaje.estadoEn(minutoActual)
        val segmento = viaje.segmentos.getOrNull(estado.indice)

        // Mapa
        val dondeDibujar = posicion ?: estado.punto
        posicionMostrada = dondeDibujar
        avatar?.position = LatLng(dondeDibujar.lat, dondeDibujar.lng)
        rastro?.points = viaje.segmentos.take(estado.indice).flatMap { s -> s.recorrido.puntos }
            .plus(segmento?.recorrido?.puntosHasta(estado.fraccionTramo) ?: emptyList())
            .map { LatLng(it.lat, it.lng) }
        if (seguirCamara && !llego) moverCamara(dondeDibujar)

        // Cambio de tramo: nueva instrucción y nuevo avatar (personita, combi o taxi)
        if (estado.indice != indiceActual && segmento != null) {
            val anterior = indiceActual
            indiceActual = estado.indice
            if (aviso == Aviso.NINGUNO) mostrarInstruccion(segmento.paso, estado.indice)
            medioActual = medioDe(segmento.paso, segmento.estilo)
            actualizarIconoPersonita()
            if (anterior >= 0) {
                progresoView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                avisarParadaSiCorresponde(anterior)
            }
        }

        // Panel de abajo
        val minutosFaltan = ceil(viaje.totalMin - minutoActual).toInt()
        tvRestante.text = getString(R.string.trip_remaining, formatearTiempo(minutosFaltan))
        val hora = SimpleDateFormat("HH:mm", Locale.getDefault())
            .format(Date(System.currentTimeMillis() + minutosFaltan * 60_000L))
        tvDistancia.text = getString(R.string.trip_distance_eta, formatearDistancia(viaje.metrosRestantes(minutoActual)), hora)
        tvPorcentaje.text = "${(estado.progreso * 100).toInt()}%"
        progresoView.progreso = estado.progreso.toFloat()
    }

    private fun mostrarInstruccion(paso: Paso, indice: Int) {
        findViewById<TextView>(R.id.tvInstruccionPaso).text =
            getString(R.string.trip_step_of, indice + 1, viaje.segmentos.size)
        val (titulo, detalle) = textoDePaso(paso)
        findViewById<TextView>(R.id.tvInstruccionTitulo).text = titulo
        mostrarDetalle(
            when {
                ultimaUbicacion == null -> getString(R.string.trip_waiting_gps)
                distanciaARuta > METROS_FUERA_DE_RUTA -> getString(R.string.trip_off_route, formatearDistancia(distanciaARuta))
                else -> detalle
            }
        )
        findViewById<ImageView>(R.id.ivInstruccionIcono).setImageResource(iconoDePaso(paso))
        findViewById<View>(R.id.flInstruccionIcono).backgroundTintList = ColorStateList.valueOf(colorDePaso(paso))
        saltitoTarjeta()
    }

    private fun mostrarDetalle(texto: String) {
        findViewById<TextView>(R.id.tvInstruccionDetalle).text = texto
    }

    /** Pequeño "salto" de la tarjeta para que se note el cambio. */
    private fun saltitoTarjeta() {
        findViewById<View>(R.id.cardInstruccion).apply {
            scaleX = 0.94f
            scaleY = 0.94f
            animate().scaleX(1f).scaleY(1f).setDuration(320).setInterpolator(OvershootInterpolator(3f)).start()
        }
    }

    /** Si al terminar un tramo había una parada intermedia, lo avisa. */
    private fun avisarParadaSiCorresponde(indiceTerminado: Int) {
        paradaTrasSegmento[indiceTerminado]?.let { nombre ->
            Toast.makeText(this, getString(R.string.step_stop, nombre), Toast.LENGTH_SHORT).show()
        }
    }

    // ---------- Permiso y GPS ----------

    private fun comprobarGpsYSeguir() {
        if (llego || isFinishing) return
        if (!UbicacionHelper.tienePermiso(this)) {
            mostrarAviso(Aviso.SIN_PERMISO)
            return
        }
        val ajustes = LocationSettingsRequest.Builder().addLocationRequest(pedidoUbicacion).build()
        LocationServices.getSettingsClient(this).checkLocationSettings(ajustes)
            .addOnSuccessListener { empezarASeguir() }
            .addOnFailureListener { error ->
                if (error is ResolvableApiException && !isFinishing) {
                    // Muestra el diálogo del sistema "Activar ubicación" sin salir de la app
                    runCatching { encenderGps.launch(IntentSenderRequest.Builder(error.resolution).build()) }
                        .onFailure { empezarASeguir() }
                } else {
                    // Algunos celulares no saben responder: se intenta igual
                    empezarASeguir()
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun empezarASeguir() {
        if (llego || isFinishing || !UbicacionHelper.tienePermiso(this)) return
        mostrarAviso(Aviso.NINGUNO)
        clienteUbicacion.removeLocationUpdates(callbackUbicacion)
        clienteUbicacion.requestLocationUpdates(pedidoUbicacion, callbackUbicacion, Looper.getMainLooper())
        siguiendo = true
        if (ultimaUbicacion == null) mostrarEstadoGps(getString(R.string.trip_gps_searching), GRIS)
    }

    private fun dejarDeSeguir() {
        if (!siguiendo) return
        runCatching { clienteUbicacion.removeLocationUpdates(callbackUbicacion) }
        siguiendo = false
    }

    private fun alTocarActivar() {
        if (!UbicacionHelper.tienePermiso(this)) {
            val puedePreguntar = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (permisoPedido && !puedePreguntar) {
                // Lo negaron con "No volver a preguntar": solo se puede dar en los ajustes de la app
                startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
                )
            } else {
                permisoPedido = true
                pedirUbicacion.launch(UbicacionHelper.PERMISOS)
            }
        } else {
            gpsRechazado = false
            comprobarGpsYSeguir()
        }
    }

    private fun mostrarAviso(nuevo: Aviso) {
        if (nuevo == Aviso.NINGUNO && aviso == Aviso.NINGUNO) return
        aviso = nuevo
        when (nuevo) {
            Aviso.NINGUNO -> {
                btnActivar.visibility = View.GONE
                viaje.segmentos.getOrNull(indiceActual)?.let { mostrarInstruccion(it.paso, indiceActual) }
            }
            Aviso.SIN_PERMISO, Aviso.GPS_APAGADO -> {
                val sinPermiso = nuevo == Aviso.SIN_PERMISO
                btnActivar.visibility = View.VISIBLE
                btnActivar.setText(if (sinPermiso) R.string.trip_allow_location else R.string.trip_turn_on_gps)
                findViewById<TextView>(R.id.tvInstruccionPaso).setText(R.string.trip_mode_live)
                findViewById<TextView>(R.id.tvInstruccionTitulo)
                    .setText(if (sinPermiso) R.string.trip_need_location_title else R.string.trip_gps_off_title)
                mostrarDetalle(getString(if (sinPermiso) R.string.trip_need_location else R.string.trip_gps_off_detail))
                findViewById<ImageView>(R.id.ivInstruccionIcono).setImageResource(R.drawable.ic_gps)
                findViewById<View>(R.id.flInstruccionIcono).backgroundTintList = ColorStateList.valueOf(Color.parseColor(ROJO))
                mostrarEstadoGps(getString(R.string.trip_gps_off), ROJO)
                saltitoTarjeta()
            }
        }
    }

    /** La pastilla de arriba a la derecha: en vivo (verde), señal débil (ámbar), buscando o sin señal. */
    private fun mostrarEstadoGps(texto: String, colorHex: String) {
        tvEstadoGps.text = texto
        TextViewCompat.setCompoundDrawableTintList(tvEstadoGps, ColorStateList.valueOf(Color.parseColor(colorHex)))
    }

    // ---------- Tu ubicación mueve a la personita ----------

    private fun alRecibirUbicacion(ubicacion: Location) {
        if (llego) return
        val p = Punto(ubicacion.latitude, ubicacion.longitude)
        val precision = if (ubicacion.hasAccuracy()) ubicacion.accuracy else PRECISION_MAXIMA_M
        val ahora = SystemClock.elapsedRealtime()
        val anterior = ultimaUbicacion
        val anteriorMs = ultimaUbicacionMs
        ultimaSenalMs = ahora

        // Estado de la señal
        val precisionTexto = precision.roundToInt()
        if (precision <= 30f) mostrarEstadoGps(getString(R.string.trip_gps_live, precisionTexto), VERDE)
        else mostrarEstadoGps(getString(R.string.trip_gps_weak, precisionTexto), AMBAR)
        // Con mucho error no se mueve nada (salvo la primera vez, para no esperar sin fin)
        if (precision > PRECISION_MAXIMA_M && anterior != null) return

        ultimaUbicacion = p
        ultimaUbicacionMs = ahora

        // ¿Te estás moviendo? Por la velocidad que da el GPS o, si no la da (o da 0 por error),
        // porque avanzaste más de lo que mide el error del GPS
        val metrosDesdeAnterior = anterior?.let { Geo.distanciaMetros(it, p) } ?: 0.0
        val velocidadCalculada = metrosDesdeAnterior / max(0.5, (ahora - anteriorMs) / 1000.0)
        val velocidad = if (ubicacion.hasSpeed()) ubicacion.speed.toDouble() else velocidadCalculada
        val seMovio = (velocidad >= VELOCIDAD_MOVIMIENTO && metrosDesdeAnterior >= 1.0) ||
            (velocidadCalculada >= VELOCIDAD_MOVIMIENTO && metrosDesdeAnterior > precision)
        if (anterior != null && seMovio) ultimoMovimientoMs = ahora

        if (anterior == null) {
            alRecibirPrimeraUbicacion(p)
            return
        }

        // Llegada
        if (Geo.distanciaMetros(p, waypoints.last().punto) <= METROS_LLEGADA) {
            animadorPosicion?.cancel()
            aplicarMinuto(viaje.totalMin, null)
            llegar()
            return
        }
        avanzarHasta(p, precision.toDouble())
    }

    private fun alRecibirPrimeraUbicacion(p: Punto) {
        avatar?.alpha = 1f
        val (_, distancia) = viaje.proyectar(p, 0.0)
        val destino = waypoints.last().punto
        if (!yaReplanifico && distancia > METROS_REPLANIFICAR &&
            Geo.distanciaMetros(p, destino) in METROS_LLEGADA..METROS_MAX_REPLANIFICAR
        ) {
            replanificarDesde(p)
        }
        if (Geo.distanciaMetros(p, destino) <= METROS_LLEGADA) {
            aplicarMinuto(viaje.totalMin, null)
            llegar()
            return
        }
        avanzarHasta(p, 0.0, animado = false)
        mapa?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(p.lat, p.lng), 17f))
        viaje.segmentos.getOrNull(indiceActual)?.let { if (aviso == Aviso.NINGUNO) mostrarInstruccion(it.paso, indiceActual) }
    }

    /**
     * Cerca de la ruta, la personita va sobre la línea y el viaje avanza (nunca retrocede).
     * Si te alejas, se dibuja donde estás y el avance espera a que vuelvas.
     */
    private fun avanzarHasta(p: Punto, precision: Double, animado: Boolean = true) {
        val (minutoEnRuta, distancia) = viaje.proyectar(p, minutoActual)
        val estabaFuera = distanciaARuta > METROS_FUERA_DE_RUTA
        distanciaARuta = distancia
        val fuera = distancia > METROS_FUERA_DE_RUTA
        val nuevoMinuto = if (fuera) minutoActual else minutoEnRuta
        val pegar = distancia <= max(METROS_PEGAR_A_RUTA, min(precision, PRECISION_MAXIMA_M.toDouble()))

        // Aviso al salir o volver a la ruta
        if (aviso == Aviso.NINGUNO) {
            if (fuera) mostrarDetalle(getString(R.string.trip_off_route, formatearDistancia(distancia)))
            else if (estabaFuera) viaje.segmentos.getOrNull(indiceActual)?.let { mostrarDetalle(textoDePaso(it.paso).second) }
        }

        // Quieta y con el GPS "temblando" en el mismo sitio: ni se mueve ni avanza el viaje
        val destinoVisual = if (pegar) viaje.estadoEn(nuevoMinuto).punto else p
        val actual = posicionMostrada
        if (!seEstaMoviendo() && actual != null && pegar == pegadaARuta &&
            Geo.distanciaMetros(actual, destinoVisual) < max(5.0, precision * 0.5)
        ) return

        // Hacia dónde mira la personita
        val rumbo = if (pegar) viaje.estadoEn(nuevoMinuto).rumbo
        else actual?.takeIf { Geo.distanciaMetros(it, p) > 2.0 }?.let { Recorrido(listOf(it, p)).rumboEn(0.5) }
        rumbo?.let { voltearSegunRumbo(it) }

        if (animado) moverPersonita(nuevoMinuto, if (pegar) null else p)
        else {
            pegadaARuta = pegar
            aplicarMinuto(nuevoMinuto, if (pegar) null else p)
        }
    }

    /** Desliza a la personita (en ~0.9 s) desde donde está hasta su nueva posición. */
    private fun moverPersonita(minutoDestino: Double, posicionDestino: Punto?) {
        animadorPosicion?.cancel()
        val minutoInicio = minutoActual
        val desde = posicionMostrada
        val pegadaAntes = pegadaARuta
        pegadaARuta = posicionDestino == null
        // Si antes y después va sobre la ruta, avanza por la línea; si no, en línea recta
        val porLaRuta = pegadaAntes && posicionDestino == null
        animadorPosicion = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 900
            interpolator = LinearInterpolator()
            addUpdateListener {
                val t = (it.animatedValue as Float).toDouble()
                val minuto = minutoInicio + (minutoDestino - minutoInicio) * t
                val posicion = if (porLaRuta || desde == null) {
                    posicionDestino
                } else {
                    val hasta = posicionDestino ?: viaje.estadoEn(minutoDestino).punto
                    Punto(desde.lat + (hasta.lat - desde.lat) * t, desde.lng + (hasta.lng - desde.lng) * t)
                }
                aplicarMinuto(minuto, posicion)
            }
            start()
        }
    }

    private fun voltearSegunRumbo(rumbo: Float) {
        // Con margen, para que no "tiemble" al ir hacia el norte o el sur
        val izquierda = when (rumbo) {
            in 200f..340f -> true
            in 20f..160f -> false
            else -> haciaIzquierda
        }
        if (izquierda != haciaIzquierda) {
            haciaIzquierda = izquierda
            actualizarIconoPersonita()
        }
    }

    private fun seEstaMoviendo(): Boolean =
        ultimoMovimientoMs > 0 && SystemClock.elapsedRealtime() - ultimoMovimientoMs < MS_SIN_MOVERSE

    /** La ruta salía de otro lugar: se calcula de nuevo desde donde estás, con las mismas paradas. */
    private fun replanificarDesde(p: Punto) {
        yaReplanifico = true
        waypoints = listOf(Waypoint(getString(R.string.guide_my_location), p)) + waypoints.drop(1)
        plan = planner.planificar(waypoints, modo)
        viaje = Viaje(plan)
        minutoActual = 0.0
        indiceActual = -1
        distanciaARuta = 0.0
        pegadaARuta = true
        posicionMostrada = null
        findViewById<View>(R.id.tvViajeCreditoCalles).visibility = View.GONE
        configurarBarra()
        redibujarMapa()
        aplicarMinuto(0.0, null)
        buscarTrazoPorCalles()
        Toast.makeText(this, R.string.trip_replanned, Toast.LENGTH_SHORT).show()
    }

    private fun confirmarTerminar() {
        if (llego || ultimaUbicacion == null) {
            finish()
            return
        }
        AlertDialog.Builder(this)
            .setTitle(R.string.trip_end_question)
            .setPositiveButton(R.string.trip_end_yes) { _, _ -> finish() }
            .setNegativeButton(R.string.trip_keep_going, null)
            .show()
    }

    // ---------- Llegada ----------

    private fun llegar() {
        if (llego) return
        llego = true
        animadorPosicion?.cancel()
        dejarDeSeguir()
        mostrarEstadoGps(getString(R.string.trip_arrived_title), VERDE)
        progresoView.enMovimiento = false
        progresoView.llego = true
        medioActual = ProgresoViajeView.Medio.A_PIE
        actualizarIconoPersonita()
        progresoView.progreso = 1f
        tvPorcentaje.text = "100%"
        tvRestante.setText(R.string.trip_arrived_title)

        val destino = waypoints.last()
        val lugar = todosLosLugares.firstOrNull { it.lugar.nombre == destino.nombre }
        findViewById<TextView>(R.id.tvLlegadaLugar).text = destino.nombre
        findViewById<TextView>(R.id.tvLlegadaResumen).text = getString(
            R.string.trip_arrived_summary,
            formatearDistancia(plan.distanciaMetros),
            formatearTiempo(plan.minutosTotales)
        )
        lugar?.categoria?.let { cat ->
            findViewById<ImageView>(R.id.ivLlegadaIcono).setImageResource(cat.icono)
            findViewById<View>(R.id.flLlegadaIcono).backgroundTintList = ColorStateList.valueOf(Color.parseColor(cat.colorHex))
        }
        findViewById<View>(R.id.btnLlegadaVerLugar).apply {
            visibility = if (lugar != null) View.VISIBLE else View.GONE
            setOnClickListener { lugar?.let { DetalleNavigator.abrir(this@ViajeActivity, it.lugar) } }
        }

        val overlay = findViewById<View>(R.id.overlayLlegada)
        val tarjeta = findViewById<View>(R.id.cardLlegada)
        overlay.alpha = 0f
        overlay.visibility = View.VISIBLE
        overlay.animate().alpha(1f).setDuration(250).start()
        tarjeta.scaleX = 0.7f
        tarjeta.scaleY = 0.7f
        tarjeta.animate().scaleX(1f).scaleY(1f).setDuration(450).setInterpolator(OvershootInterpolator(2f)).start()
        findViewById<ConfetiView>(R.id.confetiLlegada).post {
            findViewById<ConfetiView>(R.id.confetiLlegada).lanzar()
        }
        overlay.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }

    // ---------- Textos, íconos y colores ----------

    private fun esMovimiento(paso: Paso) =
        paso.tipo == TipoPaso.CAMINAR || paso.tipo == TipoPaso.TRANSPORTE || paso.tipo == TipoPaso.TAXI

    private fun medioDe(paso: Paso, estilo: EstiloTramo) = when {
        estilo == EstiloTramo.CAMINATA -> ProgresoViajeView.Medio.A_PIE
        paso.tipo == TipoPaso.TAXI -> ProgresoViajeView.Medio.TAXI
        else -> ProgresoViajeView.Medio.COMBI
    }

    private fun textoDePaso(paso: Paso): Pair<String, String> = when (paso.tipo) {
        TipoPaso.CAMINAR -> getString(R.string.step_walk_to, paso.destinoNombre) to
            "${formatearDistancia(paso.metros)} · ${formatearTiempo(paso.minutos)}"
        TipoPaso.TRANSPORTE -> getString(R.string.step_take_line, paso.linea?.nombre ?: "") to
            getString(
                R.string.trip_bus_detail,
                paso.paraderoSubida?.nombre ?: "",
                paso.paraderoBajada?.nombre ?: "",
                String.format(Locale.US, "S/ %.2f", paso.linea?.tarifa ?: 0.0)
            )
        TipoPaso.TAXI -> getString(R.string.step_taxi_to, paso.destinoNombre) to
            "${formatearDistancia(paso.metros)} · ${formatearTiempo(paso.minutos)} · ${getString(R.string.step_approx)} " +
            String.format(Locale.US, "S/ %.2f", planner.tarifaTaxi(paso.metros))
        else -> getString(R.string.step_arrive, paso.destinoNombre) to ""
    }

    private fun iconoDePaso(paso: Paso): Int = when (paso.tipo) {
        TipoPaso.CAMINAR -> R.drawable.ic_walk
        TipoPaso.TRANSPORTE -> R.drawable.ic_bus
        TipoPaso.TAXI -> R.drawable.ic_taxi
        else -> MarcadoresMapa.categoriaDe(todosLosLugares, paso.destinoNombre)?.icono ?: R.drawable.ic_bandera
    }

    private fun colorDePaso(paso: Paso): Int = Color.parseColor(
        when (paso.tipo) {
            TipoPaso.CAMINAR -> MarcadoresMapa.COLOR_CAMINAR
            TipoPaso.TRANSPORTE -> MarcadoresMapa.COLOR_COMBI
            TipoPaso.TAXI -> MarcadoresMapa.COLOR_TAXI
            else -> MarcadoresMapa.COLOR_MARCA
        }
    )

    private fun conAlfa(color: Int, alfa: Int) = Color.argb(alfa, Color.red(color), Color.green(color), Color.blue(color))

    private fun formatearTiempo(min: Int): String =
        if (min < 60) getString(R.string.time_minutes, min)
        else getString(R.string.time_hours_minutes, min / 60, min % 60)

    // ---------- Ciclo de vida ----------

    override fun onStart() {
        super.onStart()
        // Al volver de los ajustes de la app (permiso ya dado) se retoma solo
        if (!llego && UbicacionHelper.tienePermiso(this) && !gpsRechazado) comprobarGpsYSeguir()
    }

    override fun onStop() {
        // En segundo plano no se usa el GPS
        dejarDeSeguir()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        handler.removeCallbacks(animarPersonita)
        handler.removeCallbacks(vigilarSenal)
        handler.post(animarPersonita)
        handler.postDelayed(vigilarSenal, 5_000)
    }

    override fun onPause() {
        handler.removeCallbacks(animarPersonita)
        handler.removeCallbacks(vigilarSenal)
        super.onPause()
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        animadorPosicion?.cancel()
        runCatching { clienteUbicacion.removeLocationUpdates(callbackUbicacion) }
        super.onDestroy()
    }
}
