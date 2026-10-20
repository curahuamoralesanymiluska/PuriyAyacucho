package com.example.puriyayacucho

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileEmail: TextView
    private lateinit var btnMisReservas: LinearLayout
    private lateinit var btnMisFavoritos: LinearLayout
    private lateinit var btnCerrarSesion: LinearLayout
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        val mainView = findViewById<View>(R.id.main)
        mainView?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        initializeViews()
        setupProfileData()
        setupMenuListeners()
        setupBottomNavigation()
    }

    private fun initializeViews() {
        tvProfileName = findViewById(R.id.tvProfileName)
        tvProfileEmail = findViewById(R.id.tvProfileEmail)
        btnMisReservas = findViewById(R.id.btnMisReservas)
        btnMisFavoritos = findViewById(R.id.btnMisFavoritos)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        bottomNavigation = findViewById(R.id.bottomNavigation)
    }

    private fun setupProfileData() {
        val sharedPref = getSharedPreferences("usuario_sesion", MODE_PRIVATE)
        val nombreUsuario = sharedPref.getString("nombre_usuario", "Usuario") ?: "Usuario"
        val correoUsuario = sharedPref.getString("correo_usuario", "usuario@example.com") ?: "usuario@example.com"

        tvProfileName.text = nombreUsuario
        tvProfileEmail.text = correoUsuario
    }

    private fun setupMenuListeners() {
        btnMisReservas.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

        btnMisFavoritos.setOnClickListener {
            // Regresar a la pantalla de favoritos (vía MainActivity o fragmento)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("navigate_to", "favoritos")
            startActivity(intent)
            finish()
        }

        btnCerrarSesion.setOnClickListener {
            val sharedPref = getSharedPreferences("usuario_sesion", MODE_PRIVATE)
            sharedPref.edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.navPerfil

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navHome -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.navFavoritos -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("navigate_to", "favoritos")
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.navExplorar -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.navPerfil -> true
                else -> false
            }
        }
    }
}
