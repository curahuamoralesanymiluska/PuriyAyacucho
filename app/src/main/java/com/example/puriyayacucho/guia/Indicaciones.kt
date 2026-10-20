package com.example.puriyayacucho.guia

import android.content.Context

enum class TipoManiobra {
    DEPART, TURN, STRAIGHT, UTURN, END_OF_ROAD, FORK, ROUNDABOUT, CONTINUE, ARRIVE
}

enum class ModificadorManiobra {
    LEFT, SLIGHT_LEFT, SHARP_LEFT, RIGHT, SLIGHT_RIGHT, SHARP_RIGHT, STRAIGHT, UTURN, NONE
}

data class Maniobra(
    val tipo: TipoManiobra,
    val modificador: ModificadorManiobra,
    val calle: String?,
    val punto: Punto,
    val distanciaM: Int,
    val salida: Int? = null
)

object Indicaciones {

    /** Devuelve la instrucción de la maniobra usando los recursos de idioma del sistema */
    fun texto(context: Context, maniobra: Maniobra, destino: String): String {
        val res = context.resources
        val calle = maniobra.calle?.takeIf { it.isNotBlank() }

        return when (maniobra.tipo) {
            TipoManiobra.DEPART -> {
                if (calle != null) "Sal por $calle" else "Inicia el recorrido"
            }
            TipoManiobra.TURN, TipoManiobra.END_OF_ROAD -> {
                val giro = when (maniobra.modificador) {
                    ModificadorManiobra.LEFT -> "Gira a la izquierda"
                    ModificadorManiobra.SLIGHT_LEFT -> "Gira levemente a la izquierda"
                    ModificadorManiobra.SHARP_LEFT -> "Gira bruscamente a la izquierda"
                    ModificadorManiobra.RIGHT -> "Gira a la derecha"
                    ModificadorManiobra.SLIGHT_RIGHT -> "Gira levemente a la derecha"
                    ModificadorManiobra.SHARP_RIGHT -> "Gira bruscamente a la derecha"
                    else -> "Gira"
                }
                val accion = if (maniobra.tipo == TipoManiobra.END_OF_ROAD) "Al final de la calle, ${giro.lowercase()}" else giro
                if (calle != null) "$accion en $calle" else accion
            }
            TipoManiobra.STRAIGHT -> {
                if (calle != null) "Sigue recto por $calle" else "Sigue recto"
            }
            TipoManiobra.UTURN -> {
                "Da la vuelta"
            }
            TipoManiobra.FORK -> {
                val lado = when (maniobra.modificador) {
                    ModificadorManiobra.LEFT, ModificadorManiobra.SLIGHT_LEFT, ModificadorManiobra.SHARP_LEFT -> "izquierda"
                    ModificadorManiobra.RIGHT, ModificadorManiobra.SLIGHT_RIGHT, ModificadorManiobra.SHARP_RIGHT -> "derecha"
                    else -> "tu vía"
                }
                if (calle != null) "En la bifurcación, mantente a la $lado hacia $calle" else "En la bifurcación, mantente a la $lado"
            }
            TipoManiobra.ROUNDABOUT -> {
                val sal = maniobra.salida?.toString()?.plus("ª ") ?: ""
                if (calle != null) "En la rotonda, toma la ${sal}salida hacia $calle" else "En la rotonda, toma la ${sal}salida"
            }
            TipoManiobra.CONTINUE -> {
                if (calle != null) "Sigue por $calle" else "Continúa"
            }
            TipoManiobra.ARRIVE -> {
                "Llegaste a $destino"
            }
        }
    }
}
