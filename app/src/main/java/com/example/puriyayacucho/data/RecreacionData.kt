package com.example.puriyayacucho.data

import com.example.puriyayacucho.R

data class LugarRecreativo(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val actividades: List<String>,
    val horario: String,
    val precioEntrada: String,
    val distanciaDesdeHuamanga: String,
    val ubicacion: String,
    val imagen: Any = R.drawable.ph_recreacion,
    val galeria: List<Any> = emptyList(),
    val coordenadas: Pair<Double, Double>,
    /** true si la coordenada se estimó (verificar en el mapa). */
    val coordenadasAproximadas: Boolean = false,
    val videoUrl: String = "",
    val fuente: String = ""
)

/**
 * Lugares recreativos REALES de Huamanga (consultados en octubre de 2026).
 * TODO: verificar las coordenadas aproximadas en el mapa y los precios actuales.
 */
object RecreacionFactory {
    fun obtenerLugaresRecreativos(): List<LugarRecreativo> = listOf(
        LugarRecreativo(
            id = 1,
            nombre = "Mirador Intiwarkuna (Cerro La Picota)",
            descripcion = "Mirador natural sobre el cerro La Picota, en el centro poblado de Huascahura, con vista a toda la ciudad. " +
                "Tiene columpio extremo, canopy, cuatrimotos, puentes colgantes e instalaciones para fotos; de noche se ve Huamanga iluminada.",
            actividades = listOf("Columpio extremo S/ 20", "Canopy S/ 30", "Cuatrimoto S/ 10", "Puentes colgantes", "Fotos"),
            horario = "Todos los días · 9:00 AM - 5:00 PM (tours nocturnos 7:30 PM)",
            precioEntrada = "S/ 10",
            distanciaDesdeHuamanga = "20 - 25 min (unos 5 km al oeste)",
            ubicacion = "Cerro La Picota, Huascahura (vía Los Libertadores)",
            coordenadas = Pair(-13.1700, -74.2650),
            coordenadasAproximadas = true,
            fuente = "El Comercio, viajerotours.com, magicaltravelperu.com, academia.edu (estudio del centro ecoturístico La Picota)"
        ),
        LugarRecreativo(
            id = 2,
            nombre = "Mirador Turístico Acuchimay",
            descripcion = "Mirador de tres niveles en Carmen Alto, con el Cristo Blanco, piletas, glorieta y vista a la ciudad. " +
                "Es un paseo familiar: juegos infantiles, vuelo de cometas, artesanía y restaurantes de comida típica.",
            actividades = listOf("Juegos infantiles", "Vuelo de cometas", "Artesanía", "Comida típica", "Vista panorámica"),
            horario = "Todos los días · 6:00 AM - 9:00 PM",
            precioEntrada = "Libre",
            distanciaDesdeHuamanga = "10 min (Carmen Alto)",
            ubicacion = "Vía Libertadores 221, Carmen Alto",
            coordenadas = Pair(-13.1745, -74.2275),
            coordenadasAproximadas = true,
            fuente = "MINCETUR - Inventario de Recursos Turísticos (ficha 1114), infoayacucho.com"
        ),
        LugarRecreativo(
            id = 3,
            nombre = "Recreo Típico \"Tu Casa\"",
            descripcion = "Recreo campestre de comida típica ayacuchana para ir en familia o con amigos.",
            actividades = listOf("Comida típica", "Ambiente familiar", "Reservas"),
            horario = "Consultar",
            precioEntrada = "Platos a precio medio",
            distanciaDesdeHuamanga = "10 min (Urb. Jardín)",
            ubicacion = "Jr. Los Álamos 260, Urb. Jardín",
            coordenadas = Pair(-13.165407, -74.21451),
            fuente = "tripadvisor.com (d8598398)"
        )
    )
}
