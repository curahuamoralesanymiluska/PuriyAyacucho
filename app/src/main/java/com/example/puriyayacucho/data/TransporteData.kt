package com.example.puriyayacucho.data

import com.example.puriyayacucho.guia.Punto

enum class TipoTransporte { COMBI, MICRO, COLECTIVO, BUS, TAXI }

data class Paradero(
    val id: Int,
    val nombre: String,
    val punto: Punto,
    val lineas: List<Int>
)

data class LineaTransporte(
    val id: Int,
    val nombre: String,
    val tipo: TipoTransporte,
    /** Puntos por donde pasa la línea, en orden. Se puede recorrer en ambos sentidos. */
    val recorrido: List<Punto>,
    val tarifa: Double,
    val velocidadKmh: Double,
    val horario: String,
    val nota: String = "",
    val fuente: String = "",
    /**
     * true para los micros de la ciudad: paran donde les hagas la señal, así que se suben y
     * se bajan en el punto de su recorrido más cercano (no hace falta un paradero fijo).
     */
    val paradaLibre: Boolean = false
)

/**
 * Transporte desde Huamanga (datos revisados en octubre de 2026):
 *
 * - Huamanga → Wari → Quinua: las combis y los autos colectivos salen del **ex paradero de
 *   Huanta**, en la Av. Salvador Cavero (S/ 3.50 - 4). Para Wari se le pide al chofer bajar en
 *   las ruinas. Después de las 5 PM es difícil encontrar combi de regreso.
 *   Fuentes: confirmado por la autora del proyecto; OpenStreetMap ("Combis para Wari y Quinua"
 *   y "Autos para Quinua", vía mapcarta.com/N14135484448 y mapcarta.com/N13794945901);
 *   blueskylimit.com (tarifa).
 * - Huamanga → Huanta: las combis salen del **Parque Magdalena** (S/ 4 - 6 según la hora,
 *   cerca de 1 h). También hay autos colectivos (S/ 6 - 8, unos 45 min).
 *   Fuentes: confirmado por la autora del proyecto; infoayacucho.com (precios y tiempos).
 * - Huamanga → Vilcashuamán (107 km, unas 2 h): no hay transporte público regular;
 *   se va en taxi desde la Plaza Mayor (unos S/ 40 por tramo) o con agencia.
 *   Fuente: rutasviajesperu.com.
 * - Micros de la ciudad: ver [lineasMicro]. Se agregan con `paradaLibre = true`.
 *
 * TODO: el Parque Magdalena, Huanta y el trazado de las combis son aproximados; conviene
 * grabarlos con GPS en campo. (Con internet, la app dibuja el camino real por la carretera.)
 */
object TransporteFactory {

    /** Ex paradero de Huanta (Av. Salvador Cavero): hoy salen de aquí las combis a Wari y Quinua. */
    private val EX_PARADERO_HUANTA = Punto(-13.158553, -74.217839)
    /** Parque Magdalena (aprox., junto al templo y el mercado de la Magdalena): combis a Huanta. */
    private val PARQUE_MAGDALENA = Punto(-13.15790, -74.22130)
    /** Huanta, cerca de la plaza (aprox.). */
    private val HUANTA = Punto(-12.93950, -74.24700)

    /**
     * Micros de la ciudad de Huamanga. Para agregar uno, copia el ejemplo y pon los puntos por
     * donde pasa, en orden (esquinas o lugares conocidos, cada 300-500 m).
     *
     * LineaTransporte(
     *     id = 10,
     *     nombre = "Micro línea X (Barrio A – Barrio B)",
     *     tipo = TipoTransporte.MICRO,
     *     recorrido = listOf(Punto(lat, lng), Punto(lat, lng), ...),
     *     tarifa = 1.0,
     *     velocidadKmh = 15.0,
     *     horario = "5:30 AM – 10:00 PM",
     *     nota = "Micro de color ... con letrero ...; hazle la señal en la esquina",
     *     fuente = "...",
     *     paradaLibre = true
     * )
     */
    val lineasMicro: List<LineaTransporte> = emptyList()

    val lineas: List<LineaTransporte> = listOf(
        LineaTransporte(
            id = 1,
            nombre = "Combi Huamanga – Quinua (pasa por Wari)",
            tipo = TipoTransporte.COMBI,
            recorrido = listOf(
                EX_PARADERO_HUANTA,        // Ex paradero de Huanta (Av. Salvador Cavero)
                Punto(-13.1350, -74.2120),
                Punto(-13.1100, -74.2050),
                Punto(-13.0850, -74.2010),
                Punto(-13.0610, -74.1985), // Ruinas de Wari
                Punto(-13.0560, -74.1700),
                Punto(-13.0505, -74.1398)  // Quinua (mercado)
            ),
            tarifa = 4.0,
            velocidadKmh = 35.0,
            horario = "Mañana y tarde · difícil después de las 5:00 PM",
            nota = "Sale del ex paradero de Huanta (Av. Salvador Cavero); también hay autos colectivos. " +
                "Pide al chofer que te deje en las ruinas de Wari",
            fuente = "Confirmado por la autora del proyecto; OpenStreetMap; blueskylimit.com"
        ),
        LineaTransporte(
            id = 2,
            nombre = "Taxi a Vilcashuamán (desde la Plaza Mayor)",
            tipo = TipoTransporte.TAXI,
            recorrido = listOf(
                Punto(-13.1603, -74.2252), // Plaza Mayor de Huamanga
                Punto(-13.2500, -74.1500),
                Punto(-13.4000, -74.0800),
                Punto(-13.5500, -74.0500),
                Punto(-13.7104, -74.0467)  // Vilcashuamán
            ),
            tarifa = 40.0,
            // La carretera real mide ~107 km y toma ~2 h; el trazado aquí es aproximado
            // (~66 km en línea), por eso la velocidad se ajusta para que el tiempo sea ~2 h.
            velocidadKmh = 33.0,
            horario = "A coordinar con el taxista",
            nota = "No hay transporte público regular; se puede acordar que el taxi espere",
            fuente = "rutasviajesperu.com"
        ),
        LineaTransporte(
            id = 3,
            nombre = "Combi Huamanga – Huanta",
            tipo = TipoTransporte.COMBI,
            recorrido = listOf(PARQUE_MAGDALENA, HUANTA),
            tarifa = 5.0,
            // La carretera mide más que la recta (~25 km); la velocidad se ajusta para que el
            // viaje dure cerca de 1 hora, como indican las fuentes.
            velocidadKmh = 27.0,
            horario = "Todo el día · más caro desde las 6:30 PM",
            nota = "Sale del Parque Magdalena · S/ 4 – 6 según la hora. " +
                "También hay autos colectivos (S/ 6 – 8, unos 45 min)",
            fuente = "Confirmado por la autora del proyecto; infoayacucho.com"
        )
    ) + lineasMicro

    val paraderos: List<Paradero> = listOf(
        Paradero(1, "Ex paradero de Huanta (combis a Wari y Quinua)", EX_PARADERO_HUANTA, listOf(1)),
        Paradero(2, "Ruinas de Wari (bajar a pedido)", Punto(-13.0610, -74.1985), listOf(1)),
        Paradero(3, "Quinua (junto al mercado)", Punto(-13.0505, -74.1398), listOf(1)),
        Paradero(4, "Plaza Mayor (taxis)", Punto(-13.1603, -74.2252), listOf(2)),
        Paradero(5, "Vilcashuamán", Punto(-13.7104, -74.0467), listOf(2)),
        Paradero(6, "Parque Magdalena (combis a Huanta)", PARQUE_MAGDALENA, listOf(3)),
        Paradero(7, "Huanta (llegada de las combis)", HUANTA, listOf(3))
    )
}
