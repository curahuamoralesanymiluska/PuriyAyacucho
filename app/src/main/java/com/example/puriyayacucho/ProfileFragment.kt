package com.example.puriyayacucho

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.puriyayacucho.bienvenida.Bienvenida
import com.example.puriyayacucho.perfil.FotoPerfil
import com.example.puriyayacucho.ui.AvatarPerfilView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : Fragment() {

    private var avatar: AvatarPerfilView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("usuario_sesion", Context.MODE_PRIVATE)
        val nombreUsuario = sharedPref.getString("nombre_usuario", "Usuario")
        val correoUsuario = sharedPref.getString("correo_usuario", "correo@ejemplo.com")

        view.findViewById<TextView>(R.id.tvProfileName).text = nombreUsuario
        view.findViewById<TextView>(R.id.tvProfileEmail).text = correoUsuario

        // Foto de perfil: la misma de la cabecera; al tocarla se cambia (la galería la abre MainActivity)
        avatar = view.findViewById(R.id.avatarPerfilGrande)
        avatar?.setOnClickListener { (activity as? MainActivity)?.cambiarFotoPerfil() }
        mostrarFoto()

        // Botón Mis Favoritos
        view.findViewById<View>(R.id.btnMisFavoritos).setOnClickListener {
            // Acceder al BottomNavigationView de MainActivity
            val navView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)
            navView?.selectedItemId = R.id.navFavoritos
        }

        // Mis boletos y Mis recorridos
        view.findViewById<View>(R.id.btnMisBoletos)?.setOnClickListener {
            startActivity(Intent(activity, MisBoletosActivity::class.java))
        }
        view.findViewById<View>(R.id.btnMisRecorridos)?.setOnClickListener {
            startActivity(Intent(activity, MisRecorridosActivity::class.java))
        }

        // Volver a ver la guía de bienvenida con la personita
        view.findViewById<View>(R.id.btnGuiaBienvenida)?.setOnClickListener {
            Bienvenida.mostrar(requireActivity())
        }

        // Botón Configuración
        view.findViewById<View>(R.id.btnConfiguracion).setOnClickListener {
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Botón Cerrar Sesión
        view.findViewById<View>(R.id.btnCerrarSesion).setOnClickListener {
            sharedPref.edit().clear().apply()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    fun mostrarFoto() {
        val ctx = context ?: return
        avatar?.mostrar(FotoPerfil.archivo(ctx))
    }

    override fun onDestroyView() {
        avatar = null
        super.onDestroyView()
    }
}
