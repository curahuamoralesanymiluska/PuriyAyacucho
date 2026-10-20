package com.example.puriyayacucho.guia

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

class VozGuia(private val context: Context, private val onSinVoz: () -> Unit) {
    private var tts: TextToSpeech? = null
    private var listo = AtomicBoolean(false)
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var focusRequest: AudioFocusRequest? = null
    private var ultimaFrase: String? = null

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Idioma de la app, fallback a español
                val loc = Locale.getDefault()
                val lang = if (loc.language == "qu") Locale("es", "PE") else loc

                val result = tts?.setLanguage(lang)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    val fallback = tts?.setLanguage(Locale("es", "ES"))
                    if (fallback == TextToSpeech.LANG_MISSING_DATA || fallback == TextToSpeech.LANG_NOT_SUPPORTED) {
                        onSinVoz()
                    } else {
                        listo.set(true)
                    }
                } else {
                    listo.set(true)
                }

                // Configurar audio para navegación (baja la música)
                val atributos = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
                tts?.setAudioAttributes(atributos)

                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}
                    override fun onDone(utteranceId: String?) {
                        abandonarFocoAudio()
                    }
                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        abandonarFocoAudio()
                    }
                })
            } else {
                onSinVoz()
            }
        }
    }

    private fun solicitarFocoAudio() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val atributos = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                .setAudioAttributes(atributos)
                .build()
            focusRequest?.let { audioManager.requestAudioFocus(it) }
        } else {
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(
                null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
            )
        }
    }

    private fun abandonarFocoAudio() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusRequest?.let { audioManager.abandonAudioFocusRequest(it) }
        } else {
            @Suppress("DEPRECATION")
            audioManager.abandonAudioFocus(null)
        }
    }

    fun hablar(texto: String) {
        if (!listo.get() || texto == ultimaFrase) return
        ultimaFrase = texto
        solicitarFocoAudio()
        tts?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "viaje_" + System.currentTimeMillis())
    }

    fun shutdown() {
        abandonarFocoAudio()
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
