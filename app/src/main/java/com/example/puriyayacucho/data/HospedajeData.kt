package com.example.puriyayacucho.data

import com.example.puriyayacucho.R

enum class TipoHospedaje { HOTEL, AIRBNB }

data class Hospedaje(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val tipo: TipoHospedaje,
    /** Tipo visible: "Hotel", "Bed & breakfast", "Departamento entero"... */
    val subtipo: String,
    val estrellas: Int? = null,
    val precioNoche: String,
    /** Puntuación pública (Tripadvisor, de 1 a 5). null si no hay suficientes reseñas. */
    val rating: Float? = null,
    val ubicacion: String,
    val telefono: String = "N/A",
    val servicios: List<String> = emptyList(),
    val imagen: Any = R.drawable.ph_hotel,
    val galeria: List<Any> = emptyList(),
    val coordenadas: Pair<Double, Double>,
    /** true si la coordenada se estimó a partir de la dirección (verificar en el mapa). */
    val coordenadasAproximadas: Boolean = false,
    /** De dónde salió el dato (para poder verificarlo). */
    val fuente: String = "",
    val comodidades: Set<Comodidad> = emptySet(),
    val camas: Int? = null,
    val dormitorios: Int? = null,
    val banos: Int? = null,
    val huespedesMax: Int? = null
)

/**
 * Hospedajes REALES de Huamanga y Quinua (consultados en octubre de 2026).
 * Direcciones, servicios y coordenadas: fichas públicas de Expedia/Travelocity;
 * puntuaciones: Tripadvisor; teléfonos: Wikivoyage y ruta0.com.
 * Los precios cambian según la fecha, por eso se muestra "Consultar tarifa".
 * TODO: agregar fotos propias o con permiso de cada hotel.
 */
object HospedajeFactory {

    private const val CONSULTAR = "Consultar tarifa"

    fun obtenerHospedajes(): List<Hospedaje> = obtenerHoteles() + obtenerAirbnb()

    fun obtenerHoteles(): List<Hospedaje> = listOf(
        Hospedaje(
            id = 1,
            nombre = "DM Hoteles Ayacucho",
            descripcion = "Hotel en una casona del centro histórico, a una cuadra de la Plaza Mayor.",
            tipo = TipoHospedaje.HOTEL, subtipo = "Hotel",
            precioNoche = CONSULTAR, rating = 3.4f,
            ubicacion = "Jr. 9 de Diciembre 184, Huamanga",
            coordenadas = Pair(-13.15885, -74.22594),
            fuente = "travelocity.com (h23088839), tripadvisor.com"
        ),
        Hospedaje(
            id = 2,
            nombre = "Hotel Santa Rosa",
            descripcion = "Hotel tradicional a pocos pasos de la Plaza Mayor, con recepción las 24 horas.",
            tipo = TipoHospedaje.HOTEL, subtipo = "Hotel",
            precioNoche = CONSULTAR, rating = 3.8f,
            ubicacion = "Jr. Lima 166, Huamanga",
            servicios = listOf("Wi-Fi gratis", "Recepción 24 h", "Snack bar", "Ayuda con tours y entradas"),
            coordenadas = Pair(-13.16062, -74.22719),
            fuente = "expedia.com (h37233335), tripadvisor.com"
        ),
        Hospedaje(
            id = 3,
            nombre = "Hostal Tres Máscaras",
            descripcion = "Hostal amplio alrededor de un patio, a una cuadra al sureste de la Plaza Mayor.",
            tipo = TipoHospedaje.HOTEL, subtipo = "Hostal",
            precioNoche = CONSULTAR, rating = 3.8f,
            ubicacion = "Jr. Tres Máscaras 194, Huamanga",
            telefono = "(066) 312921",
            servicios = listOf("Wi-Fi", "Agua caliente", "Desayuno a pedido", "Patio"),
            coordenadas = Pair(-13.16268, -74.22444),
            fuente = "expedia.com (h37495181), Wikivoyage, tripadvisor.com"
        ),
        Hospedaje(
            id = 4,
            nombre = "Hotel Misky Samay",
            descripcion = "Hotel céntrico y tranquilo, a unas tres cuadras de la Plaza Mayor.",
            tipo = TipoHospedaje.HOTEL, subtipo = "Hotel",
            precioNoche = CONSULTAR, rating = 3.8f,
            ubicacion = "Jr. Carlos F. Vivanco 145, Int. C, Huamanga",
            telefono = "996 622 006",
            servicios = listOf("Wi-Fi gratis", "Desayuno disponible", "Lavandería", "Servicio a la habitación"),
            coordenadas = Pair(-13.16288, -74.22765),
            fuente = "expedia.com (h36448971), ruta0.com, tripadvisor.com"
        ),
        Hospedaje(
            id = 5,
            nombre = "Altipacha Ayacucho Hotel",
            descripcion = "Hotel en el Jr. 28 de Julio, cerca de los templos coloniales del centro.",
            tipo = TipoHospedaje.HOTEL, subtipo = "Hotel",
            precioNoche = CONSULTAR,
            ubicacion = "Jr. 28 de Julio, Huamanga",
            servicios = listOf("Desayuno incluido", "Estacionamiento", "Wi-Fi gratis", "Restaurante", "Acepta mascotas"),
            coordenadas = Pair(-13.16570, -74.22750),
            fuente = "expedia.com (h34367771)"
        ),
        Hospedaje(
            id = 6,
            nombre = "Hotel Sierra Dorada",
            descripcion = "Hotel de tres estrellas en la urbanización Mariscal Cáceres, al norte del centro.",
            tipo = TipoHospedaje.HOTEL, subtipo = "Hotel", estrellas = 3,
            precioNoche = CONSULTAR,
            ubicacion = "Urb. Mariscal Cáceres Mz. I Lt. 21, Huamanga",
            servicios = listOf("Desayuno incluido", "Estacionamiento", "Wi-Fi gratis", "Restaurante"),
            coordenadas = Pair(-13.14914, -74.22567),
            fuente = "expedia.com (h24438555), ruta0.com"
        ),
        Hospedaje(
            id = 7,
            nombre = "Hotel Casa Munayki",
            descripcion = "Hotel en el centro histórico, cerca del Jr. Garcilaso de la Vega.",
            tipo = TipoHospedaje.HOTEL, subtipo = "Hotel",
            precioNoche = CONSULTAR,
            ubicacion = "Jr. Garcilaso de la Vega 209, Huamanga",
            servicios = listOf("Desayuno incluido", "Wi-Fi gratis", "Restaurante", "Lavandería", "Acepta mascotas"),
            coordenadas = Pair(-13.15921, -74.22748),
            fuente = "expedia.com (h121234112)"
        ),
        Hospedaje(
            id = 8,
            nombre = "Sumaq Wari Hotel",
            descripcion = "Hotel con estacionamiento en el Jr. San Martín, al este del centro histórico.",
            tipo = TipoHospedaje.HOTEL, subtipo = "Hotel",
            precioNoche = CONSULTAR,
            ubicacion = "Jr. San Martín 837, Huamanga",
            servicios = listOf("Desayuno incluido", "Estacionamiento", "Wi-Fi gratis", "Limpieza diaria"),
            coordenadas = Pair(-13.16291, -74.22242),
            fuente = "expedia.com (h83313726)"
        ),
        Hospedaje(
            id = 9,
            nombre = "Hotel Yañez Inn",
            descripcion = "Hotel en la Av. Mariscal Cáceres, cerca del mercado y de la salida a Quinua.",
            tipo = TipoHospedaje.HOTEL, subtipo = "Hotel",
            precioNoche = CONSULTAR, rating = 3.5f,
            ubicacion = "Av. Mariscal Cáceres 1210, Huamanga",
            servicios = listOf("Desayuno incluido", "Wi-Fi gratis", "Guardaequipaje", "Sala común"),
            coordenadas = Pair(-13.15698, -74.22726),
            fuente = "expedia.com (h95878925), tripadvisor.com"
        ),
        Hospedaje(
            id = 10,
            nombre = "La Villa de Roel",
            descripcion = "Hospedaje en el Jr. Manco Cápac, en el barrio de la Magdalena.",
            tipo = TipoHospedaje.HOTEL, subtipo = "Hospedaje",
            precioNoche = CONSULTAR,
            ubicacion = "Jr. Manco Cápac 513, Huamanga",
            telefono = "979 703 295",
            servicios = listOf("Wi-Fi gratis", "Lavandería", "Servicio a la habitación", "Recepción 24 h"),
            coordenadas = Pair(-13.15638, -74.22308),
            fuente = "expedia.com (h37706272), ruta0.com"
        ),
        Hospedaje(
            id = 11,
            nombre = "Hospedaje Libertadores",
            descripcion = "Hospedaje en el pueblo de Quinua, al costado del mercado. Ideal para visitar la Pampa de Ayacucho y Wari.",
            tipo = TipoHospedaje.HOTEL, subtipo = "Hospedaje",
            precioNoche = CONSULTAR,
            ubicacion = "Al costado del mercado de Quinua",
            coordenadas = Pair(-13.05021, -74.13944),
            fuente = "expedia.com (h89174905)"
        )
    )

    /**
     * Casas, departamentos y casas de hospedaje (alojamiento tipo Airbnb).
     * No se listan anuncios de personas particulares: para eso la app abre
     * la búsqueda de Airbnb en Huamanga (ver ExploreFragment).
     */
    fun obtenerAirbnb(): List<Hospedaje> = listOf(
        Hospedaje(
            id = 101,
            nombre = "TuKasa – Hospedaje Libertad",
            descripcion = "Bed & breakfast familiar con desayuno de comida local incluido.",
            tipo = TipoHospedaje.AIRBNB, subtipo = "Bed & breakfast",
            precioNoche = CONSULTAR,
            ubicacion = "Jr. Libertad 938, Huamanga",
            servicios = listOf("Desayuno incluido", "Wi-Fi (50+ Mbps)", "Lavandería"),
            imagen = R.drawable.ph_airbnb,
            coordenadas = Pair(-13.15515, -74.22768),
            fuente = "expedia.com (h126621545)"
        ),
        Hospedaje(
            id = 102,
            nombre = "Departamento en la Plaza Mayor",
            descripcion = "Departamento entero de 1 dormitorio frente a la Plaza Mayor, para hasta 4 adultos y 1 niño.",
            tipo = TipoHospedaje.AIRBNB, subtipo = "Departamento entero",
            precioNoche = "Desde US$ 84 / noche",
            ubicacion = "Plaza Mayor de Huamanga",
            servicios = listOf("Cocina", "Lavadora", "Secadora", "Wi-Fi", "Acepta mascotas"),
            imagen = R.drawable.ph_airbnb,
            coordenadas = Pair(-13.1603, -74.2252),
            coordenadasAproximadas = true,
            fuente = "expedia.com (h118165116), precio consultado en oct. 2026"
        ),
        Hospedaje(
            id = 103,
            nombre = "Tinoco Apart Hotel",
            descripcion = "Apart hotel con departamentos equipados, a una cuadra de la Plaza Mayor.",
            tipo = TipoHospedaje.AIRBNB, subtipo = "Apart hotel",
            precioNoche = CONSULTAR,
            ubicacion = "Jr. Tres Máscaras 152, Huamanga",
            imagen = R.drawable.ph_airbnb,
            coordenadas = Pair(-13.1624, -74.2247),
            coordenadasAproximadas = true,
            fuente = "infoisinfo.com.pe, tripadvisor.com"
        )
    )
}
