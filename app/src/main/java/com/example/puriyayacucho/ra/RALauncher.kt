package com.example.puriyayacucho.ra

import android.content.Context
import com.example.puriyayacucho.ARCoreActivity
import com.example.puriyayacucho.RAActivity
import com.example.puriyayacucho.data.SitioRA
import com.google.ar.core.ArCoreApk

/**
 * Elige la mejor experiencia de RA para el celular:
 * - ARCore (modelo 3D anclado al suelo) si el equipo es compatible.
 * - RA simple (reconstrucción dibujada sobre la cámara) en el resto.
 */
object RALauncher {

    fun arCoreCompatible(context: Context): Boolean = try {
        val disponibilidad = ArCoreApk.getInstance().checkAvailability(context)
        // "Transitorio" = todavía consultando; lo tratamos como compatible
        // (si luego falla, ARCoreActivity cambia sola a la versión simple).
        disponibilidad.isSupported || disponibilidad.isTransient
    } catch (e: Exception) {
        false
    }

    fun abrir(context: Context, sitio: SitioRA, modoDemo: Boolean) {
        if (arCoreCompatible(context)) ARCoreActivity.abrir(context, sitio, modoDemo)
        else RAActivity.abrir(context, sitio, modoDemo)
    }
}
