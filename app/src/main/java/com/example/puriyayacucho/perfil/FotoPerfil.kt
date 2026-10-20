package com.example.puriyayacucho.perfil

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.math.min

/**
 * La foto de perfil se guarda **solo en el celular** (carpeta privada de la app), una por
 * cuenta: si cierras sesión y entra otra persona, cada una ve la suya. No se sube a internet.
 */
object FotoPerfil {

    private const val CARPETA = "perfil"
    /** Lado máximo de la foto guardada (px): de sobra para un círculo de 110 dp. */
    private const val LADO_MAX = 720

    fun archivo(context: Context): File {
        val prefs = context.getSharedPreferences("usuario_sesion", Context.MODE_PRIVATE)
        val correo = prefs.getString("correo_usuario", null)?.trim()?.lowercase()
        val clave = if (correo.isNullOrEmpty()) "invitado" else Integer.toHexString(correo.hashCode())
        return File(File(context.filesDir, CARPETA), "foto_$clave.jpg")
    }

    fun existe(context: Context): Boolean = archivo(context).let { it.exists() && it.length() > 0 }

    fun borrar(context: Context) {
        archivo(context).delete()
    }

    /**
     * Copia la foto elegida: cuadrada (recortada al centro), derecha (según la orientación de
     * la cámara) y liviana. Devuelve true si salió bien.
     */
    suspend fun guardar(context: Context, uri: Uri): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val resolver = context.contentResolver
            // 1) Medidas, sin cargar la imagen entera en memoria
            val medidas = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, medidas) }
            if (medidas.outWidth <= 0 || medidas.outHeight <= 0) return@runCatching false
            var muestra = 1
            while (min(medidas.outWidth, medidas.outHeight) / (muestra * 2) >= LADO_MAX) muestra *= 2

            // 2) Carga reducida
            val original = resolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, BitmapFactory.Options().apply { inSampleSize = muestra })
            } ?: return@runCatching false

            // 3) Cuadrado al centro, escala y giro
            val grados = resolver.openInputStream(uri)?.use { gradosExif(it) } ?: 0
            val lado = min(original.width, original.height)
            val escala = min(1f, LADO_MAX.toFloat() / lado)
            val matriz = Matrix().apply {
                postScale(escala, escala)
                if (grados != 0) postRotate(grados.toFloat())
            }
            val lista = Bitmap.createBitmap(
                original, (original.width - lado) / 2, (original.height - lado) / 2, lado, lado, matriz, true
            )

            // 4) Se escribe en un temporal y luego se reemplaza (nunca queda una foto a medias)
            val destino = archivo(context)
            val fechaAnterior = if (destino.exists()) destino.lastModified() else 0L
            destino.parentFile?.mkdirs()
            val temporal = File(destino.parentFile, destino.name + ".tmp")
            FileOutputStream(temporal).use { lista.compress(Bitmap.CompressFormat.JPEG, 88, it) }
            if (lista !== original) lista.recycle()
            original.recycle()
            if (!temporal.renameTo(destino)) {
                temporal.copyTo(destino, overwrite = true)
                temporal.delete()
            }
            // La fecha del archivo es la "firma" para Glide: debe cambiar aunque guardes dos fotos
            // en el mismo segundo, o se vería la anterior desde la caché
            if (destino.lastModified() <= fechaAnterior) destino.setLastModified(fechaAnterior + 1_000L)
            true
        }.getOrDefault(false)
    }

    private fun gradosExif(entrada: InputStream): Int =
        when (ExifInterface(entrada).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
}
