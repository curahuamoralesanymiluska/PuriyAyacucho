package com.example.puriyayacucho.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.puriyayacucho.R
import com.example.puriyayacucho.data.Comodidad
import com.example.puriyayacucho.data.CategoriaLugar
import com.example.puriyayacucho.data.LugarTuristico
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/**
 * Hoja de filtros por modulo.
 *
 * Recibe la categoria activa, la lista completa de lugares de esa categoria
 * y el set de comodidades actualmente seleccionadas. Al tocar "Ver N lugares"
 * llama a [onAplicar] con el nuevo set.
 */
class FiltrosBottomSheet(
    private val categoria: CategoriaLugar,
    private val todosLosLugares: List<LugarTuristico>,
    private val seleccionadas: Set<Comodidad>,
    private val onAplicar: (Set<Comodidad>) -> Unit
) : BottomSheetDialogFragment(R.layout.dialog_filtros) {

    private val activas = seleccionadas.toMutableSet()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnVer = view.findViewById<MaterialButton>(R.id.btnVerLugares)
        val tvLimpiar = view.findViewById<TextView>(R.id.tvLimpiarFiltros)

        // Comodidades que pertenecen a esta categoria, con al menos 1 lugar
        val comodidades = Comodidad.paraCategoria(categoria).filter { com ->
            todosLosLugares.any { lugar ->
                lugar.itemFiltrable?.comodidades?.contains(com) == true
            }
        }

        // Construir chips de comodidades con FlexboxLayoutManager si disponible,
        // sino usar un ChipGroup en el layout
        val rvComodidades = view.findViewById<RecyclerView>(R.id.rvComodidadesFiltro)

        // Usamos un ChipGroup dinamico dentro del RecyclerView container
        // Para simplicidad y sin librerias externas, inflamos los chips directamente
        // en un ChipGroup que ponemos en el slot del RecyclerView
        val parent = rvComodidades.parent as? LinearLayout
        val chipGroup = ChipGroup(requireContext()).apply {
            isSingleSelection = false
            chipSpacingHorizontal = 4
            chipSpacingVertical = 4
            setPadding(
                16,
                0,
                16,
                0
            )
        }

        comodidades.forEach { com ->
            val count = todosLosLugares.count { lugar ->
                lugar.itemFiltrable?.comodidades?.contains(com) == true
            }
            val etiqueta = getString(com.etiqueta)
            val chip = Chip(requireContext()).apply {
                text = "$etiqueta ($count)"
                isCheckable = true
                isChecked = activas.contains(com)
                isEnabled = count > 0
                setChipBackgroundColorResource(R.color.sel_puriy_chip_fondo)
                setChipStrokeColorResource(R.color.sel_puriy_chip_borde)
                chipStrokeWidth = 2f
                isCheckedIconVisible = false
                setTextColor(resources.getColorStateList(R.color.sel_puriy_chip_texto, null))
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) activas.add(com) else activas.remove(com)
                    actualizarBoton(btnVer)
                }
            }
            chipGroup.addView(chip)
        }

        // Insertar el ChipGroup en lugar del RecyclerView
        val idx = (rvComodidades.parent as LinearLayout).indexOfChild(rvComodidades)
        (rvComodidades.parent as LinearLayout).removeView(rvComodidades)
        (parent ?: (view as LinearLayout)).addView(chipGroup, idx)

        actualizarBoton(btnVer)

        tvLimpiar.setOnClickListener {
            activas.clear()
            for (i in 0 until chipGroup.childCount) {
                (chipGroup.getChildAt(i) as? Chip)?.isChecked = false
            }
            actualizarBoton(btnVer)
        }

        btnVer.setOnClickListener {
            onAplicar(activas.toSet())
            dismiss()
        }
    }

    private fun actualizarBoton(btn: MaterialButton) {
        val n = todosLosLugares.count { lugar ->
            activas.isEmpty() || activas.all { com ->
                lugar.itemFiltrable?.comodidades?.contains(com) == true
            }
        }
        btn.text = if (activas.isEmpty()) "Ver todos los lugares" else "Ver $n lugares"
    }
}
