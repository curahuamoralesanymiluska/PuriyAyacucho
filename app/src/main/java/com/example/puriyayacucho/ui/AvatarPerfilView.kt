package com.example.puriyayacucho.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.puriyayacucho.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.ShapeAppearanceModel
import java.io.File
import kotlin.math.max

/**
 * Foto de perfil redonda. Mientras no hay foto se ve una **personita con chullo**
 * (`ic_avatar_persona`) y una insignia de cámara que invita a tocarla para poner tu foto.
 *
 * ```xml
 * <com.example.puriyayacucho.ui.AvatarPerfilView
 *     android:id="@+id/avatarPerfil"
 *     android:layout_width="72dp"
 *     android:layout_height="72dp"
 *     app:avatarFondo="claro" />   <!-- "rosa" sobre degradados rosados -->
 * ```
 *
 * Quien la usa decide qué pasa al tocarla (abrir la galería) y llama a [mostrar] con el
 * archivo de la foto (o null).
 */
class AvatarPerfilView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val sobreRosa: Boolean
    private val densidad = resources.displayMetrics.density
    private val foto: ShapeableImageView
    private val insignia: ImageView
    private val personita: Int

    init {
        var rosa = false
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.AvatarPerfilView)
            rosa = a.getInt(R.styleable.AvatarPerfilView_avatarFondo, 0) == 1
            a.recycle()
        }
        sobreRosa = rosa
        personita = if (rosa) R.drawable.ic_avatar_persona_blanca else R.drawable.ic_avatar_persona

        val borde = 2.5f * densidad
        foto = ShapeableImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setAllCornerSizes(RelativeCornerSize(0.5f))
                .build()
            strokeWidth = borde
            strokeColor = ColorStateList.valueOf(if (rosa) Color.WHITE else Color.parseColor("#FF3355"))
            // El borde se dibuja a caballo del contorno: este relleno evita que se corte
            val relleno = (borde / 2f).toInt() + 1
            setPadding(relleno, relleno, relleno, relleno)
            scaleType = ImageView.ScaleType.CENTER_CROP
            importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
        }
        insignia = ImageView(context).apply {
            layoutParams = LayoutParams((26 * densidad).toInt(), (26 * densidad).toInt(), Gravity.BOTTOM or Gravity.END)
            setBackgroundResource(if (rosa) R.drawable.bg_avatar_insignia_blanca else R.drawable.bg_avatar_insignia)
            setImageResource(R.drawable.ic_camera)
            imageTintList = ColorStateList.valueOf(if (rosa) Color.parseColor("#FF3355") else Color.WHITE)
            scaleType = ImageView.ScaleType.FIT_CENTER
            elevation = 2f * densidad
            importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
        }
        addView(foto)
        addView(insignia)
        isClickable = true
        isFocusable = true
        mostrarPersonita()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // La insignia mide ~30 % del avatar (mínimo 22 dp), así se ve bien en 64 dp y en 110 dp
        val ancho = MeasureSpec.getSize(widthMeasureSpec)
        if (ancho > 0) {
            val lado = max((ancho * 0.3f).toInt(), (22 * densidad).toInt())
            val lp = insignia.layoutParams as LayoutParams
            if (lp.width != lado) {
                lp.width = lado
                lp.height = lado
                val p = (lado * 0.24f).toInt()
                insignia.setPadding(p, p, p, p)
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    /** Muestra la foto guardada o, si no hay, la personita. */
    fun mostrar(archivo: File?) {
        if (archivo == null || !archivo.exists() || archivo.length() == 0L) {
            mostrarPersonita()
            return
        }
        contentDescription = context.getString(R.string.profile_photo_change)
        runCatching {
            Glide.with(foto)
                .load(archivo)
                // La firma cambia cada vez que guardas otra foto: así no queda la anterior en caché
                .signature(ObjectKey(archivo.lastModified()))
                .centerCrop()
                .placeholder(personita)
                .error(personita)
                .into(foto)
        }.onFailure { foto.setImageResource(personita) }
    }

    private fun mostrarPersonita() {
        runCatching { Glide.with(foto).clear(foto) }
        foto.setImageResource(personita)
        contentDescription = context.getString(R.string.profile_photo_add)
    }
}
