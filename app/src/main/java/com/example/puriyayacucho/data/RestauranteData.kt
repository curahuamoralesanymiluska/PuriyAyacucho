package com.example.puriyayacucho.data

import com.example.puriyayacucho.R
import android.content.Context

data class Restaurante(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val platos: List<String>,
    val precio: String,
    val rating: Float,
    val ubicacion: String,
    val horario: String,
    val telefono: String = "N/A",
    val imagen: Any = R.drawable.ic_food,
    val videoUrl: String = "",
    val coordenadas: Pair<Double, Double> = Pair(-13.1519, -74.2145),
    val galeria: List<Any> = emptyList(),
    val coordenadasAproximadas: Boolean = false,
    val comodidades: Set<Comodidad> = emptySet(),
    val fuente: String = ""
)

object RestauranteFactory {
    fun obtenerRestaurantesAyacucho(context: Context): List<Restaurante> = listOf(

        Restaurante(
            id = 1,
            nombre = context.getString(R.string.rest_1_name),
            descripcion = context.getString(R.string.rest_1_desc),
            platos = listOf("Chaufa de Quinua", "Puca Picante", "Adobo Ayacuchano", "Ceviche de Trucha", "Bondiola a la Plancha", "Limonada Frozen con Hierba Buena"),
            precio = "S/. 25 - S/. 55",
            rating = 4.7f,
            ubicacion = "Jr. 28 de Julio 540 a media cuadra del Museo Cáceres",
            horario = "08:00 AM - 11:00 PM",
            telefono = "(066) 780199",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778786748/via_via_1_uxdaef.png",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778869397/vid_via_via_zrftfv.mp4",
            coordenadas = Pair(-13.1599, -74.2248),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778786746/via_via_6_iidyou.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778786751/via_via_5_gmm2ih.png",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778786744/via_via2_q9ouvd.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778786741/via_via_4_nxblgo.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778786740/via_via_3_tkmgru.jpg"
            )
        ),

        Restaurante(
            id = 2,
            nombre = context.getString(R.string.rest_2_name),
            descripcion = context.getString(R.string.rest_2_desc),
            platos = listOf("Chicharrón Huamanguino con Qapchi", "Trucha Chactada de Maíz", "Puca Picante", "Tacu Tacu de Frejoles", "Sopa Seca Ayacuchana", "Postres Artesanales"),
            precio = "S/. 35 - S/. 65",
            rating = 4.1f,
            ubicacion = "Portal Independencia N° 60, Plaza Mayor de Huamanga, Ayacucho",
            horario = "12:00 PM - 10:00 PM",
            telefono = "960 994 756",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787085/sukre_1_t18lxd.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778869485/vid_sukre_wkqmhb.mp4",
            coordenadas = Pair(-13.1597, -74.2246),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787078/sukre_6_x0oi5f.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787075/sukre_5_udelnb.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787080/sukre_3_rcsejn.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787082/sukre_2_jojzal.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787076/sukre_4_y5h6bt.jpg"
            )
        ),

        Restaurante(
            id = 3,
            nombre = context.getString(R.string.rest_3_name),
            descripcion = context.getString(R.string.rest_3_desc),
            platos = listOf("Puca Picante", "Trucha Frita", "Chicharrón de Chancho", "Adobo Ayacuchano", "Caldo de Mote", "Macerado de Wayramuña"),
            precio = "S/. 20 - S/. 45",
            rating = 4.6f,
            ubicacion = "Jr. Asamblea N° 138, 2do piso, Ayacucho",
            horario = "10:00 AM - 09:00 PM",
            telefono = "929 659 887",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778777132/la_humanguina_1_zt8lvz.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778869588/vid_huamanguina_fv0qbs.mp4",
            coordenadas = Pair(-13.1605, -74.2251),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778777120/la_humanguina_6_wqx4ao.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778777128/la_humanguina_2_v9ehjd.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778777127/la_humanguina_3_rtptye.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778777123/la_humanguina_4_ppmyxm.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778777122/la_humanguina_5_shefg8.jpg"
            )
        ),

        Restaurante(
            id = 4,
            nombre = context.getString(R.string.rest_4_name),
            descripcion = context.getString(R.string.rest_4_desc),
            platos = listOf("Chancho Asado", "Chicharrón Ayacuchano", "Puca Picante", "Cuy Chactado", "Ceviche Huamanguino", "Macerado de Toronjil"),
            precio = "S/. 20 - S/. 45",
            rating = 4.4f,
            ubicacion = "Jr. José Olaya N° 106, Ayacucho",
            horario = "09:00 AM - 09:00 PM",
            telefono = "977 127 917",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778777973/las_flores_1_q1hwqp.png",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778869744/vid_las_flores_ckjtmw.mp4",
            coordenadas = Pair(-13.1601, -74.2244),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778777965/las_flores_6_u2hey1.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778777966/las_flores_5_fnryij.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778777967/las_flores_4_bcbrzg.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778777969/las_flores_3_tjcb8m.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778777971/las_flores_2_fgg6ea.jpg"
            )
        ),

        Restaurante(
            id = 5,
            nombre = context.getString(R.string.rest_5_name),
            descripcion = context.getString(R.string.rest_5_desc),
            platos = listOf("Trucha a la Parrilla", "Pollo a la Brasa", "Caldo de Mote", "Chicharrón Ayacuchano", "Pizza de Pan Chapla", "Qapchi"),
            precio = "S/. 20 - S/. 40",
            rating = 4.1f,
            ubicacion = "Jr. 9 de Diciembre 205, Ayacucho",
            horario = "08:00 AM - 09:00 PM",
            telefono = "N/A",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778355/el_nino_1_hbewbj.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778869859/vid_nino_ui7sk4.mp4",
            coordenadas = Pair(-13.1610, -74.2258),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778347/el_nino_6_xvpjb4.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778348/el_nino_5_gdxhwq.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778349/el_nino_4_zu3rec.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778351/el_nino_3_cymt18.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778353/el_nino_2_d8vpcn.jpg"
            )
        ),

        Restaurante(
            id = 6,
            nombre = context.getString(R.string.rest_6_name),
            descripcion = context.getString(R.string.rest_6_desc),
            platos = listOf("Quinoto (Risotto de Quinua)", "Chaufa de Quinua", "Pasta Artesanal Ayacuchana", "Pisco Sour", "Lomo Saltado", "Menú Ejecutivo del Día"),
            precio = "S/. 15 - S/. 35",
            rating = 4.4f,
            ubicacion = "Jr. Cusco 241, Ayacucho",
            horario = "08:00 AM - 10:00 PM",
            telefono = "999 848 611",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778637/mestizo_1_nagekw.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778869977/vid_mestizo_vkjfxa.mp4",
            coordenadas = Pair(-13.1607, -74.2252),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778626/mestizo_6_jezddl.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778628/mestizo_5_d6u1wt.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778630/mestizo_4_iushfm.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778632/mestizo_3_wowfmd.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778635/mestizo_2_enlqpq.jpg"
            )
        ),

        Restaurante(
            id = 7,
            nombre = context.getString(R.string.rest_7_name),
            descripcion = context.getString(R.string.rest_7_desc),
            platos = listOf("Tacu Tacu Lomo", "Ribeye Steak", "Trucha a la Plancha", "Chaufa de Alpaca", "Tuna Pisco Sour", "Postres Artesanales"),
            precio = "S/. 35 - S/. 70",
            rating = 5.0f,
            ubicacion = "Jr. 28 de Julio 564, Ayacucho",
            horario = "12:00 PM - 10:00 PM",
            telefono = "924 132 608",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778921/fuziones_1_yxpmz1.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778870077/fuziones_vid_jhydpk.mp4",
            coordenadas = Pair(-13.1614, -74.2255),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778916/fuziones_6_ul8bc0.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778910/fuziones_2_hs4ekx.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778912/fuziones_4_hjhdvv.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778914/fuziones_3_lmp3gz.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778778919/fuziones_5_kkebls.jpg"
            )
        ),

        Restaurante(
            id = 8,
            nombre = context.getString(R.string.rest_8_name),
            descripcion = context.getString(R.string.rest_8_desc),
            platos = listOf("Pizza Calzone Artesanal", "Pizza de Masa Delgada", "Pasta Fetuccini", "Cerveza Artesanal Local", "Sangría", "Bruschetta"),
            precio = "S/. 30 - S/. 55",
            rating = 4.9f,
            ubicacion = "Jr. Lima 138, Ayacucho",
            horario = "06:00 PM - 01:00 AM",
            telefono = "N/A",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778779252/magia_negra_1_ey15rj.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778870137/vid_magia_negra_lpymx3.mp4",
            coordenadas = Pair(-13.1603, -74.2243),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778779242/magia_negra_6_fphb4i.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778779245/magia_negra_4_qwfxf3.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778779247/magia_negra_3_vncscs.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778779250/magia_negra_2_wypbwh.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778779240/magia_negra_5_ke57kx.jpg"
            )
        ),

        Restaurante(
            id = 9,
            nombre = context.getString(R.string.rest_9_name),
            descripcion = context.getString(R.string.rest_9_desc),
            platos = listOf("Tiradito Nikkei de Trucha", "Causa Rellena de Alpaca", "Sushi de Trucha Andina", "Ceviche Nikkei", "Tataki de Trucha", "Pisco Sour Especial"),
            precio = "S/. 30 - S/. 60",
            rating = 4.5f,
            ubicacion = "Jr. Mariano Melgar, Las Nazarenas 519, Ayacucho",
            horario = "12:00 PM - 10:00 PM",
            telefono = "N/A",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778779491/koi_1_sdvzvu.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778870212/koi_vid_uqbedm.mp4",
            coordenadas = Pair(-13.1598, -74.2228),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778779488/koi_6_s0jlhj.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778779486/koi_2_xoua5m.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778779484/koi_3_cfiksf.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778779482/koi_4_pqomgz.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778779479/koi_5_kz4upk.jpg"
            )
        ),

        Restaurante(
            id = 10,
            nombre = context.getString(R.string.rest_10_name),
            descripcion = context.getString(R.string.rest_10_desc),
            platos = listOf("Puca Picante", "Cuy Chactado", "Mondongo Ayacuchano", "Pachamanca", "Lomo Saltado", "Chicharrón de Chancho"),
            precio = "S/. 30 - S/. 60",
            rating = 3.6f,
            ubicacion = "Jr. Bellido 465, Ayacucho",
            horario = "12:00 PM - 10:00 PM",
            telefono = "N/A",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787463/la_casona_1_ahamsl.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778870283/vid_casona_oizi75.mp4",
            coordenadas = Pair(-13.1596, -74.2240),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787461/la_casona_2_amd1pg.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787458/la_casona_3_bcf2ix.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787456/la_casona_4_dhddft.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787454/la_casona_6_qcfdb0.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787452/la_casona_5_mp4u4i.jpg"
            )
        ),

        Restaurante(
            id = 11,
            nombre = context.getString(R.string.rest_11_name),
            descripcion = context.getString(R.string.rest_11_desc),
            platos = listOf("Pollo a la Brasa en Leña", "Parrilla Mixta", "Anticuchos", "Puca Picante", "Limonada de Hierba Luisa", "Ensalada Criolla"),
            precio = "S/. 25 - S/. 50",
            rating = 3.6f,
            ubicacion = "Portal Constitución 8, Plaza Mayor de Huamanga, Ayacucho",
            horario = "12:00 PM - 10:00 PM",
            telefono = "N/A",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787813/la_tinajas_1_xkndji.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778870350/vid_tinajas_nrtvr9.mp4",
            coordenadas = Pair(-13.1600, -74.2249),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787802/la_tinajas_6_iigroi.webp",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787804/la_tinajas_5_y8xuim.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787806/la_tinajas_4_oglqs2.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787808/la_tinajas_3_ugqmjl.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778787811/la_tinajas_2_cfqnty.jpg"
            )
        ),

        Restaurante(
            id = 12,
            nombre = context.getString(R.string.rest_12_name),
            descripcion = context.getString(R.string.rest_12_desc),
            platos = listOf("Puca Picante", "Cuy Chactado", "Pachamanca", "Chicharrón Huamanguino", "Caldo de Gallina", "Menú del Día"),
            precio = "S/. 15 - S/. 40",
            rating = 4.3f,
            ubicacion = "Jr. Teodoro Jáuregui 117, Pío Max, Ayacucho",
            horario = "07:30 AM - 08:00 PM",
            telefono = "N/A",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778788039/kibo_1_mdfcql.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778870495/vid_carbonvino_bdt3sn.mov",
            coordenadas = Pair(-13.1580, -74.2265),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778788037/kibo_6_pwx8pi.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778788034/kibo_2_izukki.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778788032/kibo_3_qha0v0.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778788029/kibo_4_iigw5y.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778788027/kibo_5_wfoojz.jpg"
            )
        ),

        Restaurante(
            id = 13,
            nombre = context.getString(R.string.rest_13_name),
            descripcion = context.getString(R.string.rest_13_desc),
            platos = listOf("Parrilla al Carbón", "Lomo a la Parrilla", "Puca Picante", "Costillas BBQ", "Vino de la Casa", "Ensalada de la Huerta"),
            precio = "S/. 35 - S/. 65",
            rating = 4.2f,
            ubicacion = "Jr. Bellido 593, Segundo Patio, Ayacucho",
            horario = "08:00 AM - 09:00 PM",
            telefono = "N/A",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778788836/carbonvino_1_wazvso.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778870495/vid_carbonvino_bdt3sn.mov",
            coordenadas = Pair(-13.1612, -74.2257),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778788833/carbonvino_6_abmmie.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778788826/carbonvino_4_e0ndgl.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778788831/carbonvino_2_zlzjrr.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778788828/carbonvino_3_yeioki.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778788824/carbonvino_5_db2ohk.jpg"
            )
        ),

        Restaurante(
            id = 14,
            nombre = context.getString(R.string.rest_14_name),
            descripcion = context.getString(R.string.rest_14_desc),
            platos = listOf("Bowl de Quinua con Verduras", "Smoothie de Camu Camu", "Ensalada de Kale Andino", "Hamburguesa Vegana de Kiwicha", "Tacos de Soja", "Jugo Verde Detox"),
            precio = "S/. 15 - S/. 35",
            rating = 4.3f,
            ubicacion = "Portal Independencia N° 60, frente a la Plaza Mayor, Ayacucho",
            horario = "08:00 AM - 09:00 PM",
            telefono = "997 313 953",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789031/planta_lovers_1_ty0n0g.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778870572/vid_plantalovers_iorsip.mp4",
            coordenadas = Pair(-13.1597, -74.2243),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789029/planta_lovers_6_lyzpop.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789026/planta_lovers_2_weozzl.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789023/planta_lovers_3_fhp7ca.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789021/planta_lovers_4_c62ncx.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789019/planta_lovers_5_zq381m.jpg"
            )
        ),

        Restaurante(
            id = 15,
            nombre = context.getString(R.string.rest_15_name),
            descripcion = context.getString(R.string.rest_15_desc),
            platos = listOf("Pollo a la Brasa", "Pollo a la Parrilla", "Hamburguesa Retablo", "Anticuchos de Res", "Parrillada Mixta", "Papas Fritas con Salsa Criolla"),
            precio = "S/. 20 - S/. 45",
            rating = 4.0f,
            ubicacion = "Jr. Asamblea 219, Ayacucho",
            horario = "12:00 PM - 11:00 PM",
            telefono = "N/A",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789326/retablo_1_ojfdss.png",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778870639/retablo_vid_c2nfyf.mp4",
            coordenadas = Pair(-13.1609, -74.2248),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789328/retablo_6_cwqq6m.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789317/retablo_4_ajpblq.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789315/retablo_5_zogisj.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789320/retablo_3_z101gb.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789323/retablo_2_hxs3a6.jpg"
            )
        ),

        Restaurante(
            id = 16,
            nombre = context.getString(R.string.rest_16_name),
            descripcion = context.getString(R.string.rest_16_desc),
            platos = listOf("Tiradito Especial Puka's", "Chaufa de Mariscos", "Chifa de Quinua con Frejolito Chino", "Lomo Saltado", "Sopa Wantán Andina", "Aeropuerto Mixto"),
            precio = "S/. 20 - S/. 45",
            rating = 4.4f,
            ubicacion = "Av. Mariscal Cáceres 1243, Ayacucho",
            horario = "12:00 PM - 09:00 PM",
            telefono = "N/A",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789592/pukas_wasi_1_tt0rh3.png",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778870702/pukas_vid_nedxix.mp4",
            coordenadas = Pair(-13.1573, -74.2301),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789590/pukas_wasi_6_ikuein.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789587/pukas_wasi_2_lxadlk.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789584/pukas_wasi_3_mv4gy3.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789582/pukas_wasi_4_bagzyi.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789579/pukas_wasi_5_blbr5t.jpg"
            )
        ),

        Restaurante(
            id = 17,
            nombre = context.getString(R.string.rest_17_name),
            descripcion = context.getString(R.string.rest_17_desc),
            platos = listOf("Café de Especialidad VRAE", "Café con Leche Artesanal", "Chocolate Ayacuchano Caliente", "Desayuno Huamanguino", "Pastel de Quinua", "Sandwich Artesanal"),
            precio = "S/. 8 - S/. 25",
            rating = 5.0f,
            ubicacion = "Jr tres mascaras 510, Centro Histórico, Ayacucho",
            horario = "07:00 AM - 09:00 PM",
            telefono = "N/A",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789914/antaqa_1_xubu1w.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778870834/antaqa_vid_nnyzkn.mp4",
            coordenadas = Pair(-13.1604, -74.2247),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789911/antaqa_6_sq6wyj.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789905/antaqa_3_ul7gbd.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789908/antaqa_2_vjenan.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789902/antaqa_4_zy6bgd.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778789900/antaqa_5_sjd5as.jpg"
            )
        ),

        Restaurante(
            id = 18,
            nombre = context.getString(R.string.rest_18_name),
            descripcion = context.getString(R.string.rest_18_desc),
            platos = listOf("Trucha en Salsa de Huacatay", "Ceviche de Trucha", "Papa Rellena Huamanguina", "Arroz con Leche de Quinua", "Menú Ejecutivo Colonial", "Chicha Morada Artesanal"),
            precio = "S/. 18 - S/. 40",
            rating = 5.0f,
            ubicacion = "Jr. Cusco 252, Centro Histórico, Ayacucho",
            horario = "08:00 AM - 08:00 PM",
            telefono = "N/A",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790071/patio_colonial_1_m8jrzc.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778870916/patio_vid_agxgve.mp4",
            coordenadas = Pair(-13.1606, -74.2253),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790068/patio_colonial_2_aln7vk.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790065/patio_colonial_3_eperwn.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790062/patio_colonial_4_frkohx.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790076/patio_colonial_5_jj8ncj.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790073/patio_colonial_6_j76rz1.jpg"
            )
        ),

        Restaurante(
            id = 19,
            nombre = context.getString(R.string.rest_19_name),
            descripcion = context.getString(R.string.rest_19_desc),
            platos = listOf("Mondongo Huamanguino", "Chorizo Huamanguino", "Qapchi Blanco", "Adobo Ayacuchano", "Caldo de Cabeza", "Chicha de Jora"),
            precio = "S/. 12 - S/. 30",
            rating = 4.3f,
            ubicacion = "Jirón Los Alamos 260, Ayacucho",
            horario = "07:00 AM - 03:00 PM",
            telefono = "999 992 769",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790399/tu_casa_1_kkup7w.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778871003/tucasa_vid_sxjdvh.mp4",
            coordenadas = Pair(-13.164206274235335, -74.21452334662207),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790396/tu_casa_2_hzbmxm.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790386/tu_casa_6_oumxdx.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790388/tu_casa_5_vdifoa.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790391/tu_casa_4_azscnl.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790394/tu_casa_3_tya0qw.jpg"
            )
        ),

        Restaurante(
            id = 20,
            nombre = context.getString(R.string.rest_20_name),
            descripcion = context.getString(R.string.rest_20_desc),
            platos = listOf("Puca Picante", "Cuy Chactado", "Pachamanca a la Olla", "Chicharrón de Chancho", "Trucha Frita con Papas Nativas", "Chicha Morada Natural"),
            precio = "S/. 25 - S/. 55",
            rating = 4.6f,
            ubicacion = "Av. Circunvalación s/n, sector Acuchimay, Ayacucho",
            horario = "11:00 AM - 06:00 PM",
            telefono = "N/A",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790765/cameykar_1_q45pd9.png",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778871118/cameycar_vid_cmir39.mp4",
            coordenadas = Pair(-13.1550, -74.2320),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790777/cameykar_6_xdotsm.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790768/cameykar_2_ery2fl.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790771/cameykar_3_ztzxjr.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778790774/cameykar_4_qhokxx.jpg"
            )
        )
    )
}
