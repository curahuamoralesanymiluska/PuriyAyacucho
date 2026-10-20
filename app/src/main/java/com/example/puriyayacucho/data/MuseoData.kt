package com.example.puriyayacucho.data

import android.content.Context
import com.example.puriyayacucho.R

data class Museo(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val contenido: String,
    val horario: String,
    val ubicacion: String,
    val imagen: Any = R.drawable.ic_museum,
    val coordenadas: Pair<Double, Double> = Pair(-13.1519, -74.2145),
    val videoUrl: String = "dQw4w9WgXcQ",
    val galeria: List<Any> = emptyList()
)

object MuseoFactory {
    fun obtenerMuseosAyacucho(context: Context): List<Museo> = listOf(

        Museo(
            id = 1,
            nombre = context.getString(R.string.museo_1_name),
            descripcion = context.getString(R.string.museo_1_desc),
            contenido = context.getString(R.string.museo_1_content),
            horario = "09:00 AM - 05:00 PM",
            ubicacion = "Av. Independencia 502, Centro Cultural Simón Bolívar, Ayacucho",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778815369/hipolito_1_bgugsn.jpg",
            coordenadas = Pair(-13.1588, -74.2237),
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778868613/museo_hipolito_yphn7o.mp4",
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778815368/hipolito_2_m5mi9z.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778815369/hipolito_4_hrao6u.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778815368/hipolito_5_toodal.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778815368/hipolito_3_g7hz7w.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778815368/hipolito_6_fltubh.jpg"
            )
        ),

        Museo(
            id = 2,
            nombre = context.getString(R.string.museo_2_name),
            descripcion = context.getString(R.string.museo_2_desc),
            contenido = context.getString(R.string.museo_2_content),
            horario = "09:00 AM - 05:00 PM",
            ubicacion = "Km. 23 Carretera Ayacucho–Quinua, Pacaycasa, Huamanga",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778815656/warii_1_r715rr.jpg",
            coordenadas = Pair(-13.0768, -74.1930),
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778868598/museo_wari_gdrebr.mp4",
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778815658/warii_2_kowsyw.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778815658/warii_4_yturwk.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778815658/warii_5_tprwur.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778815658/warii_6_qbp3zq.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778815657/warii_3_xee91o.jpg"           )

        ),

        Museo(
            id = 3,
            nombre = context.getString(R.string.museo_3_name),
            descripcion = context.getString(R.string.museo_3_desc),
            contenido = context.getString(R.string.museo_3_content),
            horario = "09:00 AM - 05:00 PM",
            ubicacion = "Plaza Principal de Quinua, a 35 km al noreste de Ayacucho",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816399/quinua_1_hvfxp1.jpg",
            coordenadas = Pair(-13.0366, -74.1333),
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778868585/museo_quinua_shxa7t.mp4",
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816400/quinua_2_bxd4jw.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816399/quinua_6_iqmbqr.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816399/quinua_5_vb5zbi.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816400/quinua_3_jy4add.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816400/quinua_4_bliu9x.jpg"
            )
        ),

        Museo(
            id = 4,
            nombre = context.getString(R.string.museo_4_name),
            descripcion = context.getString(R.string.museo_4_desc),
            contenido = context.getString(R.string.museo_4_content),
            horario = "09:00 AM - 01:00 PM / 03:00 PM - 06:00 PM",
            ubicacion = "Jr. 28 de Julio 508, Ayacucho",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816725/caceres_1_g2c7mz.jpg",
            coordenadas = Pair(-13.1612, -74.2256),
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778868623/museo_caceres_pr6uox.mp4",
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816725/caceres_2_s9buts.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816724/caceres_4_ctruq7.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816724/caceres_3_mnei8s.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816723/caceres_5_uu3q6v.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816717/caceres_6_kh6dhf.jpg"
            )
        ),

        Museo(
            id = 5,
            nombre = context.getString(R.string.museo_5_name),
            descripcion = context.getString(R.string.museo_5_desc),
            contenido = context.getString(R.string.museo_5_content),
            horario = "09:00 AM - 01:00 PM / 03:00 PM - 06:00 PM (Lun–Vie) / 09:00 AM - 01:00 PM (Sáb)",
            ubicacion = "Prolongación Libertad, Cuadra 14 (esq. Parque de la Memoria), Ayacucho",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816964/memoria_1_bgvium.jpg",
            coordenadas = Pair(-13.1523, -74.2198),
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778868628/museo_memoria_dg0pbc.mp4",
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816964/memoria_2_am8hci.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816962/memoria_4_zsbyao.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816962/memoria_6_mfperm.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778816962/memoria_5_gpt3nu.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778817028/memoria_3_ceqt2b.jpg"
            )
        ),

        Museo(
            id = 6,
            nombre = context.getString(R.string.museo_6_name),
            descripcion = context.getString(R.string.museo_6_desc),
            contenido = context.getString(R.string.museo_6_content),
            horario = "09:00 AM - 01:00 PM / 03:00 PM - 06:00 PM",
            ubicacion = "Jr. Cusco 424, Barrio de Santa Ana, Ayacucho",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778817258/antay_1_oyugzq.webp",
            coordenadas = Pair(-13.1595, -74.2230),
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778868618/museo_antay_lm8z4i.mp4",
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778817257/antay_2_te22t1.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778817257/antay_3_adbd53.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778817256/antay_4_haywf4.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778817255/antay_6_q0je3j.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778817255/antay_5_arou8t.jpg"
            )
        ),

        Museo(
            id = 7,
            nombre = context.getString(R.string.museo_7_name),
            descripcion = context.getString(R.string.museo_7_desc),
            contenido = context.getString(R.string.museo_7_content),
            horario = "09:00 AM - 06:00 PM",
            ubicacion = "Barrio Belén, Ayacucho (aprox. 15 min en taxi desde la Plaza Mayor)",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778817449/retabloo_1_l6nmhu.jpg",
            coordenadas = Pair(-13.1640, -74.2190),
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778868608/museo_casa_retablo_xxkoho.mp4",
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778817448/retabloo_2_bzhryt.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778817448/retabloo_4_urdomv.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778817447/retabloo_3_hjewl9.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778817446/retabloo_5_dhqjjd.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778817446/retabloo_6_ejbonq.jpg"
            )
        ),

        Museo(
            id = 9,
            nombre = context.getString(R.string.museo_9_name),
            descripcion = context.getString(R.string.museo_9_desc),
            contenido = context.getString(R.string.museo_9_content),
            horario = "09:00 AM - 05:00 PM",
            ubicacion = "Sector San José s/n, Quinua, Huamanga, Ayacucho",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818102/san_jose_1_qgthsm.jpg",
            coordenadas = Pair(-13.0390, -74.1310),
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778868633/museo_textil_t6etth.mp4",
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818101/san_jose_3_bvscwl.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818101/san_jose_2_sdff1d.jpg",

            )
        ),

        Museo(
            id = 10,
            nombre = context.getString(R.string.museo_10_name),
            descripcion = context.getString(R.string.museo_10_desc),
            contenido = context.getString(R.string.museo_10_content),
            horario = "09:00 AM - 05:00 PM",
            ubicacion = "Distrito de Vischongo, Provincia de Vilcashuamán, Ayacucho (118 km al sureste de Ayacucho)",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818272/vilcas_1_ekfji2.jpg",
            coordenadas = Pair(-13.7104, -74.0467),
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778868654/museo_vilcashuaman_u9ydpb.mp4",
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818271/vilcas_2_tyxkpi.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818270/vilcas_3_r300vl.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818269/vilcas_4_z8begz.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818269/vilcas_5_en5buo.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818268/vilcas_6_j2hjmz.jpg"
            )
        ),

        Museo(
            id = 11,
            nombre = context.getString(R.string.museo_11_name),
            descripcion = context.getString(R.string.museo_11_desc),
            contenido = context.getString(R.string.museo_11_content),
            horario = "09:00 AM - 12:00 PM / 03:00 PM - 05:30 PM",
            ubicacion = "Portal Municipal s/n, Plaza Mayor de Huamanga, Ayacucho",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818639/catedral_1_dt6eax.jpg",
            coordenadas = Pair(-13.1598, -74.2248),
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778868638/museo_catedral_k301cw.mp4",
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818637/catedral_3_mrki5l.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818638/catedral_2_swyftt.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818637/catedral_4_qrjiuo.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818636/catedral_6_nz6byb.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818636/catedral_5_jp4toj.jpg"
            )
        ),

        Museo(
            id = 12,
            nombre = context.getString(R.string.museo_12_name),
            descripcion = context.getString(R.string.museo_12_desc),
            contenido = context.getString(R.string.museo_12_content),
            horario = "08:00 AM - 09:00 PM",
            ubicacion = "Jr. Asamblea 164, Centro Histórico, Ayacucho",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818877/cafe_museo_1_ee6s7f.jpg",
            coordenadas = Pair(-13.1602, -74.2245),
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778868603/museo_cafe_fil7rz.mp4",
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818876/cafe_museo_3_echfac.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778818878/cafe_museo_2_y9osus.jpg"

            )
        ),

        Museo(
            id = 13,
            nombre = context.getString(R.string.museo_13_name),
            descripcion = context.getString(R.string.museo_13_desc),
            contenido = context.getString(R.string.museo_13_content),
            horario = "09:00 AM - 05:00 PM",
            ubicacion = "Pampa de la Quinua, km 37 carretera Ayacucho–Quinua, Huamanga",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819242/obelisco_1_sp74qn.jpg",
            coordenadas = Pair(-13.0190, -74.1130),
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778868594/museo_obelisco_umfmad.mp4",
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819241/obelisco_2_fnufmg.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819240/obelisco_3_a4bwru.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819240/obelisco_4_zoafrb.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819239/obelisco_6_fqgt1p.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819239/obelisco_5_bexnpo.jpg"
            )
        ),

        Museo(
            id = 14,
            nombre = context.getString(R.string.museo_14_name),
            descripcion = context.getString(R.string.museo_14_desc),
            contenido = context.getString(R.string.museo_14_content),
            horario = "09:00 AM - 01:00 PM / 03:00 PM - 06:00 PM",
            ubicacion = "Jr. 28 de Julio, Centro Histórico, Ayacucho",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819534/la_casonaa_2_xijc7g.jpg",
            coordenadas = Pair(-13.1608, -74.2252),
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778868588/museo_bozasolis_m09bw2.mp4",
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819534/la_casonaa_2_xijc7g.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819532/la_casonaa_3_q2qnvm.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819531/la_casonaa_4_ikhtep.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819530/la_casonaa_5_fhgtv5.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819529/la_casonaa_6_fee7cw.avif"
            )
        ),

        Museo(
            id = 15,
            nombre = context.getString(R.string.museo_15_name),
            descripcion = context.getString(R.string.museo_15_desc),
            contenido = context.getString(R.string.museo_15_content),
            horario = "09:00 AM - 05:00 PM",
            ubicacion = "Sector Cuchoquesera, km 24 vía Ayacucho–Cangallo, Huamanga",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819793/cueva_piquimachay_1_uzfmof.jpg",
            coordenadas = Pair(-13.2250, -74.2800),
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778869108/piki_cepjhq.mp4",
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819792/cueva_piquimachay_2_yauinu.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819791/cueva_piquimachay_5_r6cqfw.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819789/cueva_piquimachay_4_g14ezf.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778819789/cueva_piquimachay_3_dgeggg.jpg",

            )
        ),
    )
}
