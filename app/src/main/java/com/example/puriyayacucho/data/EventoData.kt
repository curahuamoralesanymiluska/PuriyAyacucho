package com.example.puriyayacucho.data

import com.example.puriyayacucho.R

enum class TipoEvento(val colorHex: String, val etiqueta: Int) {
    CONCIERTO("#FF3D5A", R.string.event_type_concert),
    FESTIVIDAD("#FF7A18", R.string.event_type_festivity),
    TEATRO("#7C4DFF", R.string.event_type_theater)
}

data class TipoEntrada(
    val nombre: String,
    val precio: Double,
    val stock: Int
)

data class EventoBoleto(
    val id: Int,
    val nombre: String,
    val tipo: TipoEvento,
    /** Formato "dd/MM/yyyy" */
    val fecha: String,
    /** Formato "HH:mm" (24 h) */
    val hora: String,
    val lugar: String,
    val coordenadas: Pair<Double, Double>,
    val imagen: Any = R.drawable.ph_evento,
    val entradas: List<TipoEntrada>,
    val organizador: String,
    /** Si el evento corresponde a una festividad del calendario. */
    val festividadId: Int? = null
) {
    val precioDesde: Double get() = entradas.minOfOrNull { it.precio } ?: 0.0
}

/**
 * AVISO: DATOS DE EJEMPLO — TODO: reemplazar por eventos reales de los organizadores
 * (o cargarlos desde Firestore para no tener que actualizar la app).
 */
object EventoFactory {
    fun obtenerEventos(): List<EventoBoleto> = listOf(
        EventoBoleto(
            id = 1,
            nombre = "Noche de huaynos",
            tipo = TipoEvento.CONCIERTO,
            fecha = "24/10/2026",
            hora = "20:00",
            lugar = "Coliseo de Huamanga",
            coordenadas = Pair(-13.1560, -74.2210),
            entradas = listOf(
                TipoEntrada("General", 35.0, 500),
                TipoEntrada("Preferencial", 60.0, 150),
                TipoEntrada("VIP", 90.0, 40)
            ),
            organizador = "Organizador de ejemplo"
        ),
        EventoBoleto(
            id = 2,
            nombre = "Serenata en la Plaza",
            tipo = TipoEvento.FESTIVIDAD,
            fecha = "02/11/2026",
            hora = "19:00",
            lugar = "Plaza Mayor de Huamanga",
            coordenadas = Pair(-13.1605, -74.2257),
            entradas = listOf(
                TipoEntrada("General", 15.0, 800),
                TipoEntrada("Silla reservada", 30.0, 200)
            ),
            organizador = "Organizador de ejemplo"
        ),
        EventoBoleto(
            id = 3,
            nombre = "Teatro de retablos",
            tipo = TipoEvento.TEATRO,
            fecha = "15/11/2026",
            hora = "18:30",
            lugar = "Centro Cultural de Huamanga",
            coordenadas = Pair(-13.1592, -74.2262),
            entradas = listOf(
                TipoEntrada("General", 20.0, 120),
                TipoEntrada("Estudiante", 12.0, 60)
            ),
            organizador = "Organizador de ejemplo"
        )
    )

    fun porId(id: Int): EventoBoleto? = obtenerEventos().firstOrNull { it.id == id }
}
