package com.example.puriyayacucho

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puriyayacucho.adapters.LugarNaturalAdapter
import com.example.puriyayacucho.adapters.LugarTuristicoAdapter
import com.example.puriyayacucho.adapters.MuseoAdapter
import com.example.puriyayacucho.adapters.RestauranteAdapter
import com.example.puriyayacucho.data.BarFactory
import com.example.puriyayacucho.data.CafeFactory
import com.example.puriyayacucho.data.CategoriaLugar
import com.example.puriyayacucho.data.Comodidad
import com.example.puriyayacucho.data.HospedajeFactory
import com.example.puriyayacucho.data.LugarMapper
import com.example.puriyayacucho.data.LugarNaturalFactory
import com.example.puriyayacucho.data.LugarTuristico
import com.example.puriyayacucho.data.LugarTuristicoFactory
import com.example.puriyayacucho.data.MuseoFactory
import com.example.puriyayacucho.data.RestauranteFactory
import com.example.puriyayacucho.data.toLugarTuristico
import com.example.puriyayacucho.ui.FiltrosBottomSheet
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip

/** Categorias que admiten filtrado por comodidades */
private val CATEGORIAS_FILTRABLES = setOf(
    CategoriaLugar.BAR,
    CategoriaLugar.RESTAURANTE,
    CategoriaLugar.HOTEL,
    CategoriaLugar.AIRBNB,
    CategoriaLugar.CAFE
)

class ExploreFragment : Fragment() {

    private lateinit var rvExplorePlaces: RecyclerView
    private lateinit var barraFiltros: View
    private lateinit var tvContador: TextView
    private lateinit var tvBurbuja: TextView
    private lateinit var btnAbrirFiltros: MaterialButton
    private lateinit var llChipsRapidos: LinearLayout

    /** Categoria actualmente visible */
    private var categoriaActual: CategoriaLugar? = null

    /** Filtros activos por categoria, persisten mientras el fragment esta vivo */
    private val filtrosPorCategoria = mutableMapOf<CategoriaLugar, Set<Comodidad>>()

    /** Lista sin filtrar de la categoria actual */
    private var listaCompleta: List<LugarTuristico> = emptyList()

    companion object {
        const val ARG_CATEGORIA = "categoria"

        fun nuevaInstancia(categoria: String?): ExploreFragment = ExploreFragment().apply {
            arguments = Bundle().apply { putString(ARG_CATEGORIA, categoria) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvExplorePlaces = view.findViewById(R.id.rvExplorePlaces)
        rvExplorePlaces.layoutManager = LinearLayoutManager(context)

        barraFiltros = view.findViewById(R.id.barraFiltros)
        tvContador = view.findViewById(R.id.tvContadorLugares)
        tvBurbuja = view.findViewById(R.id.tvBurbujaFiltros)
        btnAbrirFiltros = view.findViewById(R.id.btnAbrirFiltros)
        llChipsRapidos = view.findViewById(R.id.llChipsRapidos)

        // Chips de categorias
        val chips = mapOf(
            R.id.chipTemplos to CategoriaLugar.TEMPLO,
            R.id.chipMuseos to CategoriaLugar.MUSEO,
            R.id.chipGastro to CategoriaLugar.RESTAURANTE,
            R.id.chipNaturaleza to CategoriaLugar.NATURALEZA,
            R.id.chipHoteles to CategoriaLugar.HOTEL,
            R.id.chipAirbnb to CategoriaLugar.AIRBNB,
            R.id.chipBares to CategoriaLugar.BAR,
            R.id.chipRecreacion to CategoriaLugar.RECREACION,
            R.id.chipCafes to CategoriaLugar.CAFE
        )
        chips.forEach { (chipId, categoria) ->
            view.findViewById<Chip>(chipId)?.setOnClickListener { mostrarCategoria(categoria) }
        }

        // Airbnb externo
        view.findViewById<View>(R.id.btnBuscarAirbnb)?.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.airbnb.com/s/Ayacucho--Peru/homes")))
            } catch (e: Exception) {
                Toast.makeText(requireContext(), R.string.route_maps_error, Toast.LENGTH_SHORT).show()
            }
        }

        // Boton filtros abre la hoja de la categoria actual
        btnAbrirFiltros.setOnClickListener { abrirHojaFiltros() }

        // Inicio
        val categoriaInicial = CategoriaLugar.desdeClave(arguments?.getString(ARG_CATEGORIA))
        val etSearch = activity?.findViewById<EditText>(R.id.etSearch)
        val query = etSearch?.text?.toString() ?: ""
        when {
            categoriaInicial != null -> mostrarCategoria(categoriaInicial)
            query.isNotEmpty() -> filtrarPorNombre(query)
            else -> resetearFiltros()
        }
    }

    /** Muestra la lista de una categoria con filtros aplicados */
    fun mostrarCategoria(categoria: CategoriaLugar) {
        if (!::rvExplorePlaces.isInitialized) return
        val ctx = requireContext()
        categoriaActual = categoria

        // Airbnb externo
        view?.findViewById<View>(R.id.btnBuscarAirbnb)?.visibility =
            if (categoria == CategoriaLugar.AIRBNB) View.VISIBLE else View.GONE

        // Construir lista completa de LugarTuristico para esta categoria
        listaCompleta = when (categoria) {
            CategoriaLugar.TEMPLO -> LugarTuristicoFactory.obtenerLugaresTuristicosAyacucho(ctx)
            CategoriaLugar.MUSEO -> MuseoFactory.obtenerMuseosAyacucho(ctx).map { it.toLugarTuristico(ctx) }
            CategoriaLugar.RESTAURANTE -> RestauranteFactory.obtenerRestaurantesAyacucho(ctx).map { it.toLugarTuristico(ctx) }
            CategoriaLugar.NATURALEZA -> LugarNaturalFactory.obtenerLugaresNaturalesAyacucho(ctx).map { it.toLugarTuristico(ctx) }
            CategoriaLugar.HOTEL -> HospedajeFactory.obtenerHoteles().map { it.toLugarTuristico(ctx) }
            CategoriaLugar.AIRBNB -> HospedajeFactory.obtenerAirbnb().map { it.toLugarTuristico(ctx) }
            CategoriaLugar.BAR -> BarFactory.obtenerBares().map { it.toLugarTuristico(ctx) }
            CategoriaLugar.RECREACION -> LugarMapper.deCategoria(ctx, CategoriaLugar.RECREACION)
            CategoriaLugar.CAFE -> CafeFactory.obtenerCafes().map { it.toLugarTuristico(ctx) }
        }

        // Mostrar / ocultar barra de filtros
        val filtrable = categoria in CATEGORIAS_FILTRABLES &&
            Comodidad.paraCategoria(categoria).any { com ->
                listaCompleta.any { it.itemFiltrable?.comodidades?.contains(com) == true }
            }
        if (filtrable) {
            barraFiltros.visibility = View.VISIBLE
            actualizarChipsRapidos(categoria)
        } else {
            barraFiltros.visibility = View.GONE
        }

        aplicarFiltros()
    }

    /** Aplica los filtros activos de la categoria actual y actualiza el adapter */
    private fun aplicarFiltros() {
        val cat = categoriaActual ?: return
        val ctx = requireContext()
        val filtrosActivos = filtrosPorCategoria[cat] ?: emptySet()

        val listaFiltrada = if (filtrosActivos.isEmpty()) {
            listaCompleta
        } else {
            listaCompleta.filter { lugar ->
                filtrosActivos.all { com ->
                    lugar.itemFiltrable?.comodidades?.contains(com) == true
                }
            }
        }

        // Actualizar contador y burbuja
        if (barraFiltros.visibility == View.VISIBLE) {
            val n = listaFiltrada.size
            tvContador.text = "$n ${if (n == 1) "lugar" else "lugares"}"
            if (filtrosActivos.isEmpty()) {
                tvBurbuja.visibility = View.GONE
            } else {
                tvBurbuja.text = filtrosActivos.size.toString()
                tvBurbuja.visibility = View.VISIBLE
            }
        }

        // Mostrar lista o mensaje de vacio
        if (listaFiltrada.isEmpty() && filtrosActivos.isNotEmpty()) {
            rvExplorePlaces.adapter = null
            // TODO: mostrar vista de "sin resultados" con boton limpiar
            Toast.makeText(
                ctx,
                "Sin lugares con esos filtros. Prueba quitando alguno.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Para categorias con adaptadores especializados, si no hay filtros usamos el adapter original
        rvExplorePlaces.adapter = when {
            cat == CategoriaLugar.TEMPLO && filtrosActivos.isEmpty() ->
                LugarTuristicoAdapter(listaCompleta)
            cat == CategoriaLugar.MUSEO && filtrosActivos.isEmpty() ->
                MuseoAdapter(MuseoFactory.obtenerMuseosAyacucho(ctx))
            cat == CategoriaLugar.RESTAURANTE && filtrosActivos.isEmpty() ->
                RestauranteAdapter(RestauranteFactory.obtenerRestaurantesAyacucho(ctx))
            cat == CategoriaLugar.NATURALEZA && filtrosActivos.isEmpty() ->
                LugarNaturalAdapter(LugarNaturalFactory.obtenerLugaresNaturalesAyacucho(ctx))
            cat == CategoriaLugar.RECREACION ->
                LugarTuristicoAdapter(listaFiltrada)
            else -> construirAdapterFiltrado(cat, listaFiltrada)
        }
    }

    private fun construirAdapterFiltrado(cat: CategoriaLugar, lista: List<LugarTuristico>): RecyclerView.Adapter<*> {
        return LugarTuristicoAdapter(lista)
    }

    /** Actualiza la fila de chips rapidos para la categoria */
    private fun actualizarChipsRapidos(categoria: CategoriaLugar) {
        llChipsRapidos.removeAllViews()
        val filtrosActivos = filtrosPorCategoria[categoria] ?: emptySet()

        val comodidades = Comodidad.paraCategoria(categoria).filter { com ->
            listaCompleta.any { it.itemFiltrable?.comodidades?.contains(com) == true }
        }

        comodidades.forEach { com ->
            val etiqueta = getString(com.etiqueta)
            val chip = Chip(requireContext()).apply {
                text = etiqueta
                isCheckable = true
                isChecked = filtrosActivos.contains(com)
                setChipBackgroundColorResource(R.color.sel_puriy_chip_fondo)
                setChipStrokeColorResource(R.color.sel_puriy_chip_borde)
                isCheckedIconVisible = false
                chipStrokeWidth = 1f
                setTextColor(resources.getColorStateList(R.color.sel_puriy_chip_texto, null))
                textSize = 12f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { marginEnd = 8 }
                setOnCheckedChangeListener { _, isChecked ->
                    val actuales = filtrosPorCategoria[categoria]?.toMutableSet() ?: mutableSetOf()
                    if (isChecked) actuales.add(com) else actuales.remove(com)
                    filtrosPorCategoria[categoria] = actuales
                    aplicarFiltros()
                    // Sincronizar burbuja
                    val total = actuales.size
                    if (total == 0) tvBurbuja.visibility = View.GONE
                    else { tvBurbuja.text = total.toString(); tvBurbuja.visibility = View.VISIBLE }
                }
            }
            llChipsRapidos.addView(chip)
        }
    }

    /** Abre la hoja de filtros de la categoria actual */
    private fun abrirHojaFiltros() {
        val cat = categoriaActual ?: return
        val actuales = filtrosPorCategoria[cat] ?: emptySet()
        FiltrosBottomSheet(cat, listaCompleta, actuales) { nuevos ->
            filtrosPorCategoria[cat] = nuevos
            // Re-sincronizar chips rapidos
            actualizarChipsRapidos(cat)
            aplicarFiltros()
        }.show(childFragmentManager, "filtros")
    }

    /** Busqueda global en todas las categorias */
    fun filtrarPorNombre(query: String) {
        if (!::rvExplorePlaces.isInitialized) return
        categoriaActual = null
        barraFiltros.visibility = View.GONE
        val filtrados = LugarMapper.todos(requireContext())
            .map { it.lugar }
            .filter {
                it.nombre.contains(query, ignoreCase = true) ||
                    it.tipo.contains(query, ignoreCase = true) ||
                    it.descripcion.contains(query, ignoreCase = true)
            }
        rvExplorePlaces.adapter = LugarTuristicoAdapter(filtrados)
    }

    fun resetearFiltros() {
        if (!::rvExplorePlaces.isInitialized) return
        categoriaActual = null
        barraFiltros.visibility = View.GONE
        view?.findViewById<View>(R.id.btnBuscarAirbnb)?.visibility = View.GONE
        rvExplorePlaces.adapter = LugarTuristicoAdapter(
            LugarTuristicoFactory.obtenerLugaresTuristicosAyacucho(requireContext())
        )
    }
}
