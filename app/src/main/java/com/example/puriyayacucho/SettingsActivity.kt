package com.example.puriyayacucho

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.os.LocaleListCompat
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : BaseActivity() {

    private lateinit var tvCurrentLanguage: TextView
    private lateinit var switchNotifications: SwitchMaterial
    private lateinit var switchDarkMode: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        tvCurrentLanguage = findViewById(R.id.tvCurrentLanguage)
        switchNotifications = findViewById(R.id.switchNotifications)
        switchDarkMode = findViewById(R.id.switchNotifications) // Temporalmente usamos el mismo si no existe el otro

        // Cargar preferencias guardadas
        val sharedPref = getSharedPreferences("app_settings", MODE_PRIVATE)
        val languageCode = sharedPref.getString("language", "es")
        tvCurrentLanguage.text = getLanguageName(languageCode ?: "es")

        val notificationsEnabled = sharedPref.getBoolean("notifications", true)
        switchNotifications.isChecked = notificationsEnabled

        findViewById<View>(R.id.btnLanguage).setOnClickListener {
            showLanguageDialog()
        }

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean("notifications", isChecked).apply()
            val msg = if (isChecked) getString(R.string.success) else getString(R.string.error)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btnChangePassword).setOnClickListener {
            Toast.makeText(this, "Función para cambiar contraseña", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btnDeleteAccount).setOnClickListener {
            showDeleteAccountDialog()
        }
    }

    private fun getLanguageName(code: String): String {
        return when (code) {
            "en" -> "English"
            "pt" -> "Português"
            "qu" -> "Quechua"
            else -> "Español"
        }
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("Español", "English", "Português", "Quechua")
        val codes = arrayOf("es", "en", "pt", "qu")

        val currentLang = getSharedPreferences("app_settings", MODE_PRIVATE).getString("language", "es")
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
        val sharedPref = getSharedPreferences("app_settings", MODE_PRIVATE)
        sharedPref.edit().putString("language", code).apply()

        // Guardar en SharedPreferences para persistencia manual si es necesario
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

    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Cuenta")
            .setMessage("¿Estás seguro?")
            .setPositiveButton("Eliminar") { _, _ ->
                Toast.makeText(this, "Cuenta eliminada", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
