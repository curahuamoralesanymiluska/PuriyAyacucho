package com.example.puriyayacucho

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.puriyayacucho.data.PuntoInteresRA
import com.example.puriyayacucho.data.SitioRA
import com.example.puriyayacucho.data.SitioRAFactory
import com.example.puriyayacucho.ra.ReconstruccionView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.slider.Slider
import java.util.Locale

/**
 * Experiencia de Realidad Aumentada en el sitio (Wari, Pampa de Quinua, Vilcashuamán).
 * Muestra la cámara y encima la reconstrucción del lugar, con narración por voz.
 */
class RAActivity : BaseActivity(), TextToSpeech.OnInitListener {

    companion object {
        private const val EXTRA_SITIO_ID = "sitio_id"
        private const val EXTRA_DEMO = "modo_demo"

        fun abrir(context: Context, sitio: SitioRA, modoDemo: Boolean) {
            context.startActivity(Intent(context, RAActivity::class.java).apply {
                putExtra(EXTRA_SITIO_ID, sitio.id)
                putExtra(EXTRA_DEMO, modoDemo)
            })
        }
    }

    private lateinit var sitio: SitioRA
    private lateinit var previewView: PreviewView
    private lateinit var reconstruccion: ReconstruccionView
    private lateinit var cardPunto: MaterialCardView

    private var tts: TextToSpeech? = null
    private var ttsListo = false
    private var hablando = false

    /** Idiomas de la narración (el quechua aún no tiene voz en Android). */
    private val idiomas = listOf("es", "en", "pt")
    private var idiomaActual = 0

    private val pedirCamara = registerForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        if (concedido) iniciarCamara()
        else Toast.makeText(this, R.string.ra_camera_denied, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ra)

        sitio = SitioRAFactory.porId(intent.getIntExtra(EXTRA_SITIO_ID, -1)) ?: run {
            finish()
            return
        }
        val modoDemo = intent.getBooleanExtra(EXTRA_DEMO, false)

        previewView = findViewById(R.id.previewRa)
        reconstruccion = findViewById(R.id.reconstruccionView)
        cardPunto = findViewById(R.id.cardRaPunto)

        findViewById<TextView>(R.id.tvRaUbicacion).text =
            if (modoDemo) getString(R.string.ra_demo_mode, getString(sitio.nombre))
            else getString(R.string.ra_you_are_at, getString(sitio.nombre))

        reconstruccion.tipo = sitio.tipo
        reconstruccion.puntosInteres = sitio.puntosInteres
        reconstruccion.onPuntoTocado = { mostrarPunto(it) }

        findViewById<Slider>(R.id.sliderRa).addOnChangeListener { _, valor, _ ->
            reconstruccion.progreso = valor / 100f
        }
        reconstruccion.progreso = 0.7f

        findViewById<View>(R.id.btnRaCerrar).setOnClickListener { finish() }
        findViewById<View>(R.id.btnRaNarracion).setOnClickListener { alternarNarracion() }
        findViewById<View>(R.id.btnRaCaptura).setOnClickListener { capturar() }
        findViewById<View>(R.id.btnRaIdioma).setOnClickListener { cambiarIdioma() }
        cardPunto.setOnClickListener { cardPunto.visibility = View.GONE }

        // Idioma inicial de la narración = idioma de la app (si está disponible)
        val idiomaApp = resources.configuration.locales[0].language
        idiomaActual = idiomas.indexOf(idiomaApp).coerceAtLeast(0)
        findViewById<TextView>(R.id.tvRaIdioma).text = idiomas[idiomaActual].uppercase(Locale.ROOT)

        tts = TextToSpeech(this, this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            iniciarCamara()
        } else {
            pedirCamara.launch(Manifest.permission.CAMERA)
        }
    }

    private fun iniciarCamara() {
        val futuro = ProcessCameraProvider.getInstance(this)
        futuro.addListener({
            try {
                val proveedor = futuro.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                proveedor.unbindAll()
                proveedor.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview)
            } catch (e: Exception) {
                Toast.makeText(this, R.string.ra_camera_error, Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun mostrarPunto(punto: PuntoInteresRA) {
        findViewById<TextView>(R.id.tvRaPuntoTitulo).setText(punto.titulo)
        findViewById<TextView>(R.id.tvRaPuntoDetalle).setText(punto.descripcion)
        cardPunto.visibility = View.VISIBLE
        findViewById<View>(R.id.tvRaAyuda).visibility = View.GONE
    }

    // ---------- Narración por voz ----------

    override fun onInit(status: Int) {
        ttsListo = status == TextToSpeech.SUCCESS
        if (!ttsListo) return
        aplicarIdiomaTts()
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) = runOnUiThread { marcarNarracion(false) }
            @Deprecated("Requerido por la API")
            override fun onError(utteranceId: String?) = runOnUiThread { marcarNarracion(false) }
        })
    }

    private fun aplicarIdiomaTts() {
        val codigo = idiomas[idiomaActual]
        val locale = if (codigo == "es") Locale("es", "PE") else Locale(codigo)
        val resultado = tts?.setLanguage(locale)
        if (resultado == TextToSpeech.LANG_MISSING_DATA || resultado == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts?.setLanguage(Locale("es"))
        }
    }

    /** Texto de la narración en el idioma elegido para la voz. */
    private fun textoNarracion(): String {
        val config = Configuration(resources.configuration)
        config.setLocale(Locale(idiomas[idiomaActual]))
        return createConfigurationContext(config).getString(sitio.narracion)
    }

    private fun alternarNarracion() {
        if (!ttsListo) {
            Toast.makeText(this, R.string.ra_tts_unavailable, Toast.LENGTH_SHORT).show()
            return
        }
        if (hablando) {
            tts?.stop()
            marcarNarracion(false)
        } else {
            tts?.speak(textoNarracion(), TextToSpeech.QUEUE_FLUSH, null, "narracion_${sitio.id}")
            marcarNarracion(true)
        }
    }

    private fun marcarNarracion(activa: Boolean) {
        hablando = activa
        findViewById<ImageView>(R.id.ivRaNarracion).setImageResource(
            if (activa) R.drawable.ic_stop else R.drawable.ic_volume
        )
    }

    private fun cambiarIdioma() {
        idiomaActual = (idiomaActual + 1) % idiomas.size
        findViewById<TextView>(R.id.tvRaIdioma).text = idiomas[idiomaActual].uppercase(Locale.ROOT)
        if (ttsListo) {
            aplicarIdiomaTts()
            if (hablando) tts?.speak(textoNarracion(), TextToSpeech.QUEUE_FLUSH, null, "narracion_${sitio.id}")
        }
    }

    // ---------- Captura de foto ----------

    private fun capturar() {
        val ancho = previewView.width
        val alto = previewView.height
        if (ancho == 0 || alto == 0) return

        val foto = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(foto)
        // Fondo: imagen de la cámara (o el fondo de demostración)
        val camara = previewView.bitmap
        if (camara != null) canvas.drawBitmap(camara, 0f, 0f, null)
        else findViewById<View>(R.id.main).background?.let { it.setBounds(0, 0, ancho, alto); it.draw(canvas) }
        // Encima: la reconstrucción
        reconstruccion.draw(canvas)

        if (guardarEnGaleria(foto)) {
            Toast.makeText(this, R.string.ra_photo_saved, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, R.string.ra_photo_error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarEnGaleria(bitmap: Bitmap): Boolean {
        val nombre = "PuriyAyacucho_RA_${System.currentTimeMillis()}.jpg"
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val valores = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, nombre)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PuriyAyacucho")
                }
                val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, valores) ?: return false
                contentResolver.openOutputStream(uri)?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 92, it) } ?: return false
                true
            } else {
                // Android 9 o menor: se guarda en la carpeta de la app (no requiere permisos)
                val carpeta = getExternalFilesDir("Pictures") ?: return false
                java.io.File(carpeta, nombre).outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 92, it) }
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
