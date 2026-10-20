package com.example.puriyayacucho.bienvenida

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.puriyayacucho.R
import com.google.android.material.button.MaterialButton

/**
 * La guía de bienvenida: la personita entra caminando, saluda y va dando consejos en un globo.
 * "Siguiente" pasa al próximo mensaje (si el texto aún se está escribiendo, primero lo completa);
 * "Saltar" o el botón Atrás la cierran. Al cerrarse queda marcada como vista ([Bienvenida]).
 */
class BienvenidaDialog : DialogFragment() {

    private var indice = 0
    private var escribiendo = false
    private val handler = Handler(Looper.getMainLooper())
    private var textoCompleto = ""
    private var letras = 0

    private var personita: GuiaPersonitaView? = null
    private var tvMensaje: TextView? = null
    private var tvPaso: TextView? = null
    private var puntos: LinearLayout? = null
    private var btnSiguiente: MaterialButton? = null

    /** Sin efecto "máquina de escribir" con TalkBack o con las animaciones apagadas. */
    private val escribirLetraPorLetra: Boolean
        get() {
            val ctx = context ?: return false
            val accesibilidad = ctx.getSystemService(Context.ACCESSIBILITY_SERVICE) as? AccessibilityManager
            return GuiaPersonitaView.animacionesActivas(ctx) && accesibilidad?.isTouchExplorationEnabled != true
        }

    private val escribir = object : Runnable {
        override fun run() {
            val tv = tvMensaje ?: return
            if (letras >= textoCompleto.length) {
                terminarDeEscribir()
                return
            }
            // Avanza de a 2 letras para que no se haga largo
            letras = (letras + 2).coerceAtMost(textoCompleto.length)
            tv.text = textoCompleto.substring(0, letras)
            handler.postDelayed(this, MS_POR_LETRA)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.PuriyDialogoBienvenida)
        indice = savedInstanceState?.getInt(ESTADO_INDICE, 0) ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.dialog_bienvenida, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personita = view.findViewById(R.id.personitaGuia)
        tvMensaje = view.findViewById(R.id.tvBienvenidaMensaje)
        tvPaso = view.findViewById(R.id.tvBienvenidaPaso)
        puntos = view.findViewById(R.id.puntosBienvenida)
        btnSiguiente = view.findViewById(R.id.btnBienvenidaSiguiente)
        crearPuntos()

        btnSiguiente?.setOnClickListener { siguiente() }
        view.findViewById<View>(R.id.btnBienvenidaSaltar).setOnClickListener { cerrar() }

        val card = view.findViewById<View>(R.id.cardBienvenida)
        val cola = view.findViewById<View>(R.id.colaBienvenida)
        if (savedInstanceState == null && GuiaPersonitaView.animacionesActivas(view.context)) {
            // Entrada: se oscurece el fondo, llega la personita y luego aparece el globo
            view.alpha = 0f
            view.animate().alpha(1f).setDuration(250).start()
            personita?.entrar()
            for (v in listOf(card, cola)) {
                v.alpha = 0f
                v.translationY = 24f * resources.displayMetrics.density
            }
            card.scaleX = 0.96f
            card.scaleY = 0.96f
            card.animate().alpha(1f).translationY(0f).scaleX(1f).scaleY(1f)
                .setStartDelay(850).setDuration(320).setInterpolator(OvershootInterpolator(1.2f))
                .withEndAction { mostrarMensaje(indice, saludar = true) }
                .start()
            cola.animate().alpha(1f).translationY(0f).setStartDelay(900).setDuration(280)
                .setInterpolator(DecelerateInterpolator()).start()
        } else {
            mostrarMensaje(indice, saludar = false)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun mostrarMensaje(i: Int, saludar: Boolean) {
        val total = Bienvenida.MENSAJES.size
        indice = i.coerceIn(0, total - 1)
        tvPaso?.text = getString(R.string.welcome_overline, indice + 1, total)
        actualizarPuntos()
        val ultimo = indice == total - 1
        btnSiguiente?.setText(if (ultimo) R.string.welcome_start else R.string.welcome_next)
        btnSiguiente?.icon = if (ultimo) null else ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_forward_round)

        textoCompleto = getString(Bienvenida.MENSAJES[indice])
        handler.removeCallbacks(escribir)
        if (saludar) personita?.saludar()
        if (ultimo) personita?.saltar()
        if (escribirLetraPorLetra) {
            letras = 0
            escribiendo = true
            personita?.hablando = true
            tvMensaje?.text = ""
            handler.postDelayed(escribir, 120)
        } else {
            tvMensaje?.text = textoCompleto
            terminarDeEscribir()
        }
    }

    private fun terminarDeEscribir() {
        handler.removeCallbacks(escribir)
        escribiendo = false
        tvMensaje?.text = textoCompleto
        personita?.hablando = false
    }

    private fun siguiente() {
        if (escribiendo) {
            // Primer toque: muestra el mensaje completo
            terminarDeEscribir()
            return
        }
        if (indice >= Bienvenida.MENSAJES.size - 1) cerrar()
        else mostrarMensaje(indice + 1, saludar = false)
    }

    private fun cerrar() {
        context?.let { Bienvenida.marcarVista(it) }
        handler.removeCallbacks(escribir)
        val raiz = view
        if (raiz == null || !GuiaPersonitaView.animacionesActivas(raiz.context)) {
            dismissAllowingStateLoss()
            return
        }
        raiz.animate().alpha(0f).setDuration(200).withEndAction { dismissAllowingStateLoss() }.start()
    }

    override fun onCancel(dialog: DialogInterface) {
        // Botón Atrás del celular: cuenta como "Saltar"
        context?.let { Bienvenida.marcarVista(it) }
        super.onCancel(dialog)
    }

    // ---------- Puntitos de avance ----------

    private fun crearPuntos() {
        val fila = puntos ?: return
        fila.removeAllViews()
        repeat(Bienvenida.MENSAJES.size) {
            fila.addView(View(fila.context).apply {
                background = GradientDrawable().apply { cornerRadius = 3f * resources.displayMetrics.density }
            })
        }
    }

    private fun actualizarPuntos() {
        val fila = puntos ?: return
        val d = resources.displayMetrics.density
        val ctx = fila.context
        for (i in 0 until fila.childCount) {
            val punto = fila.getChildAt(i)
            val activo = i == indice
            punto.layoutParams = LinearLayout.LayoutParams(((if (activo) 18 else 6) * d).toInt(), (6 * d).toInt()).apply {
                marginEnd = (5 * d).toInt()
            }
            (punto.background as? GradientDrawable)?.setColor(
                ContextCompat.getColor(ctx, if (activo) R.color.puriy_rosa else R.color.puriy_borde)
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ESTADO_INDICE, indice)
    }

    override fun onDestroyView() {
        handler.removeCallbacks(escribir)
        personita = null
        tvMensaje = null
        tvPaso = null
        puntos = null
        btnSiguiente = null
        super.onDestroyView()
    }

    private companion object {
        const val ESTADO_INDICE = "indice"
        const val MS_POR_LETRA = 28L
    }
}
