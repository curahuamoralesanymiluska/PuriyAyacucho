package com.example.puriyayacucho.bienvenida

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.example.puriyayacucho.R

/**
 * Cuándo se muestra la guía de bienvenida.
 *
 * Sale **al entrar a la app después de iniciar sesión**. Se guarda en `usuario_sesion`, el mismo
 * archivo de la sesión: como "Cerrar sesión" lo borra, al volver a iniciar sesión la guía aparece
 * otra vez. Desde Perfil → "Guía de bienvenida" se puede ver cuando quieras.
 */
object Bienvenida {

    private const val PREFERENCIAS = "usuario_sesion"
    private const val CLAVE_VISTA = "bienvenida_vista"
    private const val TAG = "bienvenida"

    /** Los mensajes de la guía, en orden. */
    val MENSAJES = intArrayOf(
        R.string.welcome_msg_1,
        R.string.welcome_msg_2,
        R.string.welcome_msg_3,
        R.string.welcome_msg_4,
        R.string.welcome_msg_5,
        R.string.welcome_msg_6,
        R.string.welcome_msg_7
    )

    /** Muestra la guía si en esta sesión todavía no se vio. */
    fun mostrarSiCorresponde(activity: FragmentActivity) {
        val prefs = activity.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE)
        if (prefs.getBoolean(CLAVE_VISTA, false)) return
        mostrar(activity)
    }

    /** Muestra la guía ahora (por ejemplo, desde Perfil). */
    fun mostrar(activity: FragmentActivity) {
        val fm = activity.supportFragmentManager
        if (fm.isStateSaved || fm.findFragmentByTag(TAG) != null) return
        BienvenidaDialog().show(fm, TAG)
    }

    fun marcarVista(context: Context) {
        context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE)
            .edit().putBoolean(CLAVE_VISTA, true).apply()
    }
}
