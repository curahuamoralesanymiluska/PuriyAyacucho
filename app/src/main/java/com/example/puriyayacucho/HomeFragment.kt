package com.example.puriyayacucho

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puriyayacucho.adapters.ComidaAdapter
import com.example.puriyayacucho.adapters.DescubreAyacuchoAdapter
import com.example.puriyayacucho.adapters.FestividadCalendarAdapter
import com.example.puriyayacucho.adapters.ModuloAdapter
import com.example.puriyayacucho.data.CategoriaLugar
import com.example.puriyayacucho.data.LugarMapper
import com.example.puriyayacucho.data.TipoReconstruccion
import com.example.puriyayacucho.ra.ReconstruccionView
import com.example.puriyayacucho.data.ComidaFactory
import com.example.puriyayacucho.data.FestividadFactory
import com.example.puriyayacucho.data.LugarNaturalFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

class HomeFragment : Fragment() {

    private var animacionHolograma: ValueAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupModulos(view)
        setupDescubreAyacucho(view)
        setupEventosProximos(view)
        setupComidaAyacuchana(view)
    }

    private fun setupModulos(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.rvModulos) ?: return
        rv.layoutManager = GridLayoutManager(context, 2)
        val ctx = requireContext()
        val cantidades = CategoriaLugar.entries.associateWith { cat ->
            runCatching { LugarMapper.deCategoria(ctx, cat).size }.getOrDefault(0)
        }
        rv.adapter = ModuloAdapter(CategoriaLugar.entries.toList(), cantidades) { categoria ->
            (activity as? MainActivity)?.abrirExplorar(categoria.clave)
        }
        rv.scheduleLayoutAnimation()

        view.findViewById<View>(R.id.cardGuiaPersonalizada)?.setOnClickListener {
            startActivity(Intent(ctx, GuiaActivity::class.java))
        }
        setupRealidadAumentada(view)
    }

    /** Tarjeta "Viaja al pasado": holograma animado de Wari y acceso a los sitios con RA. */
    private fun setupRealidadAumentada(view: View) {
        view.findViewById<View>(R.id.cardRealidadAumentada)?.setOnClickListener {
            startActivity(Intent(requireContext(), SitiosRAActivity::class.java))
        }
        val holograma = view.findViewById<ReconstruccionView>(R.id.hologramaInicio) ?: return
        holograma.tipo = TipoReconstruccion.RECINTO_WARI
        animacionHolograma?.cancel()
        animacionHolograma = ValueAnimator.ofFloat(0.15f, 1f).apply {
            duration = 2600
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { holograma.progreso = it.animatedValue as Float }
            start()
        }
    }

    override fun onDestroyView() {
        animacionHolograma?.cancel()
        animacionHolograma = null
        super.onDestroyView()
    }

    private fun setupDescubreAyacucho(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.rvDescubreAyacucho)
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val lugares = LugarNaturalFactory.obtenerLugaresNaturalesAyacucho(requireContext()).filter { it.imagen != "" }.take(6)
        rv.adapter = DescubreAyacuchoAdapter(lugares)
    }

    private fun setupEventosProximos(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.rvEventosProximos)
        rv.layoutManager = LinearLayoutManager(context)

        val hoy = Calendar.getInstance()
        val mesActual = hoy.get(Calendar.MONTH) + 1
        val diaActual = hoy.get(Calendar.DAY_OF_MONTH)

        // Se agregó requireContext() para obtener las festividades
        val todasLasFestividades = FestividadFactory.obtenerFestividadesAyacucho(requireContext())

        val proximosEventos = todasLasFestividades.filter {
            (it.mes > mesActual) || (it.mes == mesActual && it.dia >= diaActual)
        }.sortedWith(compareBy({ it.mes }, { it.dia }))
        .take(3)

        rv.adapter = FestividadCalendarAdapter(proximosEventos) { festividad ->
            val intent = Intent(context, PlaceDetailsActivity::class.java).apply {
                putExtra("placeName", festividad.nombre)
                putExtra("description", festividad.descripcion)
                putExtra("category", "Festividad")
                putExtra("hours", "Fecha: ${festividad.fecha}")
                putExtra("location", festividad.ubicacion)
                putExtra("recommendation", festividad.recomendacion)
                putExtra("imageUrl", festividad.imagenUrl)
                putExtra("videoUrl", festividad.videoUrl)
                putExtra("placeId", festividad.id + 5000)
                putStringArrayListExtra("galleryUrls", ArrayList(festividad.imagenesGaleria))
            }
            startActivity(intent)
        }
    }

    private fun setupComidaAyacuchana(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.rvComidaAyacuchana)
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val comidas = ComidaFactory.obtenerComidasAyacucho()
        rv.adapter = ComidaAdapter(comidas) { comida ->
            val etSearch = activity?.findViewById<EditText>(R.id.etSearch)
            if (etSearch != null) {
                etSearch.setText(comida.nombre)
            } else {
                val mainActivity = activity as? MainActivity
                val bottomNav = mainActivity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)
                bottomNav?.selectedItemId = R.id.navExplorar
            }
        }
    }
}
