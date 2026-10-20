package com.example.puriyayacucho

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.puriyayacucho.boletos.FakePaymentGateway
import com.example.puriyayacucho.boletos.NegocioConfig
import com.example.puriyayacucho.boletos.PaymentGateway
import com.example.puriyayacucho.boletos.ResultadoPago
import com.example.puriyayacucho.boletos.ResumenCompra
import com.example.puriyayacucho.data.AppDatabase
import com.example.puriyayacucho.data.BoletoEntity
import com.example.puriyayacucho.data.EventoBoleto
import com.example.puriyayacucho.data.EventoFactory
import com.example.puriyayacucho.data.TipoEntrada
import com.google.android.material.button.MaterialButton
import com.google.android.material.radiobutton.MaterialRadioButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/** Elegir tipo de entrada y cantidad, ver el resumen y pagar (pago simulado). */
class CompraActivity : BaseActivity() {

    companion object {
        private const val EXTRA_EVENTO_ID = "evento_id"
        private const val MAX_ENTRADAS = 10

        fun abrir(context: Context, eventoId: Int) {
            context.startActivity(Intent(context, CompraActivity::class.java).putExtra(EXTRA_EVENTO_ID, eventoId))
        }
    }

    /** Para cobrar de verdad, reemplazar por la implementación real (Culqi, Niubiz, Mercado Pago). */
    private val pasarela: PaymentGateway = FakePaymentGateway()

    private lateinit var evento: EventoBoleto
    private var entradaElegida: TipoEntrada? = null
    private var cantidad = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compra)

        evento = EventoFactory.porId(intent.getIntExtra(EXTRA_EVENTO_ID, -1)) ?: run {
            finish()
            return
        }

        findViewById<View>(R.id.btnCompraVolver).setOnClickListener { finish() }
        findViewById<TextView>(R.id.tvCompraTipo).setText(evento.tipo.etiqueta)
        findViewById<TextView>(R.id.tvCompraNombre).text = evento.nombre
        findViewById<TextView>(R.id.tvCompraInfo).text = "${evento.fecha} · ${evento.hora} · ${evento.lugar}"

        configurarTiposDeEntrada()

        findViewById<View>(R.id.btnMenos).setOnClickListener { cambiarCantidad(-1) }
        findViewById<View>(R.id.btnMas).setOnClickListener { cambiarCantidad(+1) }
        findViewById<View>(R.id.btnPagar).setOnClickListener { pagar() }

        actualizarResumen()
    }

    private fun configurarTiposDeEntrada() {
        val grupo = findViewById<RadioGroup>(R.id.rgTipoEntrada)
        val rojo = Color.parseColor("#FF3D5A")
        evento.entradas.forEachIndexed { i, entrada ->
            val boton = MaterialRadioButton(this).apply {
                id = View.generateViewId()
                text = "${entrada.nombre}  ·  ${soles(entrada.precio)}"
                textSize = 15f
                minHeight = (48 * resources.displayMetrics.density).toInt()
                buttonTintList = ColorStateList.valueOf(rojo)
                isEnabled = entrada.stock > 0
            }
            grupo.addView(boton)
            if (i == 0) {
                boton.isChecked = true
                entradaElegida = entrada
            }
            boton.setOnCheckedChangeListener { _, marcado ->
                if (marcado) {
                    entradaElegida = entrada
                    actualizarResumen()
                }
            }
        }
    }

    private fun cambiarCantidad(delta: Int) {
        val maximo = minOf(MAX_ENTRADAS, entradaElegida?.stock ?: MAX_ENTRADAS)
        cantidad = (cantidad + delta).coerceIn(1, maximo)
        findViewById<TextView>(R.id.tvCantidad).text = cantidad.toString()
        actualizarResumen()
    }

    private fun resumen(): ResumenCompra = ResumenCompra.calcular(entradaElegida?.precio ?: 0.0, cantidad)

    private fun actualizarResumen() {
        val r = resumen()
        val porcentaje = (NegocioConfig.COMISION_PLATAFORMA * 100).toInt()
        findViewById<TextView>(R.id.tvResumenEntradasLabel).text =
            getString(R.string.purchase_tickets_line, cantidad, entradaElegida?.nombre ?: "")
        findViewById<TextView>(R.id.tvResumenSubtotal).text = soles(r.subtotal)
        findViewById<TextView>(R.id.tvResumenComisionLabel).text = getString(R.string.purchase_service_fee, porcentaje)
        findViewById<TextView>(R.id.tvResumenComision).text = soles(r.comision)
        findViewById<TextView>(R.id.tvResumenTotal).text = soles(r.total)
        findViewById<MaterialButton>(R.id.btnPagar).text = getString(R.string.purchase_pay, soles(r.total))
    }

    private fun pagar() {
        val entrada = entradaElegida ?: return
        val r = resumen()
        val btnPagar = findViewById<MaterialButton>(R.id.btnPagar)
        val progreso = findViewById<View>(R.id.progressPago)
        btnPagar.isEnabled = false
        progreso.visibility = View.VISIBLE

        lifecycleScope.launch {
            when (val resultado = pasarela.cobrar(r.total, "${evento.nombre} x$cantidad")) {
                is ResultadoPago.Exitoso -> {
                    val correo = getSharedPreferences("usuario_sesion", MODE_PRIVATE).getString("correo_usuario", "") ?: ""
                    val boleto = BoletoEntity(
                        codigoUuid = UUID.randomUUID().toString(),
                        usuarioCorreo = correo,
                        eventoId = evento.id,
                        nombreEvento = evento.nombre,
                        fecha = evento.fecha,
                        hora = evento.hora,
                        lugar = evento.lugar,
                        tipoEntrada = entrada.nombre,
                        cantidad = cantidad,
                        total = r.total,
                        estado = "PAGADO",
                        fechaCompra = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                    )
                    val id = AppDatabase.getInstance(this@CompraActivity).boletoDao().insertar(boleto)
                    BoletoActivity.abrir(this@CompraActivity, id.toInt())
                    finish()
                }
                is ResultadoPago.Fallido -> {
                    btnPagar.isEnabled = true
                    progreso.visibility = View.GONE
                    Toast.makeText(this@CompraActivity, resultado.motivo, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun soles(valor: Double) = String.format(Locale.US, "S/ %.2f", valor)
}
