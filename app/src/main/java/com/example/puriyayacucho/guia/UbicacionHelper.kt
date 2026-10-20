package com.example.puriyayacucho.guia

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/** Obtiene la ubicación actual del usuario (o null si no hay permiso o falla). */
object UbicacionHelper {

    /** Plaza Mayor de Huamanga: se usa como origen cuando no hay ubicación. */
    val PLAZA_MAYOR = Punto(-13.1605, -74.2257)

    val PERMISOS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    fun tienePermiso(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    suspend fun obtenerUbicacion(context: Context): Punto? {
        if (!tienePermiso(context)) return null
        val cliente = LocationServices.getFusedLocationProviderClient(context)
        val cancelacion = CancellationTokenSource()
        return suspendCancellableCoroutine { cont ->
            cont.invokeOnCancellation { cancelacion.cancel() }
            cliente.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cancelacion.token)
                .addOnSuccessListener { loc ->
                    if (cont.isActive) cont.resume(loc?.let { Punto(it.latitude, it.longitude) })
                }
                .addOnFailureListener {
                    if (cont.isActive) cont.resume(null)
                }
        }
    }
}
