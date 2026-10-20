package com.example.puriyayacucho.data

data class ComidaTipica(
    val nombre: String,
    val imagen: String
)

object ComidaFactory {
    fun obtenerComidasAyacucho(): List<ComidaTipica> = listOf(
        ComidaTipica("Puca Picante", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778791759/puca_picante_cbin83.jpg"),
        ComidaTipica("Cuy Chactado", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778791752/cuy_chactado_niqh92.jpg"),
        ComidaTipica("Mondongo Ayacuchano", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778791756/mondongo_ayacuchano_gkuh01.jpg"),
        ComidaTipica("Qapchi", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778791750/Qapchi_sbm3yo.jpg"),
        ComidaTipica("Chicharrón Ayacuchano", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778791747/chicharron_ayacuchano_j91vvz.jpg")
    )
}
