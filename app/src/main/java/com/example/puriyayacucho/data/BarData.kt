package com.example.puriyayacucho.data

import com.example.puriyayacucho.R
import com.example.puriyayacucho.guia.UbicacionHelper

data class Bar(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val estilo: String,
    val horario: String,
    val precio: String,
    /** Puntuación pública (Tripadvisor, de 1 a 5). null si no hay. */
    val rating: Float? = null,
    val ubicacion: String,
    val telefono: String = "N/A",
    val imagen: Any = R.drawable.ph_bar,
    val galeria: List<Any> = emptyList(),
    val coordenadas: Pair<Double, Double>,
    /** true si la coordenada se estimó a partir de la dirección (verificar en el mapa). */
    val coordenadasAproximadas: Boolean = false,
    val ubicacionConfirmada: Boolean = true,
    val videoUrl: String = "",
    val comodidades: Set<Comodidad> = emptySet(),
    val fuente: String = ""
)

/**
 * Bares REALES de Huamanga (consultados en octubre de 2026).
 */
object BarFactory {
    fun obtenerBares(): List<Bar> = listOf(
        Bar(
            id = 8001,
            nombre = "Magia Negra Taberna Trattoria",
            descripcion = "Taberna con pizzas y cerveza artesanal; uno de los bares mejor valorados de la ciudad.",
            estilo = "Brew pub · pizzería",
            horario = "Lun a sáb · 7:00 PM - 12:00 AM",
            precio = "Precio medio",
            rating = 4.7f,
            ubicacion = "Jr. Bellido 349, Huamanga",
            telefono = "(066) 287644",
            coordenadas = Pair(-13.1602, -74.21929),
            comodidades = setOf(Comodidad.CERVEZA_ARTESANAL),
            fuente = "tripadvisor.com (d15298122), openalfa.com"
        ),
        Bar(
            id = 8002,
            nombre = "Paradise Rooftop",
            descripcion = "Bar en azotea con cócteles, sushi y mariscos, en San Juan Bautista.",
            estilo = "Bar en azotea",
            horario = "Mar · 3:00 - 11:00 PM · Mié a dom · 4:00 - 11:00 PM (lunes cerrado)",
            precio = "Cócteles y comida",
            rating = 5.0f,
            ubicacion = "Av. Arenales 310, San Juan Bautista",
            coordenadas = Pair(-13.170656, -74.202965),
            comodidades = setOf(Comodidad.TERRAZA),
            fuente = "tripadvisor.com (d34348157)"
        ),
        Bar(
            id = 8003,
            nombre = "Yaku Craft Beer",
            descripcion = "Bar con cerveza de elaboración propia, balcón y música en vivo.",
            estilo = "Cervecería artesanal · música en vivo",
            horario = "Noches",
            precio = "Hamburguesas S/ 15 - 20",
            ubicacion = "Jr. 9 de Diciembre 213, Huamanga",
            coordenadas = Pair(-13.1586, -74.2260),
            coordenadasAproximadas = true,
            comodidades = setOf(Comodidad.CERVEZA_ARTESANAL, Comodidad.MUSICA_AYACUCHANA, Comodidad.MUSICA_EN_VIVO),
            fuente = "Autora del proyecto + Lonely Planet"
        ),
        Bar(
            id = 8004,
            nombre = "Babieca Karaoke Café",
            descripcion = "Café karaoke en el centro histórico.",
            estilo = "Karaoke",
            horario = "Lun a sáb · 8:00 AM - 12:00 AM",
            precio = "Consultar",
            ubicacion = "Jr. Asamblea 187, Int. Pasaje izquierdo, Huamanga",
            coordenadas = Pair(-13.1594, -74.2249),
            coordenadasAproximadas = true,
            comodidades = setOf(Comodidad.KARAOKE),
            fuente = "openalfa.com (OpenStreetMap)"
        ),
        Bar(
            id = 8005,
            nombre = "Casa Grill Resto-Bar",
            descripcion = "Resto-bar de parrillas en el Jr. Tres Máscaras.",
            estilo = "Resto-bar · parrillas",
            horario = "Consultar",
            precio = "Consultar",
            ubicacion = "Jr. Tres Máscaras 390, Huamanga",
            coordenadas = Pair(-13.1632, -74.2228),
            coordenadasAproximadas = true,
            fuente = "wanderlog.com"
        ),
        Bar(
            id = 8006,
            nombre = "El Niño",
            descripcion = "Restaurante-bar con música ayacuchana en vivo, parrillas y pizzas (solo de noche).",
            estilo = "Restaurante-bar · música andina",
            horario = "Por confirmar",
            precio = "Por confirmar",
            ubicacion = "Jr. 9 de Diciembre 205, Plazoleta Luis Carranza, Huamanga",
            telefono = "(066) 314537",
            coordenadas = Pair(-13.1587, -74.2259),
            coordenadasAproximadas = true,
            comodidades = setOf(Comodidad.MUSICA_AYACUCHANA, Comodidad.MUSICA_EN_VIVO),
            fuente = "Autora del proyecto + Tripadvisor"
        ),
        Bar(
            id = 8007,
            nombre = "Voces y Cuerdas",
            descripcion = "Club cultural de música andina ayacuchana en vivo.",
            estilo = "Música andina en vivo",
            horario = "Por confirmar",
            precio = "Por confirmar",
            ubicacion = "Dirección por confirmar",
            coordenadas = UbicacionHelper.PLAZA_MAYOR.let { Pair(it.lat, it.lng) },
            ubicacionConfirmada = false,
            comodidades = setOf(Comodidad.MUSICA_AYACUCHANA, Comodidad.MUSICA_EN_VIVO),
            fuente = "Autora del proyecto + facebook.com/Voces-y-Cuerdas-Musica-Andina"
        ),
        Bar(
            id = 8008,
            nombre = "Pitahaya",
            descripcion = "Discoteca y bar con rock en vivo.",
            estilo = "Discoteca · rock en vivo",
            horario = "Por confirmar",
            precio = "Por confirmar",
            ubicacion = "Dirección por confirmar",
            coordenadas = UbicacionHelper.PLAZA_MAYOR.let { Pair(it.lat, it.lng) },
            ubicacionConfirmada = false,
            comodidades = setOf(Comodidad.ROCK_EN_VIVO, Comodidad.MUSICA_EN_VIVO, Comodidad.DISCOTECA),
            fuente = "Autora del proyecto + facebook.com/PitahayaAyacucho"
        ),
        Bar(
            id = 8009,
            nombre = "Golden Rock",
            descripcion = "Bar con rock en vivo.",
            estilo = "Rock en vivo",
            horario = "Por confirmar",
            precio = "Por confirmar",
            ubicacion = "Dirección por confirmar",
            coordenadas = UbicacionHelper.PLAZA_MAYOR.let { Pair(it.lat, it.lng) },
            ubicacionConfirmada = false,
            comodidades = setOf(Comodidad.ROCK_EN_VIVO, Comodidad.MUSICA_EN_VIVO),
            fuente = "Autora del proyecto + restaurantguru.com/Golden-Rock-Cafe-Ayacucho"
        ),
        Bar(
            id = 8010,
            nombre = "Sol y Luna",
            descripcion = "Karaoke y bar con rock en vivo.",
            estilo = "Karaoke · rock en vivo",
            horario = "Por confirmar",
            precio = "Por confirmar",
            ubicacion = "Dirección por confirmar",
            coordenadas = UbicacionHelper.PLAZA_MAYOR.let { Pair(it.lat, it.lng) },
            ubicacionConfirmada = false,
            comodidades = setOf(Comodidad.ROCK_EN_VIVO, Comodidad.MUSICA_EN_VIVO, Comodidad.KARAOKE),
            fuente = "Autora del proyecto + facebook.com/KaraokeSolyLunaOficial"
        ),
        Bar(
            id = 8011,
            nombre = "Templo Bar",
            descripcion = "Bar con conciertos y tributos a bandas de rock.",
            estilo = "Rock en vivo",
            horario = "Por confirmar",
            precio = "Por confirmar",
            ubicacion = "Jr. Garcilaso de la Vega 393, Huamanga",
            coordenadas = UbicacionHelper.PLAZA_MAYOR.let { Pair(it.lat, it.lng) },
            coordenadasAproximadas = true,
            ubicacionConfirmada = false, // Necesita coordenadas reales
            comodidades = setOf(Comodidad.ROCK_EN_VIVO),
            fuente = "vaope.com/ayacucho/rock"
        )
    )
}
