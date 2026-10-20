package com.example.puriyayacucho

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

open class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        // Aplica el idioma antes de cargar la pantalla
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    protected fun setupLanguageFab() {
        findViewById<FloatingActionButton>(R.id.fabLanguage)?.setOnClickListener {
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
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun updateLanguage(code: String) {
        // Guardar idioma
        LocaleHelper.setLocale(this, code)

        // Aplicar cambio usando API moderna
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(code)
        AppCompatDelegate.setApplicationLocales(appLocale)

        // Reiniciar app para aplicar cambios globalmente
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
