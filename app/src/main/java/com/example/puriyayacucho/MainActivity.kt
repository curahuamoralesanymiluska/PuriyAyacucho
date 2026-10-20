package com.example.puriyayacucho

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.puriyayacucho.bienvenida.Bienvenida
import com.example.puriyayacucho.perfil.FotoPerfil
import com.example.puriyayacucho.ui.AvatarPerfilView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private var tvUserName: TextView? = null
    private var bottomNavigation: BottomNavigationView? = null
    private var etSearch: EditText? = null
    private var appBarLayout: AppBarLayout? = null
    private var fragmentContainer: View? = null
    private var fabLanguage: FloatingActionButton? = null
    private var avatarPerfil: AvatarPerfilView? = null

    /** Galería del sistema para elegir la foto de perfil (no pide permisos). */
    private val elegirFoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) guardarFotoPerfil(uri)
    }

    /** Categoría que Explorar debe mostrar al abrirse desde un módulo de Inicio. */
    private var categoriaPendiente: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main)

            val root = findViewById<android.view.View>(R.id.main)
            root?.let {
                ViewCompat.setOnApplyWindowInsetsListener(it) { v, insets ->
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                    insets
                }
            }

            initializeViews()
            setupUserProfile()
            setupBottomNavigation()
            setupSearch()
            setupLanguageButton()

            handleIntentNavigation(intent)

            if (savedInstanceState == null && bottomNavigation?.selectedItemId == R.id.navHome) {
                loadFragment(HomeFragment())
            }

            // Guía de bienvenida: la personita aparece al entrar después de iniciar sesión
            if (savedInstanceState == null) Bienvenida.mostrarSiCorresponde(this)

        } catch (e: Exception) {
            android.util.Log.e("PuriyCrash", "Error en MainActivity", e)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntentNavigation(intent)
    }

    private fun handleIntentNavigation(intent: Intent?) {
        val navigateTo = intent?.getStringExtra("navigate_to")
        when (navigateTo) {
            "favoritos" -> {
                bottomNavigation?.selectedItemId = R.id.navFavoritos
            }
            "perfil" -> {
                bottomNavigation?.selectedItemId = R.id.navPerfil
            }
            "explorar" -> {
                abrirExplorar(intent.getStringExtra("categoria"))
            }
        }
    }

    private fun initializeViews() {
        tvUserName = findViewById(R.id.tvUserName)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        etSearch = findViewById(R.id.etSearch)
        appBarLayout = findViewById(R.id.appBarLayout)
        fragmentContainer = findViewById(R.id.fragment_container)
        fabLanguage = findViewById(R.id.fabLanguage)
    }

    private fun setupUserProfile() {
        val sharedPref = getSharedPreferences("usuario_sesion", MODE_PRIVATE)
        val nombreCompleto = sharedPref.getString("nombre_usuario", getString(R.string.guest)) ?: getString(R.string.guest)
        val primerNombre = nombreCompleto.split(" ").getOrNull(0) ?: ""

        // "¡Hola!" ya está en la línea de arriba: aquí va solo el nombre
        tvUserName?.text = primerNombre.ifBlank { getString(R.string.guest) }

        // En lugar del logo: la personita, que se cambia por tu foto al tocarla
        avatarPerfil = findViewById(R.id.avatarPerfil)
        avatarPerfil?.setOnClickListener { cambiarFotoPerfil() }
        mostrarFotoPerfil()
    }

    /** Sin foto, abre la galería; con foto, ofrece cambiarla o quitarla. También lo usa Perfil. */
    fun cambiarFotoPerfil() {
        if (!FotoPerfil.existe(this)) {
            abrirGaleria()
            return
        }
        AlertDialog.Builder(this)
            .setTitle(R.string.profile_photo_title)
            .setItems(
                arrayOf(getString(R.string.profile_photo_change), getString(R.string.profile_photo_remove))
            ) { _, opcion ->
                if (opcion == 0) {
                    abrirGaleria()
                } else {
                    FotoPerfil.borrar(this)
                    mostrarFotoPerfil()
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun abrirGaleria() {
        elegirFoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun guardarFotoPerfil(uri: Uri) {
        lifecycleScope.launch {
            val ok = FotoPerfil.guardar(this@MainActivity, uri)
            Toast.makeText(
                this@MainActivity,
                if (ok) R.string.profile_photo_saved else R.string.profile_photo_error,
                Toast.LENGTH_SHORT
            ).show()
            mostrarFotoPerfil()
        }
    }

    /** Pone la foto (o la personita) en la cabecera y en Perfil, si está abierto. */
    fun mostrarFotoPerfil() {
        avatarPerfil?.mostrar(FotoPerfil.archivo(this))
        (supportFragmentManager.findFragmentById(R.id.fragment_container) as? ProfileFragment)?.mostrarFoto()
    }

    private fun setupSearch() {
        etSearch?.addTextChangedListener { text ->
            val query = text.toString()
            if (query.isNotEmpty()) {
                if (bottomNavigation?.selectedItemId != R.id.navExplorar) {
                    bottomNavigation?.selectedItemId = R.id.navExplorar
                }

                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (fragment is ExploreFragment) {
                    fragment.filtrarPorNombre(query)
                }
            } else {
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (fragment is ExploreFragment) {
                    fragment.resetearFiltros()
                }
            }
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation?.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.navHome -> HomeFragment()
                R.id.navExplorar -> ExploreFragment.nuevaInstancia(categoriaPendiente).also { categoriaPendiente = null }
                R.id.navCalendario -> CalendarFragment()
                R.id.navFavoritos -> FavoritesFragment()
                R.id.navPerfil -> ProfileFragment()
                else -> HomeFragment()
            }
            loadFragment(fragment)

            val params = fragmentContainer?.layoutParams as? CoordinatorLayout.LayoutParams
            if (item.itemId == R.id.navCalendario) {
                appBarLayout?.visibility = View.GONE
                params?.behavior = null
            } else {
                appBarLayout?.visibility = View.VISIBLE
                params?.behavior = AppBarLayout.ScrollingViewBehavior()
            }
            fragmentContainer?.requestLayout()

            true
        }
    }

    private fun setupLanguageButton() {
        fabLanguage?.setOnClickListener {
            showLanguageDialog()
        }
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("Español", "English", "Português", "Quechua")
        val codes = arrayOf("es", "en", "pt", "qu")

        val currentLang = getSharedPreferences("app_settings", Context.MODE_PRIVATE).getString("language", "es")
        var selectedIndex = codes.indexOf(currentLang)
        if (selectedIndex == -1) selectedIndex = 0

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.language))
            .setSingleChoiceItems(languages, selectedIndex) { dialog, which ->
                val selectedCode = codes[which]
                updateLanguage(selectedCode)
                dialog.dismiss()
            }
            .show()
    }

    private fun updateLanguage(code: String) {
        // Guardar el idioma seleccionado en SharedPreferences
        val sharedPref = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        sharedPref.edit().putString("language", code).apply()

        // Aplicar cambio de idioma usando LocaleHelper
        LocaleHelper.setLocale(this, code)

        // Aplicar cambio de idioma usando el API moderno de AndroidX
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(code)
        AppCompatDelegate.setApplicationLocales(appLocale)

        // Reiniciar la actividad principal para refrescar todo
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    /** Abre Explorar mostrando directamente una categoría (por ejemplo "hoteles"). */
    fun abrirExplorar(categoria: String?) {
        val actual = supportFragmentManager.findFragmentById(R.id.fragment_container)
        val cat = com.example.puriyayacucho.data.CategoriaLugar.desdeClave(categoria)
        if (actual is ExploreFragment && bottomNavigation?.selectedItemId == R.id.navExplorar) {
            if (cat != null) actual.mostrarCategoria(cat)
        } else {
            categoriaPendiente = categoria
            bottomNavigation?.selectedItemId = R.id.navExplorar
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
