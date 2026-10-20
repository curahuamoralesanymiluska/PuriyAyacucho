package com.example.puriyayacucho.boletos

import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.math.round

/** Configuración del modelo de negocio. */
object NegocioConfig {
    /** Comisión de la plataforma por cada venta de boletos (8 %). */
    const val COMISION_PLATAFORMA = 0.08
}

/** Desglose del precio que ve el usuario antes de pagar. */
data class ResumenCompra(
    val subtotal: Double,
    val comision: Double,
    val total: Double
) {
    companion object {
        fun calcular(precioUnitario: Double, cantidad: Int, comision: Double = NegocioConfig.COMISION_PLATAFORMA): ResumenCompra {
            val subtotal = redondear(precioUnitario * cantidad)
            val cargo = redondear(subtotal * comision)
            return ResumenCompra(subtotal, cargo, redondear(subtotal + cargo))
        }

        private fun redondear(valor: Double): Double = round(valor * 100) / 100.0
    }
}

sealed class ResultadoPago {
    data class Exitoso(val codigoOperacion: String) : ResultadoPago()
    data class Fallido(val motivo: String) : ResultadoPago()
}

/**
 * Pasarela de pago. Para cobrar de verdad hay que implementar esta interfaz
 * con Culqi, Niubiz o Mercado Pago (sus SDK se encargan de los datos de tarjeta;
 * la app nunca debe guardarlos).
 */
interface PaymentGateway {
    suspend fun cobrar(monto: Double, descripcion: String): ResultadoPago
}

/** Pago SIMULADO para la demostración: no cobra nada. */
class FakePaymentGateway : PaymentGateway {
    override suspend fun cobrar(monto: Double, descripcion: String): ResultadoPago {
        delay(1_200) // simula la espera del banco
        return ResultadoPago.Exitoso("DEMO-" + UUID.randomUUID().toString().take(8).uppercase())
    }
}
