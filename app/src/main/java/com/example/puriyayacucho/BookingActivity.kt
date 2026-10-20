package com.example.puriyayacucho

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puriyayacucho.adapters.ReservaAdapter
import com.example.puriyayacucho.data.AppDatabase
import com.example.puriyayacucho.data.ReservaEntity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class BookingActivity : AppCompatActivity() {

    private lateinit var rvReservas: RecyclerView
    private lateinit var emptyStateReservas: LinearLayout
    private lateinit var tabBookingStatus: TabLayout
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var adapter: ReservaAdapter

    private var userEmail: String = ""
    private var todasLasReservas: List<ReservaEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_booking)

        val sharedPref = getSharedPreferences("usuario_sesion", MODE_PRIVATE)
        userEmail = sharedPref.getString("correo_usuario", "") ?: ""

        val mainView = findViewById<View>(R.id.main)
        mainView?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        initializeViews()
        setupRecyclerView()
        setupTabLayout()
        setupBottomNavigation()
        cargarReservas()
    }

    private fun initializeViews() {
        rvReservas = findViewById(R.id.rvReservas)
        emptyStateReservas = findViewById(R.id.emptyStateReservas)
        tabBookingStatus = findViewById(R.id.tabBookingStatus)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        findViewById<View>(R.id.btnExplorarReservas)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = ReservaAdapter(emptyList()) { reserva ->
            eliminarReserva(reserva)
        }
        rvReservas.layoutManager = LinearLayoutManager(this)
        rvReservas.adapter = adapter
    }

    private fun cargarReservas() {
        if (userEmail.isEmpty()) {
            mostrarEstadoVacio(true)
            return
        }

        lifecycleScope.launch {
            todasLasReservas = AppDatabase.getInstance(this@BookingActivity)
                .reservaDao()
                .obtenerReservasUsuario(userEmail)

            filtrarReservas(tabBookingStatus.selectedTabPosition)
        }
    }

    private fun eliminarReserva(reserva: ReservaEntity) {
        lifecycleScope.launch {
            AppDatabase.getInstance(this@BookingActivity)
                .reservaDao()
                .eliminarReserva(reserva.id)
            Toast.makeText(this@BookingActivity, "Reserva eliminada", Toast.LENGTH_SHORT).show()
            cargarReservas()
        }
    }

    private fun filtrarReservas(position: Int) {
        val estadoFiltro = when (position) {
            0 -> "Pendiente"
            1 -> "Completada"
            2 -> "Cancelada"
            else -> "Pendiente"
        }

        val listaFiltrada = todasLasReservas.filter { it.estado == estadoFiltro }
        adapter.updateList(listaFiltrada)
        mostrarEstadoVacio(listaFiltrada.isEmpty())
    }

    private fun mostrarEstadoVacio(isEmpty: Boolean) {
        if (isEmpty) {
            emptyStateReservas.visibility = View.VISIBLE
            rvReservas.visibility = View.GONE
        } else {
            emptyStateReservas.visibility = View.GONE
            rvReservas.visibility = View.VISIBLE
        }
    }

    private fun setupTabLayout() {
        tabBookingStatus.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                filtrarReservas(tab?.position ?: 0)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.navExplorar // O el ID que corresponda

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navHome -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.navFavoritos -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    finish()
                    true
                }
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
    }
}
