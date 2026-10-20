package com.example.puriyayacucho

import com.example.puriyayacucho.data.TransporteFactory
import com.example.puriyayacucho.guia.Geo
import com.example.puriyayacucho.guia.ModoViaje
import com.example.puriyayacucho.guia.Punto
import com.example.puriyayacucho.guia.RutaPlanner
import com.example.puriyayacucho.guia.TipoPaso
import com.example.puriyayacucho.guia.Waypoint
import com.example.puriyayacucho.guia.formatearDistancia
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RutaPlannerTest {

    private val planner = RutaPlanner(TransporteFactory.lineas, TransporteFactory.paraderos)
    private val plaza = Waypoint("Plaza Mayor", Punto(-13.1605, -74.2257))
    private val wari = Waypoint("Complejo Wari", Punto(-13.059466, -74.199535))
    private val laMerced = Waypoint("La Merced", Punto(-13.162348, -74.225531))

    @Test
    fun plazaAWari_usaLaCombiAQuinua() {
        val plan = planner.planificar(listOf(plaza, wari), ModoViaje.TRANSPORTE)
        val paso = plan.pasos.first { it.tipo == TipoPaso.TRANSPORTE }
        assertEquals(1, paso.linea?.id)
        assertEquals(4.0, plan.tarifaTotal, 0.001)
        assertEquals(TipoPaso.LLEGADA, plan.pasos.last().tipo)
    }

    @Test
    fun destinoCercano_seVaCaminandoSinCosto() {
        val plan = planner.planificar(listOf(plaza, laMerced), ModoViaje.TRANSPORTE)
        assertEquals(TipoPaso.CAMINAR, plan.pasos.first().tipo)
        assertEquals(0.0, plan.tarifaTotal, 0.001)
    }

    @Test
    fun sinLineaCercana_proponeTaxi() {
        val lejos = Waypoint("Cascada", Punto(-12.934, -74.222))
        val plan = planner.planificar(listOf(plaza, lejos), ModoViaje.TRANSPORTE)
        assertTrue(plan.sinTransporteDisponible)
        assertEquals(TipoPaso.TAXI, plan.pasos.first().tipo)
    }

    @Test
    fun paradaIntermedia_agregaUnPasoDeParada() {
        val plan = planner.planificar(listOf(plaza, laMerced, wari), ModoViaje.TRANSPORTE)
        assertEquals(1, plan.pasos.count { it.tipo == TipoPaso.PARADA })
    }

    @Test
    fun lineaSePuedeTomarEnSentidoInverso() {
        val plan = planner.planificar(listOf(wari, plaza), ModoViaje.TRANSPORTE)
        assertTrue(plan.pasos.any { it.tipo == TipoPaso.TRANSPORTE })
    }

    @Test
    fun lugarSobreLaRuta_estaAMenosDe500m() {
        val plan = planner.planificar(listOf(plaza, wari), ModoViaje.TRANSPORTE)
        val rutaDensa = Geo.densificar(plan.puntos)
        assertTrue(Geo.distanciaMinimaARuta(Punto(-13.1100, -74.2050), rutaDensa) < 500)
        assertTrue(Geo.distanciaMinimaARuta(Punto(-13.30, -74.40), rutaDensa) > 500)
    }

    @Test
    fun tarifaTaxi_seRedondeaA50Centimos() {
        assertEquals(11.0, planner.tarifaTaxi(4000.0), 0.001)
        assertEquals(6.5, planner.tarifaTaxi(1000.0), 0.001)
    }

    @Test
    fun formatoDeDistancia() {
        assertEquals("350 m", formatearDistancia(350.0))
        assertEquals("1.2 km", formatearDistancia(1234.0))
    }
}

class ResumenCompraTest {

    @Test
    fun comisionDel8PorCiento_seSumaAlTotal() {
        val r = com.example.puriyayacucho.boletos.ResumenCompra.calcular(precioUnitario = 35.0, cantidad = 2)
        assertEquals(70.0, r.subtotal, 0.001)
        assertEquals(5.6, r.comision, 0.001)
        assertEquals(75.6, r.total, 0.001)
    }

    @Test
    fun redondeaACentimos() {
        val r = com.example.puriyayacucho.boletos.ResumenCompra.calcular(precioUnitario = 12.99, cantidad = 3)
        assertEquals(38.97, r.subtotal, 0.001)
        assertEquals(3.12, r.comision, 0.001)
        assertEquals(42.09, r.total, 0.001)
    }
}
