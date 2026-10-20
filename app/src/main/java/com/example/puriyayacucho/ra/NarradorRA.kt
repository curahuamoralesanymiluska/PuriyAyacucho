package com.example.puriyayacucho.ra

import android.content.Context
import android.content.res.Configuration
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.os.Handler
import android.os.Looper
import java.util.Locale

/**
 * Narración por voz de un sitio (ES / EN / PT) usando la voz del celular.
 * [onEstadoCambiado] avisa si está hablando, para cambiar el ícono del botón.
 */
class NarradorRA(
    private val context: Context,
    private val onEstadoCambiado: (hablando: Boolean) -> Unit
) : TextToSpeech.OnInitListener {

    val idiomas = listOf("es", "en", "pt")
    var idiomaActual = 0
        private set

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private var listo = false
    var hablando = false
        private set
    private val principal = Handler(Looper.getMainLooper())

    init {
        val idiomaApp = context.resources.configuration.locales[0].language
        idiomaActual = idiomas.indexOf(idiomaApp).coerceAtLeast(0)
    }

    val codigoIdioma: String get() = idiomas[idiomaActual].uppercase(Locale.ROOT)
    val disponible: Boolean get() = listo

    override fun onInit(status: Int) {
        listo = status == TextToSpeech.SUCCESS
        if (!listo) return
        aplicarIdioma()
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) { principal.post { marcar(false) } }
            @Deprecated("Requerido por la API")
            override fun onError(utteranceId: String?) { principal.post { marcar(false) } }
        })
    }

    private fun aplicarIdioma() {
        val codigo = idiomas[idiomaActual]
        val locale = if (codigo == "es") Locale("es", "PE") else Locale(codigo)
        val r = tts?.setLanguage(locale)
        if (r == TextToSpeech.LANG_MISSING_DATA || r == TextToSpeech.LANG_NOT_SUPPORTED) tts?.setLanguage(Locale("es"))
    }

    /** Texto del recurso [textoRes] en el idioma elegido para la voz. */
    private fun texto(textoRes: Int): String {
        val config = Configuration(context.resources.configuration)
        config.setLocale(Locale(idiomas[idiomaActual]))
        return context.createConfigurationContext(config).getString(textoRes)
    }

    /** Empieza o detiene la narración. Devuelve false si la voz no está disponible. */
    fun alternar(textoRes: Int): Boolean {
        if (!listo) return false
        if (hablando) {
            tts?.stop()
            marcar(false)
        } else {
            tts?.speak(texto(textoRes), TextToSpeech.QUEUE_FLUSH, null, "narracion")
            marcar(true)
        }
        return true
    }

    /** Pasa al siguiente idioma; si estaba hablando, continúa en el nuevo idioma. */
    fun siguienteIdioma(textoRes: Int) {
        idiomaActual = (idiomaActual + 1) % idiomas.size
        if (!listo) return
        aplicarIdioma()
        if (hablando) tts?.speak(texto(textoRes), TextToSpeech.QUEUE_FLUSH, null, "narracion")
    }

    private fun marcar(activo: Boolean) {
        hablando = activo
        onEstadoCambiado(activo)
    }

    fun cerrar() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
