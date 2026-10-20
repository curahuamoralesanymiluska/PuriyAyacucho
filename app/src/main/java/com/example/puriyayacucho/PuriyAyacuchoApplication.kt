package com.example.puriyayacucho

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen

class PuriyAyacuchoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        // Aplicar el idioma guardado al iniciar la app
        LocaleHelper.applySavedLocale(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }
}
