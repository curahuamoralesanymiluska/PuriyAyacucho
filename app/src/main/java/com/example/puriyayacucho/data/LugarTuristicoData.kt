package com.example.puriyayacucho.data

import android.content.Context
import com.example.puriyayacucho.R

data class LugarTuristico(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val tipo: String,
    val horario: String,
    val ubicacion: String,
    val recomendacion: String,
    val imagen: Any = R.drawable.ic_temple, // Soporta URL (String) y Local (Int)
    val videoUrl: String = "",
    val coordenadas: Pair<Double, Double> = Pair(-13.1519, -74.2145),
    val telefono: String = "N/A",
    val galeria: List<Any> = emptyList(),
    val itemFiltrable: ItemFiltrable = ItemFiltrable()
)

object LugarTuristicoFactory {
    fun obtenerLugaresTuristicosAyacucho(context: Context): List<LugarTuristico> {
        val packageName = context.packageName

        val lugaresReligiosos = listOf(
            LugarTuristico(
                id = 1,
                nombre = context.getString(R.string.temple_1_name),
                descripcion = context.getString(R.string.temple_1_desc),
                tipo = "Templo Religioso",
                horario = "07:00 AM - 09:00 AM / 05:00 PM - 07:00 PM",
                ubicacion = "Plaza Mayor de Huamanga",
                recomendacion = context.getString(R.string.temple_1_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778733487/catedral_ayacucho_1_obeajz.jpg",
                videoUrl = "android.resource://$packageName/${R.raw.catedraldeayacucho}",
                coordenadas = Pair(-13.160550672000909, -74.22525103513485),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778733488/catedral_ayacucho_2_auqofx.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778733488/catedral_ayacucho_4_gtg5f6.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778733487/catedral_ayacucho_3_chpyul.jpg"
                )
            ),
            LugarTuristico(
                id = 2,
                nombre = context.getString(R.string.temple_2_name),
                descripcion = context.getString(R.string.temple_2_desc),
                tipo = "Templo Religioso",
                horario = "Libre",
                ubicacion = "Jr. 28 de Julio",
                recomendacion = context.getString(R.string.temple_2_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778733897/san_cristobal_1_ofpkzx.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778298002/Templo_de_San_Crist%C3%B3bal_copc1p.mp4",
                coordenadas = Pair(-13.166900871299001, -74.22795800278499),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778733897/san_cristobal_4_e8fsel.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778733897/san_cristobal_3_nkvgfu.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778733897/san_cristobal_2_olrntr.jpg"
                )
            ),
            LugarTuristico(
                id = 3,
                nombre = context.getString(R.string.temple_3_name),
                descripcion = context.getString(R.string.temple_3_desc),
                tipo = "Templo Religioso",
                horario = "09:00 AM - 12:00 PM",
                ubicacion = "Jr. Dos de Mayo",
                recomendacion = context.getString(R.string.temple_3_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778734092/la_merced_1_uzdqmm.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778298313/Templo_de_La_Merced_fn8pdv.mp4",
                coordenadas = Pair(-13.162348553807728, -74.22553144683684),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778734091/la_merced_4_kqfsby.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778734092/la_merced_3_emmsrj.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778734094/la_merced_2_hroqqc.jpg"
                )
            ),
            LugarTuristico(
                id = 4,
                nombre = context.getString(R.string.temple_4_name),
                descripcion = context.getString(R.string.temple_4_desc),
                tipo = "Templo Religioso",
                horario = "08:00 AM - 12:00 PM",
                ubicacion = "Jr. Bellido",
                recomendacion = context.getString(R.string.temple_4_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778734261/santo_domingo_1_sdc462.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778298479/Templo_de_Santo_Domingo_ytcgfw.mp4",
                coordenadas = Pair(-13.158056225461436, -74.2259148198465),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778734260/santo_domingo_2_rloq1x.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778734259/santo_domingo_4_lkhygr.jpg",
                    "https://res.cloudinary.com/drevpsvmr/image/upload/v1778734718/san_domingo_3_p8x7hy.jpg"
                )
            ),
            LugarTuristico(
                id = 5,
                nombre = context.getString(R.string.temple_5_name),
                descripcion = context.getString(R.string.temple_5_desc),
                tipo = "Templo Religioso",
                horario = "08:00 AM - 11:00 AM",
                ubicacion = "Jr. 28 de Julio",
                recomendacion = context.getString(R.string.temple_5_rec),
                imagen = "https://res.cloudinary.com/drevpsvmr/image/upload/v1778735030/san_juan_d_2_s1cu9m.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778298599/Templo_de_San_Juan_de_Dios_casgun.mp4",
                coordenadas = Pair(-13.163717679513649, -74.22737369414361),
                galeria = listOf(
                    "https://res.cloudinary.com/drevpsvmr/image/upload/v1778735031/san_juan_d_1_u8niju.jpg",
                    "https://res.cloudinary.com/drevpsvmr/image/upload/v1778735030/san_juan_d_3_dvhjn4.jpg",
                    "https://res.cloudinary.com/drevpsvmr/image/upload/v1778735030/san_juan_d_4_sxti4e.jpg"
                )
            ),
            LugarTuristico(
                id = 6,
                nombre = context.getString(R.string.temple_6_name),
                descripcion = context.getString(R.string.temple_6_desc),
                tipo = "Templo Religioso",
                horario = "08:00 AM - 11:00 AM",
                ubicacion = "Jr. 28 de Julio (Plazoleta San Francisco)",
                recomendacion = context.getString(R.string.temple_6_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778770598/saan_francisco_1_g8yhdo.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778298784/Templo_de_San_Francisco_de_As%C3%ADs_kwyv44.mp4",
                coordenadas = Pair(-13.163157346096776, -74.22697257411964),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778770598/saan_francisco_2_ze115z.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778770598/saan_francisco_3_snbosv.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778770598/saan_francisco_4_leqzsl.jpg"
                )
            ),
            LugarTuristico(
                id = 7,
                nombre = context.getString(R.string.temple_7_name),
                descripcion = context.getString(R.string.temple_7_desc),
                tipo = "Templo Religioso",
                horario = "07:00 AM - 09:00 AM",
                ubicacion = "Jr. Grau",
                recomendacion = context.getString(R.string.temple_7_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778770977/santa_clara_4_nqh9pn.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778299475/Templo_de_Santa_Clara_de_As%C3%ADs_u5f2c1.mp4",
                coordenadas = Pair(-13.162772448105452, -74.22835723149032),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778770977/santa_clara_3_jhzrf1.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778770977/santa_clara_1_mvhkkl.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778770977/santa_clara_2_vza1ru.jpg"
                )
            ),
            LugarTuristico(
                id = 8,
                nombre = context.getString(R.string.temple_8_name),
                descripcion = context.getString(R.string.temple_8_desc),
                tipo = "Templo Religioso",
                horario = "08:00 AM - 12:00 PM",
                ubicacion = "Plazuela de Santa Ana",
                recomendacion = context.getString(R.string.temple_8_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778771300/santa_ana_1_kd5v1a.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778349640/Templo_de_Santa_Ana_bxow4u.mp4",
                coordenadas = Pair(-13.17013920774523, -74.23084127567148),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778771299/santa_ana_4_imfncz.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778771298/santa_ana_2_rrnlmn.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778771299/santa_ana_3_bejday.jpg"
                )
            ),
            LugarTuristico(
                id = 9,
                nombre = context.getString(R.string.temple_9_name),
                descripcion = context.getString(R.string.temple_9_desc),
                tipo = "Templo Religioso",
                horario = "Libre",
                ubicacion = "Barrio de la Magdalena",
                recomendacion = context.getString(R.string.temple_9_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778771589/santa_maria_1_dvi98t.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778300264/Templo_de_Santa_Mar%C3%ADa_Magdalena_ouiwyb.mp4",
                coordenadas = Pair(-13.157494249259292, -74.22137911615765),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778771589/santa_maria_2_s296w8.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778771590/santa_maria_4_udq7nn.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778771589/santa_maria_3_vjhryq.jpg"
                )
            ),
            LugarTuristico(
                id = 10,
                nombre = context.getString(R.string.temple_10_name),
                descripcion = context.getString(R.string.temple_10_desc),
                tipo = "Templo Religioso",
                horario = "09:00 AM - 06:00 PM",
                ubicacion = "Portal de la Unión",
                recomendacion = context.getString(R.string.temple_10_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778771946/compa%C3%B1ia_jesus_5_jlxq0b.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778300418/Templo_de_La_Compa%C3%B1%C3%ADa_de_Jes%C3%BAs_nbxzh4.mp4",
                coordenadas = Pair(-13.161188720184153, -74.22661686211299),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778771946/compa%C3%B1ia_jesus_2_bvz6xk.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778771948/compa%C3%B1ia_jesus_1_kfbwsy.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778771946/compa%C3%B1ia_jesus_4_od5dfv.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778771946/compa%C3%B1ia_jesus_3_tnoyve.jpg"
                )
            ),
            LugarTuristico(
                id = 11,
                nombre = context.getString(R.string.temple_11_name),
                descripcion = context.getString(R.string.temple_11_desc),
                tipo = "Templo Religioso",
                horario = "07:00 AM - 09:00 AM",
                ubicacion = "Portal de la Unión",
                recomendacion = context.getString(R.string.temple_11_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772152/se%C3%B1ora_loreto_1_g4plao.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778300418/Templo_de_La_Compa%C3%B1%C3%ADa_de_Jes%C3%BAs_nbxzh4.mp4",
                coordenadas = Pair(-13.161188720184153, -74.22661686211299),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772153/se%C3%B1ora_loreto_2_v0wx1w.jpg"
                )
            ),
            LugarTuristico(
                id = 12,
                nombre = context.getString(R.string.temple_12_name),
                descripcion = context.getString(R.string.temple_12_desc),
                tipo = "Templo Religioso",
                horario = "08:00 AM - 12:00 PM",
                ubicacion = "Jr. Asamblea",
                recomendacion = context.getString(R.string.temple_12_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772406/san_agustin_3_la38re.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778300900/Templo_de_San_Agust%C3%ADn_kcq9dy.mp4",
                coordenadas = Pair(-13.159609673081222, -74.22496884622625),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772406/san_agustin_2_dntkql.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772407/san_agustin_1_pe7n80.jpg"
                )
            ),
            LugarTuristico(
                id = 13,
                nombre = context.getString(R.string.temple_13_name),
                descripcion = context.getString(R.string.temple_13_desc),
                tipo = "Templo Religioso",
                horario = "Eventual",
                ubicacion = "Jr. San Sebastián",
                recomendacion = context.getString(R.string.temple_13_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772576/saan_sebastian_1_kzog3u.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778301442/Templo_de_San_Sebasti%C3%A1n_c9opw7.mp4",
                coordenadas = Pair(-13.163078301426985, -74.2200256603321),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772576/saan_sebastian_2_peh8su.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772577/saan_sebastian_3_awl2fp.jpg"
                )
            ),
            LugarTuristico(
                id = 14,
                nombre = context.getString(R.string.temple_14_name),
                descripcion = context.getString(R.string.temple_14_desc),
                tipo = "Templo Religioso",
                horario = "07:00 AM - 10:00 AM",
                ubicacion = "Jr. Belén",
                recomendacion = context.getString(R.string.temple_14_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772775/tem_belen_1_u3mqps.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778341009/Templo_de_Bel%C3%A9n_vf90gl.mp4",
                coordenadas = Pair(-13.164145408602565, -74.23113899285585),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772774/tem_belen_2_rdirly.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772774/tem_belen_3_v9llid.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772773/tem_belen_4_vr5v3k.jpg"
                )
            ),
            LugarTuristico(
                id = 15,
                nombre = context.getString(R.string.temple_15_name),
                descripcion = context.getString(R.string.temple_15_desc),
                tipo = "Templo Religioso",
                horario = "09:00 AM - 11:00 AM",
                ubicacion = "Jr. 28 de Julio",
                recomendacion = context.getString(R.string.temple_15_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772940/santa_teresa_4_hta6hy.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778349899/Templo_de_Santa_Teresa_dbw5fp.mp4",
                coordenadas = Pair(-13.167776892393194, -74.22778358582029),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772943/santa_teresa_1_kojvcz.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772942/santa_teresa_2_vamlux.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778772941/santa_teresa_3_awtjpo.jpg"
                )
            ),
            LugarTuristico(
                id = 16,
                nombre = context.getString(R.string.temple_16_name),
                descripcion = context.getString(R.string.temple_16_desc),
                tipo = "Templo Religioso",
                horario = "08:00 AM - 12:00 PM",
                ubicacion = "Jr. San Martín",
                recomendacion = context.getString(R.string.temple_16_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773103/paula_2_h7toum.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778341206/Templo_de_San_Francisco_de_Paula_s1tnpk.mp4",
                coordenadas = Pair(-13.159265591596162, -74.22737571984649),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773101/paula_1_gpsbwt.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773101/paula_3_rtdreb.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773100/paula_4_tzu3yd.jpg"
                )
            ),
            LugarTuristico(
                id = 17,
                nombre = context.getString(R.string.temple_17_name),
                descripcion = context.getString(R.string.temple_17_desc),
                tipo = "Templo Religioso",
                horario = "08:00 AM - 11:00 AM",
                ubicacion = "Av. Independencia",
                recomendacion = context.getString(R.string.temple_17_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773347/buena_muerte_1_oikwmw.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778341296/Templo_de_La_Buena_Muerte_mk1wqm.mp4",
                coordenadas = Pair(-13.161287298084659, -74.2240211523023),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773345/buena_muerte_4_iljmaf.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773346/buena_muerte_2_zjk8sj.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773345/buena_muerte_3_k5zoja.jpg"
                )
            ),
            LugarTuristico(
                id = 18,
                nombre = context.getString(R.string.temple_18_name),
                descripcion = context.getString(R.string.temple_18_desc),
                tipo = "Templo Religioso",
                horario = "Eventual",
                ubicacion = "Jr. La Amargura",
                recomendacion = context.getString(R.string.temple_18_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773520/amargura_1_ivdilz.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778349997/Templo_de_La_Amargura_saeqha.mp4",
                coordenadas = Pair(-13.16058994032782, -74.22109037751618),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773519/amargura_2_ylpzkv.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773517/amargura_3_xysbln.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773519/amargura_4_abh7qi.jpg"
                )
            ),
            LugarTuristico(
                id = 19,
                nombre = context.getString(R.string.temple_19_name),
                descripcion = context.getString(R.string.temple_19_desc),
                tipo = "Templo Religioso",
                horario = "Libre",
                ubicacion = "Plaza de Carmen Alto",
                recomendacion = context.getString(R.string.temple_19_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773683/carmen_alto_1_zbi9az.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778341419/Templo_del_Carmen_Alto_yypwya.mp4",
                coordenadas = Pair(-13.175711141575478, -74.22638736033181),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773682/carmen_alto_2_dbqzcs.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773681/carmen_alto_3_yvtp1g.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773680/carmen_alto_4_zmbdqg.jpg"
                )
            ),
            LugarTuristico(
                id = 20,
                nombre = context.getString(R.string.temple_20_name),
                descripcion = context.getString(R.string.temple_20_desc),
                tipo = "Templo Religioso",
                horario = "Libre",
                ubicacion = "Pampa San Agustín",
                recomendacion = context.getString(R.string.temple_20_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773865/pampa_san_4_musnu2.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778341518/Templo_de_Pampa_San_Agust%C3%ADn_zpyrmd.mp4",
                coordenadas = Pair(-13.162100753575517, -74.22332071984634),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773866/pampa_san_1_nfpbwg.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773866/pampa_san_3_hmyrln.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778773865/pampa_san_4_musnu2.jpg"
                )
            ),
            LugarTuristico(
                id = 21,
                nombre = context.getString(R.string.temple_21_name),
                descripcion = context.getString(R.string.temple_21_desc),
                tipo = "Templo Religioso",
                horario = "Libre",
                ubicacion = "Plaza de San Juan Bautista",
                recomendacion = context.getString(R.string.temple_21_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774107/bautista_san_1_y9ilmy.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778341599/Templo_de_San_Juan_Bautista_ycx8cb.mp4",
                coordenadas = Pair(-13.16616390418755, -74.22345444868117),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774106/bautista_san_2_uwf2yb.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774105/bautista_san_3_k7qng2.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774104/bautista_san_4_vrflxb.jpg"
                )
            ),
            LugarTuristico(
                id = 22,
                nombre = context.getString(R.string.temple_22_name),
                descripcion = context.getString(R.string.temple_22_desc),
                tipo = "Templo Religioso",
                horario = "07:00 AM - 09:00 AM",
                ubicacion = "Barrio de Soquiaqato",
                recomendacion = context.getString(R.string.temple_22_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774295/soquiaqato_1_dkd6ql.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778341852/Templo_de_Soquiaqato_vwxqqu.mp4",
                coordenadas = Pair(-13.161185087801831, -74.2295579621765),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774293/soquiaqato_2_fwlv46.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774292/soquiaqato_3_ps6e3c.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774291/soquiaqato_4_gbecxt.jpg"
                )
            ),
            LugarTuristico(
                id = 23,
                nombre = context.getString(R.string.temple_23_name),
                descripcion = context.getString(R.string.temple_23_desc),
                tipo = "Templo Religioso",
                horario = "Libre",
                ubicacion = "Barrio de Conchopata",
                recomendacion = context.getString(R.string.temple_23_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774460/conchopata_2_k1vkkd.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778343990/Templo_de_Conchopata_mwslhh.mp4",
                coordenadas = Pair(-13.161908823674018, -74.21725154320019),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774461/conchopata_1_flano6.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774458/conchopata_3_z8qpin.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774457/conchopata_4_wedktt.jpg"
                )
            ),
            LugarTuristico(
                id = 24,
                nombre = context.getString(R.string.temple_24_name),
                descripcion = context.getString(R.string.temple_24_desc),
                tipo = "Templo Religioso",
                horario = "Libre",
                ubicacion = "Colina El Calvario",
                recomendacion = context.getString(R.string.temple_24_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774604/calvario_1_yfziok.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778344660/Templo_de_El_Calvario_r91zhu.mp4",
                coordenadas = Pair(-13.157228743380248, -74.22940724868133),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774603/calvario_2_mecsxy.jpg"
                )
            ),
            LugarTuristico(
                id = 25,
                nombre = context.getString(R.string.temple_25_name),
                descripcion = context.getString(R.string.temple_25_desc),
                tipo = "Templo Religioso",
                horario = "Eventual",
                ubicacion = "Jr. Huamanga",
                recomendacion = context.getString(R.string.temple_25_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774749/pilar_1_hsgkjo.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778344824/Templo_de_Nuestra_Se%C3%B1ora_del_Pilar_uyatvx.mp4",
                coordenadas = Pair(-13.154276784014218, -74.2250324450634),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774748/pilar_2_r5owya.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774746/pilar_3_gqueos.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774746/pilar_4_pije0j.jpg"
                )
            ),
            LugarTuristico(
                id = 26,
                nombre = context.getString(R.string.temple_26_name),
                descripcion = context.getString(R.string.temple_26_desc),
                tipo = "Templo Religioso",
                horario = "08:00 AM - 05:00 PM",
                ubicacion = "Cementerio General de Huamanga",
                recomendacion = context.getString(R.string.temple_26_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774908/cementerio_1_hx1u5v.jpg",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778344943/Capilla_del_Cementerio_General_q8tfva.mp4",
                coordenadas = Pair(-13.167507147196183, -74.21138905907553),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774907/cementerio_2_pwewfw.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774906/cementerio_3_bqseg1.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778774904/cementerio_4_ibx2qj.jpg"
                )
            ),
            LugarTuristico(
                id = 27,
                nombre = context.getString(R.string.temple_27_name),
                descripcion = context.getString(R.string.temple_27_desc),
                tipo = "Templo Religioso",
                horario = "Eventual",
                ubicacion = "Barrio de Quinuapata",
                recomendacion = context.getString(R.string.temple_27_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778775088/quinuapata_2_ydiacz.webp",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778345055/Templo_de_Se%C3%B1or_de_Quinuapata_xunwb0.mp4",
                coordenadas = Pair(-13.166026659476481, -74.23386238439427),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778775087/quinuapata_3_pico3l.webp",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778775085/quinuapata_4_c8hqx5.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778775090/quinuapata_1_maazzj.jpg"
                )
            ),
            LugarTuristico(
                id = 28,
                nombre = context.getString(R.string.temple_28_name),
                descripcion = context.getString(R.string.temple_28_desc),
                tipo = "Templo Religioso",
                horario = "Libre",
                ubicacion = "Sector Capillapata",
                recomendacion = context.getString(R.string.temple_28_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778282170/temploldecapillapata_txmbpa.png",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778345414/Templo_de_Capillapata_zqlpyl.mp4",
                coordenadas = Pair(-13.165735031777992, -74.21862889891598),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778775312/capillapata_2_yhjoue.webp"
                )
            ),
            LugarTuristico(
                id = 29,
                nombre = context.getString(R.string.temple_29_name),
                descripcion = context.getString(R.string.temple_29_desc),
                tipo = "Templo Religioso",
                horario = "08:00 AM - 11:00 AM",
                ubicacion = "Urb. Los Jardines",
                recomendacion = context.getString(R.string.temple_29_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778775509/s%C3%B1r_arequipa_1_t1phpn.webp",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778346895/Templo_de_Se%C3%B1or_de_Arequipa_sspzab.mp4",
                coordenadas = Pair(-13.167471692353798, -74.21272631800186),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778775510/s%C3%B1r_arequipa_2_uqypgu.webp"
                )
            ),
            LugarTuristico(
                id = 30,
                nombre = context.getString(R.string.temple_30_name),
                descripcion = context.getString(R.string.temple_30_desc),
                tipo = "Templo Religioso",
                horario = "Libre",
                ubicacion = "Barrio de Chiquinquirá",
                recomendacion = context.getString(R.string.temple_30_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778775864/chiquinquira_1_fpc6xj.webp",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778347081/Templo_de_Virgen_del_Rosario_de_Chiquinquir%C3%A1_gqj07p.mp4",
                coordenadas = Pair(-13.153415416708247, -74.2250643259964),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778775863/chiquinquira_2_vxvzrq.png",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778775862/chiquinquira_3_bf2yh5.png",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778775861/chiquinquira_4_dulq23.png"
                )
            ),
            LugarTuristico(
                id = 31,
                nombre = context.getString(R.string.temple_31_name),
                descripcion = context.getString(R.string.temple_31_desc),
                tipo = "Templo Religioso",
                horario = "08:00 AM - 06:00 PM",
                ubicacion = "Urb. Los Licenciados",
                recomendacion = context.getString(R.string.temple_31_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778776123/virgen_de_fatima_lytfcf.webp",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778347550/Templo_de_Virgen_de_F%C3%A1tima_dchrc0.mp4",
                coordenadas = Pair(-13.158353144337754, -74.22553794868135),
                galeria = listOf("", "", "")
            ),
            LugarTuristico(
                id = 32,
                nombre = context.getString(R.string.temple_32_name),
                descripcion = context.getString(R.string.temple_32_desc),
                tipo = "Templo Religioso",
                horario = "08:00 AM - 12:00 PM",
                ubicacion = "Barrio de las Maravillas",
                recomendacion = context.getString(R.string.temple_32_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778776352/se%C3%B1or_de_las_maravillas_3_wxrrvo.png",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778347633/Templo_de_Se%C3%B1or_de_las_Maravillas_nwyzvd.mp4",
                coordenadas = Pair(-12.106738518215051, -74.39287956577047),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778776352/se%C3%B1or_de_las_maravillas_3_wxrrvo.png",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778776356/se%C3%B1or_de_las_maravillas_mrxdmz.jpg"
                )
            ),
            LugarTuristico(
                id = 33,
                nombre = context.getString(R.string.temple_33_name),
                descripcion = context.getString(R.string.temple_33_desc),
                tipo = "Templo Religioso",
                horario = "Libre",
                ubicacion = "Sector Pampa Cruz",
                recomendacion = context.getString(R.string.temple_33_rec),
                imagen = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778776477/pampa_cruz_1_wf8tmf.webp",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778347726/Templo_de_Se%C3%B1or_de_Pampa_Cruz_xdacuy.mp4",
                coordenadas = Pair(-13.164274043152137, -74.22531370450672),
                galeria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778776475/pampa_cruz_2_t5jqoc.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778776477/pampa_cruz_1_wf8tmf.webp"
                )
            ),

        )
        return lugaresReligiosos
    }
}
