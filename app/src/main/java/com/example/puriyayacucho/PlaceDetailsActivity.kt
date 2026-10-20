package com.example.puriyayacucho

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.puriyayacucho.adapters.GalleryAdapter
import com.example.puriyayacucho.data.AppDatabase
import com.example.puriyayacucho.data.FavoritoEntity
import com.example.puriyayacucho.data.ReservaEntity
import com.example.puriyayacucho.data.SitioRA
import com.example.puriyayacucho.data.SitioRAFactory
import com.example.puriyayacucho.guia.Geo
import com.example.puriyayacucho.guia.UbicacionHelper
import com.example.puriyayacucho.ra.RALauncher
import com.example.puriyayacucho.data.LugarMapper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlaceDetailsActivity : BaseActivity() {

    private lateinit var ivMainImage: ImageView
    private lateinit var ivBack: ImageView
    private lateinit var ivFavorite: ImageView
    private lateinit var tvPlaceName: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvHours: TextView
    private lateinit var tvLocation: TextView
    private lateinit var btnMapa: Button
    private lateinit var btnReservar: Button
    private lateinit var btnVideo: Button
    private lateinit var rvGallery: RecyclerView
    private lateinit var rvComodidades: RecyclerView

    private var latitude: Double = -13.1519
    private var longitude: Double = -74.2145
    private var placeId: Int = -1
    private var placeName: String = ""
    private var videoId: String = ""
    private var imageSource: Any = R.drawable.logo
    private var category: String = ""
    private var isFavorite: Boolean = false
    private var userEmail: String = ""
    private var combinedGallery: MutableList<Any> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)

        val sharedPref = getSharedPreferences("usuario_sesion", MODE_PRIVATE)
        userEmail = sharedPref.getString("correo_usuario", "") ?: ""

        placeId = intent.getIntExtra("placeId", -1)
        latitude = intent.getDoubleExtra("latitude", -13.1519)
        longitude = intent.getDoubleExtra("longitude", -74.2145)
        placeName = intent.getStringExtra("placeName") ?: "Lugar"
        videoId = intent.getStringExtra("videoUrl") ?: ""

        val imageRes = intent.getIntExtra("imageRes", -1)
        val imageUrl = intent.getStringExtra("imageUrl")
        imageSource = if (!imageUrl.isNullOrEmpty()) imageUrl!! else if (imageRes != -1) imageRes else R.drawable.logo

        category = intent.getStringExtra("category") ?: "Turismo"
        val description = intent.getStringExtra("description") ?: ""
        val recomendacion = intent.getStringExtra("recommendation") ?: ""
        val hours = intent.getStringExtra("hours") ?: "Horario no disponible"
        val locationText = intent.getStringExtra("location") ?: "Ayacucho, Perú"

        val galleryRes = intent.getIntegerArrayListExtra("galleryImages")
        val galleryUrls = intent.getStringArrayListExtra("galleryUrls")

        combinedGallery.clear()
        galleryRes?.let { combinedGallery.addAll(it) }
        galleryUrls?.let { combinedGallery.addAll(it) }

        initializeViews()

        tvPlaceName.text = placeName
        tvDescription.text = if (recomendacion.isNotEmpty()) "$description\n\n$recomendacion" else description
        tvCategory.text = category
        tvHours.text = hours
        tvLocation.text = locationText

        Glide.with(this)
            .load(imageSource)
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .centerCrop()
            .into(ivMainImage)

        // Configuración de visibilidad de botones según categoría
        when (category) {
            "Festividad" -> {
                btnMapa.visibility = View.GONE
                btnReservar.visibility = View.GONE
            }
            "Naturaleza", "Templo Religioso" -> {
                btnReservar.visibility = View.GONE
            }
        }
        // Bares y recreación no tienen reserva (hoteles y Airbnb sí)
        if (category == getString(R.string.cat_bar) || category == getString(R.string.cat_recreation) || category == getString(R.string.chip_cafes)) {
            btnReservar.visibility = View.GONE
        }

        checkIfFavorite()
        setupListeners()
        setupGuiaYRealidadAumentada()
        setupGallery()
        setupComodidades()
        setupLanguageFab()
    }

    private fun initializeViews() {
        ivMainImage = findViewById(R.id.ivMainImage)
        ivBack = findViewById(R.id.ivBack)
        ivFavorite = findViewById(R.id.ivFavorite)
        tvPlaceName = findViewById(R.id.tvPlaceName)
        tvCategory = findViewById(R.id.tvCategory)
        tvDescription = findViewById(R.id.tvDescription)
        tvHours = findViewById(R.id.tvHours)
        tvLocation = findViewById(R.id.tvLocation)
        btnMapa = findViewById(R.id.btnMapa)
        btnReservar = findViewById(R.id.btnReservar)
        btnVideo = findViewById(R.id.btnVideo)
        rvGallery = findViewById(R.id.rvGallery)
        rvComodidades = findViewById(R.id.rvComodidades)
    }

    private fun setupComodidades() {
        val lugar = LugarMapper.todos(this).find { it.lugar.id == placeId }?.lugar
        val comodidades = lugar?.itemFiltrable?.comodidades?.toList() ?: emptyList()

        if (comodidades.isEmpty()) {
            rvComodidades.visibility = View.GONE
        } else {
            rvComodidades.visibility = View.VISIBLE
            rvComodidades.adapter = ComodidadesAdapter(comodidades)
        }
    }

    private fun checkIfFavorite() {
        if (userEmail.isEmpty() || placeId == -1) return
        lifecycleScope.launch {
            val fav = AppDatabase.getInstance(this@PlaceDetailsActivity).favoritoDao().buscarFavorito(userEmail, placeId)
            isFavorite = fav != null
            updateFavoriteIcon()
        }
    }

    private fun updateFavoriteIcon() {
        ivFavorite.setColorFilter(if (isFavorite) Color.parseColor("#FF3D5A") else Color.parseColor("#CCCCCC"))
    }

    private fun setupListeners() {
        ivBack.setOnClickListener { finish() }
        ivFavorite.setOnClickListener {
            if (userEmail.isEmpty()) {
                Toast.makeText(this, "Inicia sesión para guardar favoritos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val dao = AppDatabase.getInstance(this@PlaceDetailsActivity).favoritoDao()
                if (isFavorite) {
                    dao.eliminarPorId(userEmail, placeId)
                    isFavorite = false
                    Toast.makeText(this@PlaceDetailsActivity, "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
                } else {
                    val currentImg = imageSource
                    val finalUrlStr = if (currentImg is String) currentImg else ""
                    val finalResId = if (currentImg is Int) currentImg else 0

                    dao.insertar(FavoritoEntity(
                        usuarioCorreo = userEmail,
                        lugarId = placeId,
                        nombre = placeName,
                        imagenUrl = finalUrlStr,
                        imagenRes = finalResId,
                        tipo = category
                    ))
                    isFavorite = true
                    Toast.makeText(this@PlaceDetailsActivity, "¡Agregado a favoritos!", Toast.LENGTH_SHORT).show()
                }
                updateFavoriteIcon()
            }
        }

        btnMapa.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java).apply {
                putExtra("latitude", latitude)
                putExtra("longitude", longitude)
                putExtra("placeName", placeName)
            }
            startActivity(intent)
        }

        btnReservar.setOnClickListener {
            if (userEmail.isEmpty()) {
                Toast.makeText(this, "Inicia sesión para realizar una reserva", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            lifecycleScope.launch {
                val currentImg = imageSource
                val finalResId = if (currentImg is Int) currentImg else 0

                val reserva = ReservaEntity(
                    usuarioCorreo = userEmail,
                    lugarId = placeId,
                    nombreLugar = placeName,
                    fecha = fechaActual,
                    imagenRes = finalResId,
                    estado = "Pendiente"
                )
                AppDatabase.getInstance(this@PlaceDetailsActivity).reservaDao().insertar(reserva)
                Toast.makeText(this@PlaceDetailsActivity, "¡Reserva realizada con éxito!", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@PlaceDetailsActivity, BookingActivity::class.java))
            }
        }

        btnVideo.setOnClickListener {
            if (videoId.isNotEmpty()) {
                startActivity(Intent(this, VideoActivity::class.java).apply {
                    putExtra("videoId", videoId)
                    putExtra("title", placeName)
                })
            } else {
                Toast.makeText(this, "Video no disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ---------- Guía personalizada y Realidad Aumentada ----------

    private var sitioRA: SitioRA? = null

    private val pedirUbicacionRA = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        val sitio = sitioRA ?: return@registerForActivityResult
        if (resultado.values.any { it }) verificarDistanciaYAbrirRA(sitio)
        else mostrarDialogoFueraDelSitio(sitio)
    }

    private fun setupGuiaYRealidadAumentada() {
        val btnComoLlegar = findViewById<View>(R.id.btnComoLlegar)
        val btnVerRA = findViewById<View>(R.id.btnVerRA)

        // "Cómo llegar" abre la Guía con este lugar como destino (no aplica a festividades)
        if (category == "Festividad" || category == getString(R.string.cat_festivity)) {
            btnComoLlegar?.visibility = View.GONE
        }
        btnComoLlegar?.setOnClickListener {
            GuiaActivity.abrirConDestino(this, placeName, latitude, longitude)
        }

        // "Ver en RA 3D" solo aparece en Wari, Pampa de Quinua y Vilcashuamán
        sitioRA = SitioRAFactory.paraLugar(latitude, longitude)
        btnVerRA?.visibility = if (sitioRA != null) View.VISIBLE else View.GONE
        btnVerRA?.setOnClickListener {
            val sitio = sitioRA ?: return@setOnClickListener
            if (UbicacionHelper.tienePermiso(this)) verificarDistanciaYAbrirRA(sitio)
            else pedirUbicacionRA.launch(UbicacionHelper.PERMISOS)
        }
    }

    /** La RA se activa solo si el usuario está dentro del radio del sitio. */
    private fun verificarDistanciaYAbrirRA(sitio: SitioRA) {
        lifecycleScope.launch {
            val ubicacion = UbicacionHelper.obtenerUbicacion(this@PlaceDetailsActivity)
            val dentro = ubicacion != null &&
                Geo.distanciaMetros(ubicacion, sitio.punto) <= sitio.radioActivacionMetros
            if (dentro) RALauncher.abrir(this@PlaceDetailsActivity, sitio, modoDemo = false)
            else mostrarDialogoFueraDelSitio(sitio)
        }
    }

    private fun mostrarDialogoFueraDelSitio(sitio: SitioRA) {
        AlertDialog.Builder(this)
            .setTitle(R.string.ra_outside_title)
            .setMessage(getString(R.string.ra_outside_message, getString(sitio.nombre)))
            .setPositiveButton(R.string.btn_how_to_get) { _, _ ->
                GuiaActivity.abrirConDestino(this, placeName, latitude, longitude)
            }
            .setNeutralButton(R.string.ra_view_demo) { _, _ ->
                RALauncher.abrir(this, sitio, modoDemo = true)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun setupGallery() {
        if (combinedGallery.isEmpty()) return
        rvGallery.apply {
            layoutManager = LinearLayoutManager(this@PlaceDetailsActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = GalleryAdapter(combinedGallery) { source -> }
        }
    }
}
