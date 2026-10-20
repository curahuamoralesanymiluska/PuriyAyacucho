package com.example.puriyayacucho

import com.example.puriyayacucho.data.TransporteFactory
import com.example.puriyayacucho.guia.Geo
import com.example.puriyayacucho.guia.ModoViaje
import com.example.puriyayacucho.guia.Punto
import com.example.puriyayacucho.guia.Recorrido
import com.example.puriyayacucho.guia.RutaPlanner
import com.example.puriyayacucho.guia.RutasCalles
import com.example.puriyayacucho.guia.Viaje
import com.example.puriyayacucho.guia.Waypoint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

/** Pruebas del viaje animado (la personita) y del trazo por calles. */
class ViajeTest {

    private val planner = RutaPlanner(TransporteFactory.lineas, TransporteFactory.paraderos)
    private val plaza = Waypoint("Plaza Mayor", Punto(-13.1605, -74.2257))
    private val wari = Waypoint("Complejo Wari", Punto(-13.059466, -74.199535))

    @Test
    fun viajeAWariTieneTresTramosYEmpiezaYTerminaBien() {
        val plan = planner.planificar(listOf(plaza, wari), ModoViaje.TRANSPORTE)
        val viaje = Viaje(plan)
        assertEquals(3, viaje.segmentos.size) // caminar, combi, caminar
        assertEquals(plan.minutosTotales.toDouble(), viaje.totalMin, 0.001)
        assertTrue(Geo.distanciaMetros(viaje.estadoEn(0.0).punto, plaza.punto) < 1)
        assertTrue(Geo.distanciaMetros(viaje.estadoEn(viaje.totalMin).punto, wari.punto) < 1)
        assertEquals(1.0, viaje.estadoEn(viaje.totalMin).progreso, 0.0001)
    }

    @Test
    fun elGpsNoHaceRetrocederALaPersonita() {
        val viaje = Viaje(planner.planificar(listOf(plaza, wari), ModoViaje.TRANSPORTE))
        val combi = viaje.segmentos[1]
        val mitad = combi.inicioMin + combi.minutos / 2
        val (minuto, distancia) = viaje.proyectar(viaje.estadoEn(mitad).punto, 0.0)
        assertTrue(distancia < 5)
        assertEquals(mitad, minuto, 0.5)
        // Si el GPS salta hacia atrás (a la plaza), la personita se queda donde estaba
        assertEquals(minuto, viaje.proyectar(plaza.punto, minuto).first, 0.0001)
    }

    @Test
    fun enIdaYVueltaElGpsNoAdelantaALaPersonitaAlTramoDeRegreso() {
        val merced = Waypoint("La Merced", Punto(-13.162348, -74.225531))
        val plan = planner.planificar(listOf(plaza, merced, plaza), ModoViaje.A_PIE)
        // Ida por una calle y regreso por la paralela (a unos 10 m)
        val ida = listOf(plaza.punto, merced.punto)
        val vuelta = listOf(Punto(merced.punto.lat, merced.punto.lng + 0.0001), Punto(plaza.punto.lat, plaza.punto.lng + 0.0001))
        val viaje = Viaje(plan, mapOf(0 to ida, 1 to vuelta))
        assertEquals(2, viaje.segmentos.size)
        // Recién empiezas la ida; el GPS te ubica un poco más cerca de la calle de regreso
        val mitadIda = Recorrido(ida).puntoEn(0.5)
        val gps = Punto(mitadIda.lat, mitadIda.lng + 0.00006)
        val (minuto, _) = viaje.proyectar(gps, 0.0)
        assertTrue(minuto <= viaje.segmentos[0].finMin)
        // Ya de regreso, el mismo punto cuenta como el tramo de vuelta
        val (minutoVuelta, _) = viaje.proyectar(gps, viaje.segmentos[1].inicioMin + 0.1)
        assertTrue(minutoVuelta >= viaje.segmentos[1].inicioMin)
    }

    @Test
    fun conTrazoPorCallesLaPersonitaSigueLasCallesYElTiempoNoCambia() {
        val plan = planner.planificar(
            listOf(plaza, Waypoint("La Merced", Punto(-13.162348, -74.225531))), ModoViaje.A_PIE
        )
        // Una "calle" en forma de L en vez de la línea recta
        val esquina = Punto(-13.1605, -74.225531)
        val calles = listOf(plaza.punto, esquina, Punto(-13.162348, -74.225531))
        val normal = Viaje(plan)
        val porCalles = Viaje(plan, mapOf(0 to calles))
        assertEquals(normal.totalMin, porCalles.totalMin, 0.0001)
        assertEquals(3, porCalles.segmentos[0].recorrido.puntos.size)
        // A mitad de camino la personita está sobre la calle, no sobre la recta
        val mitad = porCalles.estadoEn(porCalles.totalMin / 2).punto
        val sobreLaCalle = Recorrido(calles).proyectar(mitad).second
        assertTrue(sobreLaCalle < 1)
    }

    @Test
    fun reducirPuntosConservaElPrimeroYElUltimo() {
        val puntos = (0 until 57).map { Punto(-13.0 - it * 0.001, -74.2) }
        val reducidos = RutasCalles.reducir(puntos, 20)
        assertEquals(20, reducidos.size)
        assertEquals(puntos.first(), reducidos.first())
        assertEquals(puntos.last(), reducidos.last())
        assertEquals(puntos.take(5), RutasCalles.reducir(puntos.take(5), 20))
    }

    @Test
    fun rumboHaciaElEsteEsNoventaGrados() {
        val r = Recorrido(listOf(Punto(0.0, 0.0), Punto(0.0, 0.01)))
        assertTrue(abs(r.rumboEn(0.5) - 90f) < 0.5f)
        assertEquals(0.005, r.puntoEn(0.5).lng, 1e-9)
    }
}
