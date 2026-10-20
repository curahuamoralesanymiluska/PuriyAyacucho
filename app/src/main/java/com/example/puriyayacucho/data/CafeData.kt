package com.example.puriyayacucho.data

import com.example.puriyayacucho.R
import com.example.puriyayacucho.guia.UbicacionHelper

/**
 * Cafés REALES de Huamanga, cargados con fuentes públicas (oct. 2026).
 * Los que tienen coordenadasAproximadas = true deben verificarse en Maps.
 * Los que tienen ubicacionConfirmada = false aparecen en Explorar y filtros,
 * pero NO en el mapa ni en la Guía.
 */
data class Cafe(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val ubicacion: String,
    val horario: String = "Por confirmar",
    val telefono: String = "N/A",
    val precio: String = "Por confirmar",
    val rating: Float? = null,
    val imagen: Any = R.drawable.logo,
    val coordenadas: Pair<Double, Double>,
    val coordenadasAproximadas: Boolean = false,
    val ubicacionConfirmada: Boolean = true,
    val comodidades: Set<Comodidad> = emptySet(),
    val fuente: String
)

object CafeFactory {
    fun obtenerCafes(): List<Cafe> = listOf(

        Cafe(
            id = 10001,
            nombre = "Museo Café Ayacuchano",
            descripcion = "Café de la región con granos de Ayacucho, frente a la Plaza Mayor.",
            ubicacion = "Jr. Lima 106, Plaza Mayor, Huamanga",
            horario = "Todos los días · 7:30 a 23:00",
            telefono = "927270728",
            precio = "Por confirmar",
            coordenadas = Pair(-13.1606, -74.2266),
            coordenadasAproximadas = true,
            comodidades = setOf(Comodidad.CAFE_DE_LA_REGION),
            fuente = "https://www.cafeayacuchano.pe/nuestras-cafeterias/"
        ),

        Cafe(
            id = 10002,
            nombre = "Café Bicentenario",
            descripcion = "Café de la región de la cadena Café Ayacuchano, Jr. 28 de Julio.",
            ubicacion = "Jr. 28 de Julio 106, Huamanga",
            horario = "Lun a sáb · 9:00 a 22:00 · Dom · 16:00 a 22:00",
            telefono = "942905966",
            precio = "Por confirmar",
            coordenadas = Pair(-13.1615, -74.2264),
            coordenadasAproximadas = true,
            comodidades = setOf(Comodidad.CAFE_DE_LA_REGION),
            fuente = "https://www.cafeayacuchano.pe/nuestras-cafeterias/"
        ),

        Cafe(
            id = 10003,
            nombre = "Antaqa · Café de especialidad",
            descripcion = "Café de especialidad que también tuesta sus propios granos ayacuchanos.",
            ubicacion = "Jr. Tres Máscaras 510, Huamanga",
            horario = "Lun a sáb · 8:30 a 21:20 · Dom · 8:30 a 12:00 y 17:00 a 20:30",
            telefono = "981450602",
            precio = "Por confirmar",
            coordenadas = Pair(-13.158893, -74.223535),
            comodidades = setOf(Comodidad.CAFE_ESPECIALIDAD, Comodidad.POSTRES, Comodidad.ACEPTA_MASCOTAS),
            fuente = "https://wanderlog.com/place/details/10158191/ · https://antaqa.pe/"
        ),

        Cafe(
            id = 10004,
            nombre = "Miroshnik Cacao & Coffee",
            descripcion = "Café con chocolate artesanal ayacuchano, desayunos y postres.",
            ubicacion = "Jr. Lima 186, Huamanga",
            horario = "Lun a sáb · 8:00 a 21:00 · Dom · 15:00 a 21:00",
            telefono = "976336824",
            precio = "Por confirmar",
            coordenadas = Pair(-13.160522, -74.227464),
            comodidades = setOf(Comodidad.DESAYUNOS, Comodidad.POSTRES, Comodidad.WIFI),
            fuente = "https://wanderlog.com/place/details/2832461"
        ),

        Cafe(
            id = 10005,
            nombre = "ViaVia Café Ayacucho",
            descripcion = "Café, restaurante y bar frente a la Plaza Mayor, con desayunos todo el día.",
            ubicacion = "Jr. 28 de Julio 540, Huamanga",
            horario = "Por confirmar",
            precio = "Por confirmar",
            coordenadas = Pair(-13.1609, -74.2261),
            coordenadasAproximadas = true,
            comodidades = setOf(Comodidad.DESAYUNOS, Comodidad.VISTA_A_LA_PLAZA),
            fuente = "https://wanderlog.com/list/geoCategory/377979 · https://ayacucho.viavia.world/"
        ),

        Cafe(
            id = 10006,
            nombre = "La Miel",
            descripcion = "Local de postres, helados y ensaladas de frutas en el Portal Constitución.",
            ubicacion = "Portal Constitución 166, Huamanga",
            horario = "Por confirmar",
            precio = "Por confirmar",
            coordenadas = Pair(-13.1609, -74.2258),
            coordenadasAproximadas = true,
            comodidades = setOf(Comodidad.POSTRES),
            fuente = "https://wanderlog.com/list/geoCategory/561097"
        ),

        Cafe(
            id = 10007,
            nombre = "Aroma's Café & Crepes",
            descripcion = "Café con crepes y postres en el Portal Independencia.",
            ubicacion = "Portal Independencia 65, Huamanga",
            horario = "Por confirmar",
            precio = "Por confirmar",
            coordenadas = Pair(-13.1601, -74.2253),
            coordenadasAproximadas = true,
            comodidades = setOf(Comodidad.POSTRES),
            fuente = "https://wanderlog.com/list/geoCategory/377979"
        ),

        Cafe(
            id = 10008,
            nombre = "Mestizo Café Restaurante",
            descripcion = "Café-lounge con opción vegetariana en Jr. Cusco.",
            ubicacion = "Jr. Cusco 241, Huamanga",
            horario = "Por confirmar",
            precio = "Por confirmar",
            // Dirección por confirmar: se usa Plaza Mayor como coordenada de relleno
            coordenadas = UbicacionHelper.PLAZA_MAYOR.let { Pair(it.lat, it.lng) },
            ubicacionConfirmada = false,
            comodidades = setOf(Comodidad.OPCION_VEGETARIANA),
            fuente = "https://wanderlog.com/list/geoCategory/377979"
        )
    )
}
