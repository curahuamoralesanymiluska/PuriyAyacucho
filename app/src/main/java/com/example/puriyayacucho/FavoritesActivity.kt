package com.example.puriyayacucho

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puriyayacucho.adapters.FavoritoAdapter
import com.example.puriyayacucho.data.AppDatabase
import com.example.puriyayacucho.data.FavoritoEntity
import com.example.puriyayacucho.data.LugarTuristicoFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {

    private var rvFavoritos: RecyclerView? = null
    private var emptyState: View? = null
    private var tvCount: TextView? = null
    private var adapter: FavoritoAdapter? = null
    private var userEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_favorites)

            val sharedPref = getSharedPreferences("usuario_sesion", MODE_PRIVATE)
            userEmail = sharedPref.getString("correo_usuario", "") ?: ""

            initializeViews()
            setupRecyclerView()
            loadFavorites()
            setupBottomNavigation()

        } catch (e: Exception) {
            android.util.Log.e("FavoritesCrash", "Error al iniciar favoritos", e)
            Toast.makeText(this, "Cargando favoritos...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeViews() {
        rvFavoritos = findViewById(R.id.rvFavoritos)
        emptyState = findViewById(R.id.emptyStateFavoritos)
        tvCount = findViewById(R.id.tvCountFavoritos)

        findViewById<View>(R.id.btnExplorar)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = FavoritoAdapter(
            favoritos = emptyList(),
            onDeleteClick = { favorito -> deleteFavorite(favorito) },
            onViewClick = { favorito -> navigateToDetails(favorito) }
        )
        rvFavoritos?.layoutManager = LinearLayoutManager(this)
        rvFavoritos?.adapter = adapter
    }

    private fun loadFavorites() {
        if (userEmail.isEmpty()) return

        lifecycleScope.launch {
            val lista = AppDatabase.getInstance(this@FavoritesActivity)
                .favoritoDao().obtenerPorUsuario(userEmail)

            if (lista.isEmpty()) {
                rvFavoritos?.visibility = View.GONE
                emptyState?.visibility = View.VISIBLE
                tvCount?.text = "No hay lugares guardados"
            } else {
                rvFavoritos?.visibility = View.VISIBLE
                emptyState?.visibility = View.GONE
                tvCount?.text = "${lista.size} lugares guardados"
                adapter?.updateList(lista)
            }
        }
    }

    private fun deleteFavorite(favorito: FavoritoEntity) {
        lifecycleScope.launch {
            AppDatabase.getInstance(this@FavoritesActivity)
                .favoritoDao().eliminar(favorito)
            Toast.makeText(this@FavoritesActivity, "${favorito.nombre} eliminado", Toast.LENGTH_SHORT).show()
            loadFavorites()
        }
    }

    private fun navigateToDetails(favorito: FavoritoEntity) {
        val lugarCompleto = LugarTuristicoFactory.obtenerLugaresTuristicosAyacucho(this)
            .find { it.id == favorito.lugarId }

        if (lugarCompleto != null) {
            val intent = Intent(this, PlaceDetailsActivity::class.java).apply {
                putExtra("placeId", lugarCompleto.id)
                putExtra("latitude", lugarCompleto.coordenadas.first)
                putExtra("longitude", lugarCompleto.coordenadas.second)
                putExtra("placeName", lugarCompleto.nombre)
                putExtra("description", lugarCompleto.descripcion)
                putExtra("category", lugarCompleto.tipo)
                putExtra("imageRes", lugarCompleto.imagen as Int)
                putExtra("videoUrl", lugarCompleto.videoUrl)
                putExtra("hours", lugarCompleto.horario)
                // Aseguramos que la lista sea de tipo ArrayList<Int>
                putExtra("galleryImages", ArrayList(lugarCompleto.galeria.map { it as Int }))
            }
            startActivity(intent)
        }
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navHome -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.navFavoritos -> true
                R.id.navExplorar -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.navPerfil -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
        bottomNav?.selectedItemId = R.id.navFavoritos
    }
}
