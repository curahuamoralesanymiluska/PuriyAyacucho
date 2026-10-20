package com.example.puriyayacucho

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puriyayacucho.adapters.FavoritoAdapter
import com.example.puriyayacucho.data.AppDatabase
import com.example.puriyayacucho.data.FavoritoEntity
import com.example.puriyayacucho.data.LugarTuristicoFactory
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private var rvFavoritos: RecyclerView? = null
    private var emptyState: View? = null
    private var tvCount: TextView? = null
    private var adapter: FavoritoAdapter? = null
    private var userEmail: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("usuario_sesion", Context.MODE_PRIVATE)
        userEmail = sharedPref.getString("correo_usuario", "") ?: ""

        rvFavoritos = view.findViewById(R.id.rvFavoritos)
        emptyState = view.findViewById(R.id.emptyStateFavoritos)
        tvCount = view.findViewById(R.id.tvCountFavoritos)

        setupRecyclerView()
        loadFavorites()
    }

    private fun setupRecyclerView() {
        adapter = FavoritoAdapter(
            favoritos = emptyList(),
            onDeleteClick = { favorito -> deleteFavorite(favorito) },
            onViewClick = { favorito -> navigateToDetails(favorito) }
        )
        rvFavoritos?.layoutManager = LinearLayoutManager(context)
        rvFavoritos?.adapter = adapter
    }

    private fun loadFavorites() {
        if (userEmail.isEmpty()) return

        lifecycleScope.launch {
            val lista = AppDatabase.getInstance(requireContext())
                .favoritoDao().obtenerPorUsuario(userEmail)

            if (lista.isEmpty()) {
                rvFavoritos?.visibility = View.GONE
                emptyState?.visibility = View.VISIBLE
                tvCount?.text = "No hay lugares guardados"
            } else {
                rvFavoritos?.visibility = View.VISIBLE
                emptyState?.visibility = View.GONE
                tvCount?.text = "${lista.size} lugares guardados"
                adapter?.updateList(lista)
            }
        }
    }

    private fun deleteFavorite(favorito: FavoritoEntity) {
        lifecycleScope.launch {
            AppDatabase.getInstance(requireContext())
                .favoritoDao().eliminar(favorito)
            Toast.makeText(context, "${favorito.nombre} eliminado", Toast.LENGTH_SHORT).show()
            loadFavorites()
        }
    }

    private fun navigateToDetails(favorito: FavoritoEntity) {
        val lugarCompleto = LugarTuristicoFactory.obtenerLugaresTuristicosAyacucho(requireContext())
            .find { it.id == favorito.lugarId }

        if (lugarCompleto != null) {
            val intent = Intent(context, PlaceDetailsActivity::class.java).apply {
                putExtra("placeId", lugarCompleto.id)
                putExtra("latitude", lugarCompleto.coordenadas.first)
                putExtra("longitude", lugarCompleto.coordenadas.second)
                putExtra("placeName", lugarCompleto.nombre)
                putExtra("description", lugarCompleto.descripcion)
                putExtra("category", lugarCompleto.tipo)
                putExtra("imageRes", lugarCompleto.imagen as Int)
                putExtra("videoUrl", lugarCompleto.videoUrl)
                putExtra("hours", lugarCompleto.horario)
                putExtra("galleryImages", ArrayList(lugarCompleto.galeria.map { it as Int }))
            }
            startActivity(intent)
        }
    }
}
