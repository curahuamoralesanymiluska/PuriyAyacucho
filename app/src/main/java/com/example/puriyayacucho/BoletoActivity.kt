package com.example.puriyayacucho

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.puriyayacucho.data.AppDatabase
import com.example.puriyayacucho.data.BoletoEntity
import com.example.puriyayacucho.data.EventoFactory
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

/** Boleto comprado, con su código QR para mostrar en la entrada. */
class BoletoActivity : BaseActivity() {

    companion object {
        private const val EXTRA_BOLETO_ID = "boleto_id"

        fun abrir(context: Context, boletoId: Int) {
            context.startActivity(Intent(context, BoletoActivity::class.java).putExtra(EXTRA_BOLETO_ID, boletoId))
        }

        /** Genera la imagen del código QR (en un hilo de fondo). */
        fun generarQr(texto: String, tamano: Int = 600): Bitmap {
            val hints = mapOf(EncodeHintType.MARGIN to 1)
            val matriz = QRCodeWriter().encode(texto, BarcodeFormat.QR_CODE, tamano, tamano, hints)
            val pixeles = IntArray(tamano * tamano)
            for (y in 0 until tamano) {
                for (x in 0 until tamano) {
                    pixeles[y * tamano + x] = if (matriz.get(x, y)) Color.parseColor("#222222") else Color.WHITE
                }
            }
            return Bitmap.createBitmap(pixeles, tamano, tamano, Bitmap.Config.ARGB_8888)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boleto)
        findViewById<View>(R.id.btnBoletoVolver).setOnClickListener { finish() }

        val id = intent.getIntExtra(EXTRA_BOLETO_ID, -1)
        lifecycleScope.launch {
            val boleto = AppDatabase.getInstance(this@BoletoActivity).boletoDao().obtenerPorId(id)
            if (boleto == null) {
                finish()
                return@launch
            }
            mostrar(boleto)
        }
    }

    private suspend fun mostrar(b: BoletoEntity) {
        val evento = EventoFactory.porId(b.eventoId)
        val tipo = evento?.let { getString(it.tipo.etiqueta) } ?: ""
        findViewById<TextView>(R.id.tvBoletoTipo).text = "PURIY AYACUCHO · ${tipo.uppercase(Locale.getDefault())}"
        findViewById<TextView>(R.id.tvBoletoEvento).text = b.nombreEvento
        findViewById<TextView>(R.id.tvBoletoLugar).text = b.lugar
        findViewById<TextView>(R.id.tvBoletoFecha).text = b.fecha
        findViewById<TextView>(R.id.tvBoletoHora).text = b.hora
        findViewById<TextView>(R.id.tvBoletoEntradas).text = "${b.cantidad} · ${b.tipoEntrada}"
        findViewById<TextView>(R.id.tvBoletoTotal).text = String.format(Locale.US, "S/ %.2f", b.total)
        findViewById<TextView>(R.id.tvBoletoCodigo).text = b.codigoUuid.take(8).uppercase(Locale.ROOT)

        val qr = withContext(Dispatchers.Default) { generarQr("PURIY:${b.codigoUuid}") }
        findViewById<ImageView>(R.id.ivBoletoQr).setImageBitmap(qr)

        findViewById<View>(R.id.btnAgregarCalendario).setOnClickListener { agregarAlCalendario(b) }
    }

    private fun agregarAlCalendario(b: BoletoEntity) {
        val inicio = runCatching {
            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse("${b.fecha} ${b.hora}")?.time
        }.getOrNull()
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, b.nombreEvento)
            putExtra(CalendarContract.Events.EVENT_LOCATION, b.lugar)
            putExtra(CalendarContract.Events.DESCRIPTION, "Boleto Puriy Ayacucho · ${b.cantidad} ${b.tipoEntrada}")
            if (inicio != null) {
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, inicio)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, inicio + 3 * 60 * 60 * 1000L)
            }
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, R.string.ticket_calendar_error, Toast.LENGTH_SHORT).show()
        }
    }
}
