package com.example.puriyayacucho.data

import android.content.Context
import com.example.puriyayacucho.R

data class LugarNatural(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val altitud: String,
    val actividades: List<String>,
    val acceso: String,
    val mejorEpoca: String,
    val ubicacion: String,
    val imagen: Any = R.drawable.ic_nature, // Cambiado a Any
    val coordenadas: Pair<Double, Double> = Pair(-13.1519, -74.2145),
    val videoUrl: String = "dQw4w9WgXcQ",
    val galeria: List<Any> = emptyList() // Cambiado a Any
)

object LugarNaturalFactory {
    fun obtenerLugaresNaturalesAyacucho(context: Context): List<LugarNatural> = listOf(
        LugarNatural(
            id = 1,
            nombre = context.getString(R.string.nat_1_name),
            descripcion = context.getString(R.string.nat_1_desc),
            altitud = "3,600 m.s.n.m.",
            actividades = listOf("Senderismo", "Fotografía", "Exploración"),
            acceso = "A 1.5 horas en auto desde Huamanga vía carretera Libertadores",
            mejorEpoca = "Abril a noviembre",
            ubicacion = "Distrito de Vinchos, Huamanga",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778460623/Bosque_huaraca1_kug6nr.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778433565/Bosque_huaraca_vowrg8.mp4",
            coordenadas = Pair(-13.290073306108487, -74.45114903284062),
            galeria = listOf("https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778460626/Bosque_huaraca2_p2fzpa.jpg","https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778460624/Bosque_huaraca3_aoodpq.jpg")
        ),

        LugarNatural(
            id = 2,
            nombre = context.getString(R.string.nat_2_name),
            descripcion = context.getString(R.string.nat_2_desc),
            altitud = "3,800 m.s.n.m.",
            actividades = listOf("Fotografía", "Caminata", "Exploración"),
            acceso = "Aproximadamente 2 horas en auto y caminata desde Huamanga",
            mejorEpoca = "Mayo a octubre",
            ubicacion = "Provincia de Víctor Fajardo, departamento de Ayacucho, Perú",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778461004/Chapalla1_n1e1ax.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778433795/Chapalla_Cenote_Andino_pdrnch.mp4",
            coordenadas = Pair(-13.648745463955835, -74.24316998796797),
            galeria = listOf("https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778461006/Chapalla2_txtlb3.png", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778461004/Chapalla3_ifcuz6.jpg")
        ),

        LugarNatural(
            id = 3,
            nombre = context.getString(R.string.nat_3_name),
            descripcion = context.getString(R.string.nat_3_desc),
            altitud = "2,900 m.s.n.m.",
            actividades = listOf("Fotografía", "Observación", "Caminata"),
            acceso = "A 10-15 minutos en auto desde el centro de Huamanga",
            mejorEpoca = "Atardecer",
            ubicacion = "Cerro La Picota, Huamanga",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778461559/MIRADOR_INTIWARKUNA1_kvq6fy.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778434022/MIRADOR_INTIWARKUNA_ute8kq.mp4",
            coordenadas = Pair(-13.150976361395855, -74.2396064289246),
            galeria = listOf("https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778461556/MIRADOR_INTIWARKUNA2_nqnzxx.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778461557/MIRADOR_INTIWARKUNA3_kgkv9l.png")
        ),

        LugarNatural(
            id = 4,
            nombre = context.getString(R.string.nat_4_name),
            descripcion = context.getString(R.string.nat_4_desc),
            altitud = "3,400 m.s.n.m.",
            actividades = listOf("Trekking", "Fotografía", "Aventura"),
            acceso = "A 40 minutos en auto desde Huamanga + caminata",
            mejorEpoca = "Abril a noviembre",
            ubicacion = "Huamanga",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778461744/Grietas_de_Qorihuillca1_nswvhh.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778434260/Grietas_de_Qorihuillca_mfsgik.mp4",
            coordenadas = Pair(-13.139015632290906, -74.24814173146498),
            galeria = listOf("https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778461747/Grietas_de_Qorihuillca2_nirolk.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778461745/Grietas_de_Qorihuillca3_w6sads.jpg")
        ),

        LugarNatural(
            id = 5,
            nombre = context.getString(R.string.nat_5_name),
            descripcion = context.getString(R.string.nat_5_desc),
            altitud = "3,200 m.s.n.m.",
            actividades = listOf("Caminata", "Fotografía", "Ecoturismo"),
            acceso = "A 1 hora en auto desde Huamanga",
            mejorEpoca = "Diciembre a marzo",
            ubicacion = "Sarhua, distrito de la Provincia de Fajardo Ayacucho",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778461974/Campanayuq1_enrmb7.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778434511/Cascadas_Rurales_de_Campanayuq_gzvk7s.mp4",
            coordenadas = Pair(-13.686105181685695, -74.36143094600386),
            galeria = listOf("https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778461978/Campanayuq2_zg6iw6.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778461976/Campanayuq3_vuxmya.jpg")
        ),

        LugarNatural(
            id = 6,
            nombre = context.getString(R.string.nat_6_name),
            descripcion = context.getString(R.string.nat_6_desc),
            altitud = "3,500 m.s.n.m.",
            actividades = listOf("Observación de fauna", "Senderismo", "Fotografía"),
            acceso = "A 2 horas en auto desde Huamanga",
            mejorEpoca = "Mayo a octubre",
            ubicacion = "Valle del Sondondo, provincia de Lucanas",
            imagen ="https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778685542/rutacondores_sa1oxd.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778434953/Ruta_Encantada_de_los_C%C3%B3ndores_y91yb9.mp4",
            coordenadas = Pair(-14.300862450333453, -73.96769065673749),
            galeria = listOf("https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778685542/rutacondores1_qiw0ag.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778685542/rutacondores2_e1yaw7.jpg")
        ),

        LugarNatural(
            id = 7,
            nombre = context.getString(R.string.nat_7_name),
            descripcion = context.getString(R.string.nat_7_desc),
            altitud = "3,200 m.s.n.m.",
            actividades = listOf("Caminata", "Fotografía", "Ciclismo"),
            acceso = "A 45 minutos en auto desde Huamanga",
            mejorEpoca = "Todo el año",
            ubicacion = "Distrito de Quinua, Huamanga",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778435964/Senderos_Naturales_de_Quinua_q853ah.mp4",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778685814/senderoquinua1_vrcggp.jpg",
            coordenadas = Pair(-13.044554801576032, -74.13522598433313),
            galeria = listOf("https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778685813/senderoquinua2_rh5qyl.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778685813/sendero_quinua_3_cgljbj.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778685813/sendero_quinua_4_oezxaw.jpg")
        ),

        LugarNatural(
            id = 8,
            nombre = context.getString(R.string.nat_8_name),
            descripcion = context.getString(R.string.nat_8_desc),
            altitud = "2,800 m.s.n.m.",
            actividades = listOf("Senderismo", "Exploración", "Historia natural"),
            acceso = "A 30 minutos en auto desde Huamanga",
            mejorEpoca = "Abril a noviembre",
            ubicacion = "Distrito de Pacaycasa, Huamanga",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778436378/Zona_Natural_de_Piquimachay_tevdyg.mp4",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778686353/pikimachay_2_mkzupc.jpg",
            coordenadas = Pair(-13.035421544128495, -74.22827698663576),
            galeria = listOf("https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778686353/pikimachay_1_hlsp8l.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778686353/pikimachay_3_b9lhx5.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778686353/pikimachay_4_ps1syk.jpg")
        ),

        LugarNatural(
            id = 9,
            nombre = context.getString(R.string.nat_9_name),
            descripcion = context.getString(R.string.nat_9_desc),
            altitud = "3,900 m.s.n.m.",
            actividades = listOf("Fotografía", "Observación", "Caminata"),
            acceso = "A 2 horas en auto desde Huamanga + caminata",
            mejorEpoca = "Mayo a octubre",
            ubicacion = "Chuschi (Cangallo)",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778686787/Yanacocha_1_c7dddd.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778449501/Laguna_de_Yanacocha_Ayacucho_v7dite.mp4",
            coordenadas = Pair(-13.210576054966987, -73.92188555003375),
            galeria = listOf("https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778686786/yanacocha_2_tnh8xd.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778686787/yanacocha_4_wdqbjc.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778686787/yanacocha_3_u7kuer.jpg")
        ),

        LugarNatural(
            id = 10,
            nombre = context.getString(R.string.nat_10_name),
            descripcion = context.getString(R.string.nat_10_desc),
            altitud = "3,300 m.s.n.m.",
            actividades = listOf("Trekking", "Fotografía", "Aventura"),
            acceso = "A 1 hora desde Huamanga",
            mejorEpoca = "Abril a noviembre",
            ubicacion = "Provincia de Huamanga",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778687097/campanayocc_1_oikbzf.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778449679/Cerro_Campanayoq_aoqtbs.mp4",
            coordenadas = Pair(-13.171444316066774, -74.25362432737577),
            galeria = listOf("https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778687097/campanayocc_2_cp0ysj.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778687097/campanayocc_3_ainqeq.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778687096/campanayocc_4_kraudo.jpg")
        ),

        LugarNatural(
            id = 11,
            nombre = context.getString(R.string.nat_11_name),
            descripcion = context.getString(R.string.nat_11_desc),
            altitud = "3,100 m.s.n.m.",
            actividades = listOf("Senderismo", "Fotografía", "Exploración"),
            acceso = "A 40 minutos de Huamanga",
            mejorEpoca = "Todo el año",
            ubicacion = "Distrito de Chiara",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778687391/muyu_orqo_4_wcrccz.png",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778451519/Muyu_orqo_obuarg.mp4",
            coordenadas = Pair(-13.262169922966171, -74.18641480801442),
            galeria = listOf("https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778687392/muyu_orqo_2_fnt6tp.png", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778687392/muyu_orqo_3_yuimvh.png", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778687393/muyu_orqo_1_cjoefe.png")
        ),

        LugarNatural(
            id = 12,
            nombre = context.getString(R.string.nat_12_name),
            descripcion = context.getString(R.string.nat_12_desc),
            altitud = "3,400 m.s.n.m.",
            actividades = listOf("Senderismo", "Fotografía", "Meditación"),
            acceso = "Aproximadamente a 1 hora y 20 minutos desde Huamanga, siguiendo el desvío desde el pueblo de Quinua",
            mejorEpoca = "Mayo a octubre (temporada seca)",
            ubicacion = "Distrito de Quinua, Ayacucho",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778687721/paqcha_chirapaq_1_w8rhui.png",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778452025/Cascada_Paqcha_Chirapaq_hpziag.mp4",
            coordenadas = Pair(-13.031012599625875, -74.12008346639531),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778687720/paqcha_chirapaq_3_rruixm.png",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778687720/paqcha_chirapaq_2_bl0jj0.jpg"
            )
        ),

        LugarNatural(
            id = 13,
            nombre = context.getString(R.string.nat_13_name),
            descripcion = context.getString(R.string.nat_13_desc),
            altitud = "3,300 m.s.n.m.",
            actividades = listOf("Observación de aves", "Caminata histórica", "Fotografía de paisaje"),
            acceso = "A pocos minutos de la Plaza de Quinua, camino al Obelisco",
            mejorEpoca = "Todo el año (especialmente después de la temporada de lluvias)",
            ubicacion = "Distrito de Quinua, Ayacucho",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778688373/pantano_1_jhfyry.png",
            coordenadas = Pair(-13.037868116068143, -74.13426592762542),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778688373/pantano_3_m4zlh7.png",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778688373/pantano_2_o8j90u.png", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778688372/pantano_4_itipcc.png"
            )
        ),

        LugarNatural(
            id = 14,
            nombre = context.getString(R.string.nat_14_name),
            descripcion = context.getString(R.string.nat_14_desc),
            altitud = "3,750 m.s.n.m.",
            actividades = listOf("Parapente", "Trekking", "Fotografía panorámica", "Exploración arqueológica"),
            acceso = "Aproximadamente a 45-60 minutos desde Huamanga, accesible por la ruta hacia Socos",
            mejorEpoca = "Abril a noviembre (especialmente para vuelos en parapente)",
            ubicacion = "Distrito de Socos (Cerca de los límites con Quinua)",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778689278/condoray_1_vanjaq.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778453637/Hatun_Condoray_hnqm2u.mp4",
            coordenadas = Pair(-13.231509236232611, -74.2800173968792),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778689279/condoray_2_h0k8zs.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778689277/condoray_3_l4rocm.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778689278/condoray_4_fshqi2.jpg"
            )
        ),

        LugarNatural(
            id = 15,
            nombre = context.getString(R.string.nat_15_name),
            descripcion = context.getString(R.string.nat_15_desc),
            altitud = "3,400 m.s.n.m.",
            actividades = listOf("Caminata", "Fotografía", "Camping"),
            acceso = "A 1.5 horas desde Huamanga",
            mejorEpoca = "Todo el año",
            ubicacion = "Distrito de Vinchos",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778690345/rosaspampa_1_ovwtow.png",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778453846/Rosas_Pampa_de_Vinchos_l8ywul.mp4",
            coordenadas = Pair(-13.274120357498312, -73.85692392272286),
            galeria = listOf("https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778690345/rosaspampa_2_oszuur.png", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778690344/rosaspampa_3_u0wprx.png", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778690345/rosaspampa_4_susft9.png")
        ),

        LugarNatural(
            id = 16,
            nombre = context.getString(R.string.nat_16_name),
            descripcion = context.getString(R.string.nat_16_desc),
            altitud = "3,200 m.s.n.m.",
            actividades = listOf("Trekking de aventura", "Escalada en roca", "Fotografía de paisaje", "Observación de aves"),
            acceso = "A 20 minutos de Huamanga hasta la entrada en Carmen Alto, seguido de una caminata exigente de 3 a 5 horas (según el ritmo) por el cauce del río.",
            mejorEpoca = "Abril a noviembre (temporada seca, ya que el caudal del río es menor y permite el paso)",
            ubicacion = "Comunidad de San Cristóbal de Casaorcco, Distrito de Carmen Alto",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778690570/qosniccpaccha_4_z44gfu.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778454046/Catarata_Qosniccpaccha_fyc2i5.mp4",
            coordenadas = Pair(-13.229002658700509, -74.2312655702352),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778690571/qosniccpaccha_2_wovt7r.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778690571/qosniccpaccha_3_ieh1d4.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778690572/qosniccpaccha_1_hjntty.jpg"
            )
        ),

        LugarNatural(
            id = 17,
            nombre = context.getString(R.string.nat_17_name),
            descripcion = context.getString(R.string.nat_17_desc),
            altitud = "3,650 m.s.n.m.",
            actividades = listOf("Espeleología", "Exploración", "Fotografía de aventura", "Trekking"),
            acceso = "Ubicado a unos 45 minutos de la ciudad de Ayacucho, accesible por la ruta hacia los distritos de la zona alta de Huamanga.",
            mejorEpoca = "Mayo a octubre",
            ubicacion = "Ubicada en la provincia de Huamanga",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778690837/Uchkuera_3_dlglh0.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778455054/Uchkuera_la8x7n.mp4",
            coordenadas = Pair(-13.116257672194006, -74.24661115473826),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778690837/Uchkuera_2_po3ifd.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778690836/Uchkuera_1_hpgpjn.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778690838/Uchkuera_4_mp2iw9.jpg"
            )
        ),

        LugarNatural(
            id = 18,
            nombre = context.getString(R.string.nat_18_name),
            descripcion = context.getString(R.string.nat_18_desc),
            altitud = "3,450 m.s.n.m.",
            actividades = listOf("Paseo en bote", "Kayak", "Paddleboard", "Fotografía paisajística"),
            acceso = "Aproximadamente a 3 horas desde la ciudad de Ayacucho, ubicada en el distrito de Huancaraylla (provincia de Víctor Fajardo).",
            mejorEpoca = "Todo el año, aunque de abril a noviembre el clima es más despejado.",
            ubicacion = "Distrito de Huancaraylla, Víctor Fajardo",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778691025/uchillki_4_qvmntl.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778455502/Laguna_de_Uchillki_kscoju.mp4",
            coordenadas = Pair(-13.718383467354563, -74.10234518465964),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778691028/uchillki_1_j8n2ez.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778691027/uchillki_2_d5nivo.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778691026/uchillki_3_bnoi5j.jpg"
            )
        ),

        LugarNatural(
            id = 19,
            nombre = context.getString(R.string.nat_19_name),
            descripcion = context.getString(R.string.nat_19_desc),
            altitud = "4,050 m.s.n.m.",
            actividades = listOf("Ecoturismo", "Fotografía de naturaleza", "Caminata de altura", "Investigación biológica"),
            acceso = "A unos 45 minutos desde Huamanga, subiendo hacia el centro poblado de Manallasacc (distrito de Chiara).",
            mejorEpoca = "Todo el año, pero la floración suele ser más visible entre mayo y noviembre.",
            ubicacion = "Comunidad de Manallasacc, Distrito de Chiara",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778691453/raimondi_manallasacc_1_b1oacc.png",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778455642/Bosque_de_Puyas_de_Raimondi_-_Manallasacc_zrr8a2.mp4",
            coordenadas = Pair(-13.265, -74.215),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778691452/raimondi_manallasacc_3_nxl1cf.png",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778691452/raimondi_manallasacc_2_peuxp9.png",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778691452/raimondi_manallasacc_4_mj6b5p.png"
            )
        ),

        LugarNatural(
            id = 20,
            nombre = context.getString(R.string.nat_20_name),
            descripcion = context.getString(R.string.nat_20_desc),
            altitud = "3,400 m.s.n.m.",
            actividades = listOf("Baño termal", "Relajación", "Camping", "Fotografía"),
            acceso = "Ubicadas a 1 hora y 30 minutos aproximadamente desde Huamanga, siguiendo la ruta hacia el distrito de Socos.",
            mejorEpoca = "Todo el año (especialmente agradable en los meses fríos de junio a agosto)",
            ubicacion = "Distrito de Socos, Huamanga",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778691882/Ni%C3%B1oyujay_2_jtsmdv.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778456101/Aguas_Termales_de_Ni%C3%B1oyujay_dsstvp.mp4",
            coordenadas = Pair(-13.130345378302682, -74.17313462698947),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778691884/Ni%C3%B1oyujay_1_ichwro.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778691882/Ni%C3%B1oyujay_4_c4rbqc.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778691881/Ni%C3%B1oyujay_3_teumhj.jpg"
            )
        ),

        LugarNatural(
            id = 21,
            nombre = context.getString(R.string.nat_21_name),
            descripcion = context.getString(R.string.nat_21_desc),
            altitud = "2,750 m.s.n.m.",
            actividades = listOf("Fotografía", "Caminata", "Observación"),
            acceso = "A 10 minutos del centro en auto",
            mejorEpoca = "Atardecer",
            ubicacion = "Distrito de Carmen Alto",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778692193/acuchimay_1_rw4eoz.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778456262/Catarata_Pumapaqcha_iwropz.mp4",
            coordenadas = Pair(-13.1714778, -74.2217778),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778692191/acuchimay_2_w0k9ac.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778692191/acuchimay_3_onhcjy.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778692190/acuchimay_4_mse0sn.jpg")
        ),

        LugarNatural(
            id = 22,
            nombre = context.getString(R.string.nat_22_name),
            descripcion = context.getString(R.string.nat_22_desc),
            altitud = "3,350 m.s.n.m.",
            actividades = listOf("Senderismo", "Rituales de purificación", "Fotografía", "Observación de flora"),
            acceso = "A unos 45 minutos desde Huamanga por la vía hacia Cangallo, ubicada en el distrito de Chiara.",
            mejorEpoca = "Abril a diciembre (época seca para disfrutar mejor del paisaje y el agua)",
            ubicacion = "Distrito de Chiara, Huamanga",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778693409/pumapaqcha_3_xxkzbi.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778456262/Catarata_Pumapaqcha_iwropz.mp4",
            coordenadas = Pair(-13.442914130937151, -74.21306172454327),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778693409/pumapaqcha_2_fmjfwn.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778693411/pumapaqcha_4_l6sex4.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778693412/pumapaqcha_1_cayqc4.jpg"
            )
        ),

        LugarNatural(
            id = 23,
            nombre = context.getString(R.string.nat_23_name),
            descripcion = context.getString(R.string.nat_23_desc),
            altitud = "2,450 m.s.n.m.",
            actividades = listOf("Turismo gastronómico", "Paseos familiares", "Fotografía rural", "Descanso"),
            acceso = "A tan solo 15 o 20 minutos desde el centro de Huamanga, siguiendo la vía hacia el aeropuerto.",
            mejorEpoca = "Todo el año (especialmente fines de semana para encontrar todos los recreos abiertos)",
            ubicacion = "Huamanga",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778693648/huatatas_4_befp75.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778456598/Huatatas_ovbgam.mp4",
            coordenadas = Pair(-13.173683456178374, -74.19156838650528),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778693652/huatatas_1_ghkomb.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778693650/huatatas_2_sorqb6.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778693649/huatatas_3_cke4gj.jpg"
            )
        ),

        LugarNatural(
            id = 24,
            nombre = context.getString(R.string.nat_24_name),
            descripcion = context.getString(R.string.nat_24_desc),
            altitud = "3,250 m.s.n.m.",
            actividades = listOf("Recorrido artístico", "Fotografía", "Caminata suave", "Meditación"),
            acceso = "A unos 20 minutos desde el centro de Huamanga. Se puede llegar en taxi o transporte público hacia el sector de Condormaycco.",
            mejorEpoca = "Todo el año, preferiblemente por la tarde para aprovechar la luz del sol sobre las esculturas.",
            ubicacion = "Sector Condormaycco",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778693999/Morada_De_Los_Apus_1_uo0w2y.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778456722/Morada_de_los_Apus_w5wpss.mp4",
            coordenadas = Pair(-13.11828825934049, -74.20202379075795),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778694000/Morada_De_Los_Apus_3_utprqe.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778694000/Morada_De_Los_Apus_2_esate3.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778693999/Morada_De_Los_Apus_4_j3gc3n.jpg"
            )
        ),

        LugarNatural(
            id = 25,
            nombre = context.getString(R.string.nat_25_name),
            descripcion = context.getString(R.string.nat_25_desc),
            altitud = "2,760 m.s.n.m.",
            actividades = listOf("Observación panorámica", "Turismo religioso", "Fotografía urbana", "Caminata histórica"),
            acceso = "A pocos minutos a pie desde la Plaza de Armas de Huamanga, subiendo por las pintorescas calles del barrio de Belén.",
            mejorEpoca = "Todo el año (especialmente al atardecer para ver la iluminación de la ciudad)",
            ubicacion = "Barrio de Belén, Huamanga",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778694792/belen_1_gihfzg.jpg",
            videoUrl = "",
            coordenadas = Pair(-13.164278243532543, -74.23095088650527),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778694795/belen_2_pulkbh.png",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778694793/belen_3_qeupq3.png"

            )
        ),

        LugarNatural(
            id = 26,
            nombre = context.getString(R.string.nat_26_name),
            descripcion = context.getString(R.string.nat_26_desc),
            altitud = "2,830 m.s.n.m.",
            actividades = listOf("Turismo cultural", "Recorrido arqueológico", "Visita al museo de sitio", "Fotografía histórica"),
            acceso = "Ubicado a 22 km al noreste de la ciudad de Ayacucho (unos 30-40 minutos en auto), por la carretera hacia Quinua.",
            mejorEpoca = "Todo el año (de mayo a octubre para evitar lluvias durante la caminata)",
            ubicacion = "Distrito de Quinua, Huamanga",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695040/wari_4_depgq4.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778457023/Complejo_Arqueol%C3%B3gico_Wari_xv3xss.mp4",
            coordenadas = Pair(-13.059466216489064, -74.19953557116492),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695042/wari_3_ajhcqj.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695043/wari_2_xgmljf.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695045/wari_1_gfeb77.jpg"
            )
        ),

        LugarNatural(
            id = 27,
            nombre = context.getString(R.string.nat_27_name),
            descripcion = context.getString(R.string.nat_27_desc),
            altitud = "3,300 m.s.n.m.",
            actividades = listOf("Observación panorámica", "Fotografía de paisaje", "Caminata", "Meditación"),
            acceso = "A unos 20 minutos desde el centro de Huamanga. Se puede llegar en auto o mediante una caminata exigente pero gratificante desde el sector de San Juan Bautista.",
            mejorEpoca = "Todo el año (preferiblemente de mayo a octubre para cielos despejados y atardeceres rojizos).",
            ubicacion = "Ocros, Huamanga",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695307/puncupata_1_pn4tn2.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778457585/Mirador_de_Puncupata_zhbwg4.mp4",
            coordenadas = Pair(-13.443787785498754, -73.98867730445915),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695312/puncupata_2_w2ict3.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695306/puncupata_3_gao2wj.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695309/puncupata_4_gj1adx.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695310/puncupata_5_olzoci.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695304/puncupata_6_dfx6zk.jpg"
            )
        ),

        LugarNatural(
            id = 28,
            nombre = context.getString(R.string.nat_28_name),
            descripcion = context.getString(R.string.nat_28_desc),
            altitud = "3,550 m.s.n.m.",
            actividades = listOf("Espeleología", "Trekking", "Fotografía de aventura", "Exploración arqueológica"),
            acceso = "Ubicada a unos 40 minutos de Huamanga. El acceso implica una caminata moderada por senderos naturales desde las zonas altas de la ciudad.",
            mejorEpoca = "Abril a noviembre (época seca para garantizar seguridad al ingresar a la cueva)",
            ubicacion = "Ayacucho - San Francisco",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695650/cueva_infiernillo_1_s8y2fh.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778457718/Cueva_de_Infiernillo_dcqmvi.mp4",
            coordenadas = Pair(-13.063712382958538, -74.19213895713062),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695649/cueva_infiernillo_2_xstfzk.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695646/cueva_infiernillo_4_ldr8fo.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695647/cueva_infiernillo_3_amcsff.jpg"
            )
        ),

        LugarNatural(
            id = 29,
            nombre = context.getString(R.string.nat_29_name),
            descripcion = context.getString(R.string.nat_29_desc),
            altitud = "2,650 m.s.n.m.",
            actividades = listOf("Senderismo", "Fotografía de naturaleza", "Observación de aves", "Baño en pozas naturales"),
            acceso = "Ubicada a unos 15 minutos en auto desde la ciudad de Huanta, seguido de una caminata corta de 10 a 15 minutos por un sendero bien señalizado.",
            mejorEpoca = "Abril a diciembre (especialmente hermosa después de la temporada de lluvias cuando el verdor es intenso).",
            ubicacion = "Distrito de Huanta, Provincia de Huanta",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695837/occochaca_4_suink0.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778457809/Cascada_de_Occochaca_r9hptg.mp4",
            coordenadas = Pair(-12.934227267150582, -74.22205569357834),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695840/occochaca_1_icb5md.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695839/occochaca_2_owwg5v.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778695836/occochaca_3_ajtorr.jpg"
            )
        ),

        LugarNatural(
            id = 30,
            nombre = context.getString(R.string.nat_30_name),
            descripcion = context.getString(R.string.nat_30_desc),
            altitud = "3,600 m.s.n.m.",
            actividades = listOf("Senderismo", "Fotografía", "Observación de flora y fauna", "Caminata por el cañón"),
            acceso = "A unas 3.5 a 4 horas desde la ciudad de Huamanga.",
            mejorEpoca = "Mayo a septiembre",
            ubicacion = "Victor Fajardo",
            imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778696055/millpu_2_gskoxi.jpg",
            videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778457873/Aguas_Turquesas_de_Millpu_etxpvx.mp4",
            coordenadas = Pair(-13.749302572079511, -74.1310999711649),
            galeria = listOf(
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778696059/millpu_4_b8btwq.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778696057/millpu_1_z3jyku.jpg",
                "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778696053/millpu_3_qatyzf.jpg"
            )
        )
    )
}
