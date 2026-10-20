package com.example.puriyayacucho.ra

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.puriyayacucho.GuiaActivity
import com.example.puriyayacucho.R
import com.example.puriyayacucho.data.SitioRA
import com.example.puriyayacucho.guia.Geo
import com.example.puriyayacucho.guia.UbicacionHelper
import kotlinx.coroutines.launch

/**
 * Flujo para entrar a la RA de un sitio:
 * 1. Pide la ubicación (si hace falta).
 * 2. Si el usuario está en el sitio, abre la RA.
 * 3. Si no, ofrece "Cómo llegar" o "Ver demo".
 *
 * Se crea como propiedad de la Activity (antes de onCreate), porque registra
 * el pedido de permisos: `private val accesoRA = AccesoRA(this)`.
 */
class AccesoRA(private val activity: AppCompatActivity) {

    private var sitioPendiente: SitioRA? = null

    private val pedirUbicacion = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        val sitio = sitioPendiente ?: return@registerForActivityResult
        if (resultado.values.any { it }) verificarDistancia(sitio) else mostrarFueraDelSitio(sitio)
    }

    fun iniciar(sitio: SitioRA) {
        sitioPendiente = sitio
        if (UbicacionHelper.tienePermiso(activity)) verificarDistancia(sitio)
        else pedirUbicacion.launch(UbicacionHelper.PERMISOS)
    }

    private fun verificarDistancia(sitio: SitioRA) {
        activity.lifecycleScope.launch {
            val ubicacion = UbicacionHelper.obtenerUbicacion(activity)
            val dentro = ubicacion != null &&
                Geo.distanciaMetros(ubicacion, sitio.punto) <= sitio.radioActivacionMetros
            if (dentro) RALauncher.abrir(activity, sitio, modoDemo = false)
            else mostrarFueraDelSitio(sitio)
        }
    }

    private fun mostrarFueraDelSitio(sitio: SitioRA) {
        val nombre = activity.getString(sitio.nombre)
        AlertDialog.Builder(activity)
            .setTitle(R.string.ra_outside_title)
            .setMessage(activity.getString(R.string.ra_outside_message, nombre))
            .setPositiveButton(R.string.btn_how_to_get) { _, _ ->
                GuiaActivity.abrirConDestino(activity, nombre, sitio.punto.lat, sitio.punto.lng)
            }
            .setNeutralButton(R.string.ra_view_demo) { _, _ ->
                RALauncher.abrir(activity, sitio, modoDemo = true)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}
