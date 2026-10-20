package com.example.puriyayacucho

import com.example.puriyayacucho.data.LineaTransporte
import com.example.puriyayacucho.data.TipoTransporte
import com.example.puriyayacucho.data.TransporteFactory
import com.example.puriyayacucho.guia.ModoViaje
import com.example.puriyayacucho.guia.Punto
import com.example.puriyayacucho.guia.RutaPlanner
import com.example.puriyayacucho.guia.TipoPaso
import com.example.puriyayacucho.guia.Waypoint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Paraderos reales de las combis y micros de la ciudad (parada libre). */
class TransporteTest {

    private val planner = RutaPlanner(TransporteFactory.lineas, TransporteFactory.paraderos)
    private val plaza = Waypoint("Plaza Mayor", Punto(-13.1605, -74.2257))

    @Test
    fun aQuinuaSeSubeEnElExParaderoHuanta() {
        val quinua = Waypoint("Quinua", Punto(-13.0505, -74.1398))
        val paso = planner.planificar(listOf(plaza, quinua), ModoViaje.TRANSPORTE)
            .pasos.first { it.tipo == TipoPaso.TRANSPORTE }
        assertEquals(1, paso.linea?.id)
        assertEquals(1, paso.paraderoSubida?.id)
        assertTrue(paso.paraderoSubida!!.nombre.contains("Ex paradero de Huanta"))
    }

    @Test
    fun aHuantaSeSubeEnElParqueMagdalena() {
        val huanta = Waypoint("Plaza de Huanta", Punto(-12.9397, -74.2475))
        val plan = planner.planificar(listOf(plaza, huanta), ModoViaje.TRANSPORTE)
        val paso = plan.pasos.first { it.tipo == TipoPaso.TRANSPORTE }
        assertEquals(3, paso.linea?.id)
        assertEquals(6, paso.paraderoSubida?.id)
        // Cerca de una hora en total (caminar + esperar + combi + caminar)
        assertTrue(plan.minutosTotales in 50..90)
    }

    @Test
    fun elMicroSeTomaEnLaEsquinaMasCercanaDeSuRuta() {
        // Micro de prueba que cruza la ciudad de sur a norte
        val micro = LineaTransporte(
            id = 99, nombre = "Micro de prueba", tipo = TipoTransporte.MICRO,
            recorrido = listOf(Punto(-13.175, -74.226), Punto(-13.160, -74.226), Punto(-13.140, -74.226)),
            tarifa = 1.0, velocidadKmh = 15.0, horario = "Todo el día", paradaLibre = true
        )
        val conMicro = RutaPlanner(listOf(micro), emptyList())
        val origen = Waypoint("Casa", Punto(-13.1740, -74.2272))      // a ~100 m de la ruta
        val destino = Waypoint("Colegio", Punto(-13.1420, -74.2250))  // a ~110 m de la ruta
        val plan = conMicro.planificar(listOf(origen, destino), ModoViaje.TRANSPORTE)
        val paso = plan.pasos.first { it.tipo == TipoPaso.TRANSPORTE }
        assertEquals(99, paso.linea?.id)
        assertEquals(RutaPlanner.ESQUINA_SUBIDA, paso.paraderoSubida?.nombre)
        assertEquals(RutaPlanner.ESQUINA_BAJADA, paso.paraderoBajada?.nombre)
        // Las caminatas hasta el micro son cortas
        assertTrue(plan.pasos.filter { it.tipo == TipoPaso.CAMINAR }.all { it.metros < 300 })
        assertEquals(1.0, plan.tarifaTotal, 0.001)
    }
}
