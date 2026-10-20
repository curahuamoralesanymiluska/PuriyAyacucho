package com.example.puriyayacucho

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.puriyayacucho.data.SitioRA
import com.example.puriyayacucho.data.SitioRAFactory
import com.example.puriyayacucho.ra.NarradorRA
import com.google.android.material.card.MaterialCardView
import com.google.android.material.slider.Slider
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Scale
import io.github.sceneview.node.ModelNode

/**
 * Realidad aumentada con ARCore: detecta el suelo, coloca la reconstrucción 3D
 * del sitio anclada en el mundo real y permite caminar alrededor de ella.
 * El modelo se puede mover, girar y escalar con los dedos.
 *
 * Si el celular no es compatible con ARCore se usa RAActivity (versión simple).
 */
class ARCoreActivity : BaseActivity() {

    companion object {
        private const val EXTRA_SITIO_ID = "sitio_id"
        private const val EXTRA_DEMO = "modo_demo"

        fun abrir(context: Context, sitio: SitioRA, modoDemo: Boolean) {
            context.startActivity(Intent(context, ARCoreActivity::class.java).apply {
                putExtra(EXTRA_SITIO_ID, sitio.id)
                putExtra(EXTRA_DEMO, modoDemo)
            })
        }
    }

    private lateinit var sitio: SitioRA
    private var modoDemo = false
    private lateinit var arView: ARSceneView
    private lateinit var tvInstruccion: TextView
    private lateinit var cardInfo: MaterialCardView
    private lateinit var narrador: NarradorRA

    private var anchorNode: AnchorNode? = null
    private var modelNode: ModelNode? = null
    private var escalaBase: Scale? = null
    private var progreso = 0.7f
    private var indicePunto = 0
    private var errorAlCargarModelo = false

    private val pedirCamara = registerForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        if (concedido) {
            recreate()
        } else {
            Toast.makeText(this, R.string.ra_camera_denied, Toast.LENGTH_LONG).show()
            abrirModoSimple()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sitio = SitioRAFactory.porId(intent.getIntExtra(EXTRA_SITIO_ID, -1)) ?: run {
            finish()
            return
        }
        modoDemo = intent.getBooleanExtra(EXTRA_DEMO, false)

        // ARCore necesita la cámara antes de crear la sesión
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            pedirCamara.launch(Manifest.permission.CAMERA)
            return
        }

        setContentView(R.layout.activity_arcore)
        tvInstruccion = findViewById(R.id.tvArInstruccion)
        cardInfo = findViewById(R.id.cardArInfo)

        findViewById<TextView>(R.id.tvArUbicacion).text =
            if (modoDemo) getString(R.string.ra_demo_mode, getString(sitio.nombre))
            else getString(R.string.ra_you_are_at, getString(sitio.nombre))

        configurarAR()
        configurarControles()
    }

    private fun configurarAR() {
        arView = findViewById(R.id.arSceneView)
        arView.lifecycle = lifecycle
        arView.planeRenderer.isEnabled = true
        arView.configureSession { session, config ->
            config.depthMode = when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                true -> Config.DepthMode.AUTOMATIC
                else -> Config.DepthMode.DISABLED
            }
            config.instantPlacementMode = Config.InstantPlacementMode.DISABLED
            config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
        }
        // Cuando ARCore detecta el primer plano horizontal, colocamos ahí la reconstrucción
        arView.onSessionUpdated = { _, frame ->
            if (anchorNode == null && !errorAlCargarModelo) {
                frame.getUpdatedPlanes()
                    .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                    ?.let { plane -> colocarModelo(plane.createAnchor(plane.centerPose)) }
            }
        }
        arView.onTrackingFailureChanged = { razon -> mostrarEstadoSeguimiento(razon) }
        arView.onSessionFailed = { _ ->
            Toast.makeText(this, R.string.arcore_not_available, Toast.LENGTH_LONG).show()
            abrirModoSimple()
        }
    }

    private fun colocarModelo(anchor: Anchor) {
        val nodoAncla = AnchorNode(arView.engine, anchor).apply {
            isEditable = true // mover, girar y escalar con los dedos
        }
        try {
            val instancia = arView.modelLoader.createModelInstance(sitio.modelo3D)
            val modelo = ModelNode(
                modelInstance = instancia,
                scaleToUnits = sitio.tamanoMetros,
                centerOrigin = Position(y = -1.0f) // la base del modelo queda sobre el suelo
            )
            nodoAncla.addChildNode(modelo)
            arView.addChildNode(nodoAncla)
            anchorNode = nodoAncla
            modelNode = modelo
            escalaBase = modelo.scale.let { Scale(it.x, it.y, it.z) }
            aplicarProgreso()
            tvInstruccion.setText(R.string.arcore_placed)
        } catch (e: Exception) {
            anchor.detach()
            errorAlCargarModelo = true
            Toast.makeText(this, R.string.arcore_model_error, Toast.LENGTH_LONG).show()
        }
    }

    /** La barra Hoy ↔ Reconstrucción hace "crecer" los muros desde las ruinas. */
    private fun aplicarProgreso() {
        val base = escalaBase ?: return
        modelNode?.scale = Scale(base.x, base.y * (0.15f + 0.85f * progreso), base.z)
    }

    private fun reiniciar() {
        anchorNode?.let { nodo ->
            arView.removeChildNode(nodo)
            nodo.anchor.detach()
            nodo.destroy()
        }
        anchorNode = null
        modelNode = null
        escalaBase = null
        errorAlCargarModelo = false
        tvInstruccion.setText(R.string.arcore_move_phone)
    }

    private fun mostrarEstadoSeguimiento(razon: TrackingFailureReason?) {
        if (anchorNode != null) return
        tvInstruccion.setText(
            when (razon) {
                null, TrackingFailureReason.NONE -> R.string.arcore_move_phone
                TrackingFailureReason.INSUFFICIENT_LIGHT -> R.string.arcore_more_light
                TrackingFailureReason.EXCESSIVE_MOTION -> R.string.arcore_move_slower
                TrackingFailureReason.INSUFFICIENT_FEATURES -> R.string.arcore_more_texture
                else -> R.string.arcore_move_phone
            }
        )
    }

    private fun configurarControles() {
        narrador = NarradorRA(this) { hablando ->
            findViewById<ImageView>(R.id.ivArNarracion).setImageResource(
                if (hablando) R.drawable.ic_stop else R.drawable.ic_volume
            )
        }
        findViewById<TextView>(R.id.tvArIdioma).text = narrador.codigoIdioma

        findViewById<Slider>(R.id.sliderAr).addOnChangeListener { _, valor, _ ->
            progreso = valor / 100f
            aplicarProgreso()
        }
        findViewById<View>(R.id.btnArCerrar).setOnClickListener { finish() }
        findViewById<View>(R.id.btnArReiniciar).setOnClickListener { reiniciar() }
        findViewById<View>(R.id.btnArModoSimple).setOnClickListener { abrirModoSimple() }
        findViewById<View>(R.id.btnArNarracion).setOnClickListener {
            if (!narrador.alternar(sitio.narracion)) {
                Toast.makeText(this, R.string.ra_tts_unavailable, Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<View>(R.id.btnArIdioma).setOnClickListener {
            narrador.siguienteIdioma(sitio.narracion)
            findViewById<TextView>(R.id.tvArIdioma).text = narrador.codigoIdioma
        }
        // Muestra los puntos de interés del sitio uno por uno
        findViewById<View>(R.id.btnArInfo).setOnClickListener {
            if (sitio.puntosInteres.isEmpty()) return@setOnClickListener
            if (cardInfo.visibility == View.VISIBLE && indicePunto == 0) {
                cardInfo.visibility = View.GONE
                return@setOnClickListener
            }
            val punto = sitio.puntosInteres[indicePunto]
            findViewById<TextView>(R.id.tvArInfoTitulo).setText(punto.titulo)
            findViewById<TextView>(R.id.tvArInfoDetalle).setText(punto.descripcion)
            cardInfo.visibility = View.VISIBLE
            indicePunto = (indicePunto + 1) % sitio.puntosInteres.size
        }
        cardInfo.setOnClickListener { cardInfo.visibility = View.GONE }
    }

    /** Versión simple (sin ARCore): reconstrucción dibujada sobre la cámara. */
    private fun abrirModoSimple() {
        RAActivity.abrir(this, sitio, modoDemo)
        finish()
    }

    override fun onDestroy() {
        if (::narrador.isInitialized) narrador.cerrar()
        super.onDestroy()
    }
}
