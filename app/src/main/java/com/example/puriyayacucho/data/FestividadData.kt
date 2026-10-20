package com.example.puriyayacucho.data

import android.content.Context
import com.example.puriyayacucho.R

object FestividadFactory {
    fun obtenerFestividadesAyacucho(context: Context): List<Festividad> {
        return listOf(

            // ENERO 2026
            Festividad(
                id = 1,
                nombre = context.getString(R.string.fest_1_name),
                descripcion = context.getString(R.string.fest_1_desc),
                fecha = "02/01/2026",
                dia = 2,
                mes = 1,
                ubicacion = "Ayacucho",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778822937/adoracion_al_ni%C3%B1o_vid_ti08ly.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778822976/adoracion_al_ni%C3%B1o_1_bbtwqf.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778822975/adoracion_al_ni%C3%B1o_3_lixudj.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778822975/adoracion_al_ni%C3%B1o_3_lixudj.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778822973/adoracion_al_ni%C3%B1o_2_fjqrer.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778822972/adoracion_al_ni%C3%B1o_4_uefz0b.jpg"),
                recomendacion = context.getString(R.string.fest_1_rec)
            ),
            Festividad(
                id = 2,
                nombre = context.getString(R.string.fest_2_name),
                descripcion = context.getString(R.string.fest_2_desc),
                fecha = "06/01/2026",
                dia = 6,
                mes = 1,
                ubicacion = "Cangallo",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778823587/bajada_reyes_me2c8j.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778851780/bajadareyes_2_uvwagv.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778851777/bajadareyes_3_np4o4u.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778851783/bajadareyes_1_j2a7sn.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778851786/bajadareyes_5_n8qcd2.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778851789/bajadareyes_4_fw9qza.jpg"
                ),
                recomendacion = context.getString(R.string.fest_2_rec)
            ),
            Festividad(
                id = 3,
                nombre = context.getString(R.string.fest_3_name),
                descripcion = context.getString(R.string.fest_3_desc),
                fecha = "20/01/2026",
                dia = 20,
                mes = 1,
                ubicacion = "Huamanga",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778823669/fiesta_san_sebastian_gzyp3h.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852024/sansebastian_1_ezndyu.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852018/sansebastian_3_hwkzwv.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852021/sansebastian_2_ppij35.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852014/sansebastian_5_ke6gai.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852011/sansebastian_4_osn6ok.jpg"
                ),
                recomendacion = context.getString(R.string.fest_3_rec)
            ),

            // FEBRERO 2026
            Festividad(
                id = 4,
                nombre = context.getString(R.string.fest_4_name),
                descripcion = context.getString(R.string.fest_4_desc),
                fecha = "02/02/2026",
                dia = 2,
                mes = 2,
                ubicacion = "Ayacucho",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778826643/vid_virgen_de_la_candelaria_g2pqiq.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852243/fiesta_candelaria_1_ein6pg.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852246/fiesta_candelaria_4_cd15j0.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852249/fiesta_candelaria_3_ex6anj.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852253/fiesta_candelaria_5_kzfgva.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852256/fiesta_candelaria_2_aazasv.jpg"
                ),
                recomendacion = context.getString(R.string.fest_4_rec)
            ),
            Festividad(
                id = 5,
                nombre = context.getString(R.string.fest_5_name),
                descripcion = context.getString(R.string.fest_5_desc),
                fecha = "14/02/2026",
                dia = 14,
                mes = 2,
                ubicacion = "Huamanga",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778826755/vid_maria_parado_mmuosw.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852432/escenificacion_mariapar_1_fpmh6s.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852425/escenificacion_mariapar_2_kgvvpx.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852428/escenificacion_mariapar_3_tv3evg.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852435/escenificacion_mariapar_5_uki5gi.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852438/escenificacion_mariapar_4_o4dkmz.jpg"
                ),
                recomendacion = context.getString(R.string.fest_5_rec)
            ),
            Festividad(
                id = 6,
                nombre = context.getString(R.string.fest_6_name),
                descripcion = context.getString(R.string.fest_6_desc),
                fecha = "15/02/2026",
                dia = 15,
                mes = 2,
                ubicacion = "Ayacucho",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778824748/carnavales_ayacuchanos_vsgzsm.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852613/fiesta_carnaval_1_dcjkml.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852610/fiesta_carnaval_2_dpqwzo.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852620/fiesta_carnaval_4_btrb2c.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852617/fiesta_carnaval_5_exhxcl.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778852607/fiesta_carnaval_3_llzayl.jpg"
                ),
                recomendacion = context.getString(R.string.fest_6_rec)
            ),

            // MARZO 2026
            Festividad(
                id = 31,
                nombre = context.getString(R.string.fest_31_name),
                descripcion = context.getString(R.string.fest_31_desc),
                fecha = "27/03/2026",
                dia = 27,
                mes = 3,
                ubicacion = "Ayacucho",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778859157/fiesta_virgen_dolorosa_1_tkqkju.jpg",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778859529/virgen_dolorosa_vid_bbgod9.mp4",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778859152/fiesta_virgen_dolorosa_2_ope3zd.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778859148/fiesta_virgen_dolorosa_3_llmgqy.jpg"
                ),
                recomendacion = context.getString(R.string.fest_31_rec)
            ),
            Festividad(
                id = 32,
                nombre = context.getString(R.string.fest_32_name),
                descripcion = context.getString(R.string.fest_32_desc),
                fecha = "29/03/2026",
                dia = 29,
                mes = 3,
                ubicacion = "Huamanga",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778859534/vid_domingo_de_ramos_xsxjtr.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778858756/ramos_fiesta_1_coblk2.jpg",
                imagenesGaleria = listOf("https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778858752/ramos_fiesta_2_adwe9l.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778858747/ramos_fiesta_3_ibadjc.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778858743/ramos_fiesta_4_tqn4wr.jpg"),
                recomendacion = context.getString(R.string.fest_32_rec)
            ),

            // ABRIL 2026
            Festividad(
                id = 33,
                nombre = context.getString(R.string.fest_33_name),
                descripcion = context.getString(R.string.fest_33_desc),
                fecha = "05/04/2026",
                dia = 5,
                mes = 4,
                ubicacion = "Huamanga",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778859518/resurreccion_vid_zqksre.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778858606/semana_santa_2_c7inni.jpg",
                imagenesGaleria = listOf("https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778858611/semana_santa_1_dkpoap.webp", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778858602/semana_santa_3_vz8f8j.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778858597/semana_santa_4_imm9ya.jpg"),
                recomendacion = context.getString(R.string.fest_33_rec)
            ),
            Festividad(
                id = 34,
                nombre = context.getString(R.string.fest_34_name),
                descripcion = context.getString(R.string.fest_34_desc),
                fecha = "25/04/2026",
                dia = 25,
                mes = 4,
                ubicacion = "Huamanga",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778859540/ani_vid_huamanga_oxdkpv.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778858435/fiesta_huamanga_4_nhny34.jpg",
                imagenesGaleria = listOf("https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778858446/fiesta_huamanga_1_ia8lz5.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778858438/fiesta_huamanga_3_aiwnbw.jpg", "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778858442/fiesta_huamanga_2_hmo90k.jpg"),
                recomendacion = context.getString(R.string.fest_34_rec)
            ),

            // MAYO 2026
            Festividad(
                id = 7,
                nombre = context.getString(R.string.fest_7_name),
                descripcion = context.getString(R.string.fest_7_desc),
                fecha = "01/05/2026",
                dia = 1,
                mes = 5,
                ubicacion = "Lucanas",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778826907/vid_se%C3%B1or_de_untura_auia2k.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778853033/fiesta_untuna_a0ce48.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778853039/fiesta_untuna_2_l6xyls.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778853035/fiesta_untuna_3_l8n2rh.jpg",

                    ),
                recomendacion = context.getString(R.string.fest_7_rec)
            ),
            Festividad(
                id = 8,
                nombre = context.getString(R.string.fest_8_name),
                descripcion = context.getString(R.string.fest_8_desc),
                fecha = "03/05/2026",
                dia = 3,
                mes = 5,
                ubicacion = "Huanta, La Mar, Lucanas",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778827053/vid_fiestas_de_las_cruces_shqrw5.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778853201/fiesta_cruces_1_xas1l2.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778853198/fiesta_cruces_2_qx3r6z.webp",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778853194/fiesta_cruces_3_xxovxe.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778853191/fiesta_cruces_4_ybleyt.jpg"
                ),
                recomendacion = context.getString(R.string.fest_8_rec)
            ),
            Festividad(
                id = 9,
                nombre = context.getString(R.string.fest_9_name),
                descripcion = context.getString(R.string.fest_9_desc),
                fecha = "13/05/2026",
                dia = 13,
                mes = 5,
                ubicacion = "Ayacucho",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778827182/vid_virgen_fatima_rg5bgy.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778853413/fiesta_fatima_1_lajt09.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778853416/fiesta_fatima_2_sfbquo.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778853409/fiesta_fatima_3_l7qnwx.jpg",

                    ),
                recomendacion = context.getString(R.string.fest_9_rec)
            ),

            // JUNIO 2026
            Festividad(
                id = 10,
                nombre = context.getString(R.string.fest_10_name),
                descripcion = context.getString(R.string.fest_10_desc),
                fecha = "24/06/2026",
                dia = 24,
                mes = 6,
                ubicacion = "Paico, Sucre, Ayacucho",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778827556/vid_fiesta_sanjuan_gf7ulv.mp4",
                imagenUrl = "",
                imagenesGaleria = listOf(
                    "",
                    "",
                    ""
                ),
                recomendacion = context.getString(R.string.fest_10_rec)
            ),
            Festividad(
                id = 11,
                nombre = context.getString(R.string.fest_11_name),
                descripcion = context.getString(R.string.fest_11_desc),
                fecha = "16/06/2026",
                dia = 16,
                mes = 6,
                ubicacion = "puquio, lucanas, ayacucho",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778827733/corpus_christi_ayacucho_cntsmx.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778854224/fiesta_corpus_1_pfsl1d.png",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778854227/fiesta_corpus_2_dshb0v.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778854221/fiesta_corpus_3_x62w6t.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778854219/fiesta_corpus_4_olq6bf.jpg"
                ),
                recomendacion = context.getString(R.string.fest_11_rec)
            ),

            // JULIO 2026
            Festividad(
                id = 12,
                nombre = context.getString(R.string.fest_12_name),
                descripcion = context.getString(R.string.fest_12_desc),
                fecha = "18/07/2026",
                dia = 18,
                mes = 7,
                ubicacion = "Huamanga",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778828220/vid_virgen_carmen_z0cuwd.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778854616/fiesta_carmen_1_elxwu8.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778854609/fiesta_carmen_4_ihbobs.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778854606/fiesta_carmen_3_qxpc9g.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778854612/fiesta_carmen_2_tqtwyk.jpg"
                ),
                recomendacion = context.getString(R.string.fest_12_rec)
            ),
            Festividad(
                id = 13,
                nombre = context.getString(R.string.fest_13_name),
                descripcion = context.getString(R.string.fest_13_desc),
                fecha = "25/07/2026",
                dia = 25,
                mes = 7,
                ubicacion = "Pausa, Ayacucho",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778828405/vid_santiago_qumyvg.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778854797/fiesta_santiago_1_fogj2d.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778854793/fiesta_santiago_4_bdgcxv.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778854790/fiesta_santiago_2_jsnfol.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778854786/fiesta_santiago_3_ojrdwf.jpg"
                ),
                recomendacion = context.getString(R.string.fest_13_rec)
            ),
            Festividad(
                id = 14,
                nombre = context.getString(R.string.fest_14_name),
                descripcion = context.getString(R.string.fest_14_desc),
                fecha = "28/07/2026",
                dia = 28,
                mes = 7,
                ubicacion = "Huamanga",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778828614/vid_waqrapukus_saqr58.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778854988/waqrapukus_1_vhulwc.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778854984/waqrapukus_2_bai9hz.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778854981/waqrapukus_3_y5j7gy.jpg",

                    ),
                recomendacion = context.getString(R.string.fest_14_rec)
            ),
            Festividad(
                id = 15,
                nombre = context.getString(R.string.fest_15_name),
                descripcion = context.getString(R.string.fest_15_desc),
                fecha = "29/07/2026",
                dia = 29,
                mes = 7,
                ubicacion = "Parinacochas",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778828742/vid_virgen_nieves_qu4mko.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855142/fiesta_nieves_1_rmjgo4.webp",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855145/fiesta_nieves_2_yxpkby.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855138/fiesta_nieves_3_ckiwod.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855134/fiesta_nieves_4_h9kxj2.jpg"
                ),
                recomendacion = context.getString(R.string.fest_15_rec)
            ),

            // AGOSTO 2026
            Festividad(
                id = 16,
                nombre = context.getString(R.string.fest_16_name),
                descripcion = context.getString(R.string.fest_16_desc),
                fecha = "01/08/2026",
                dia = 1,
                mes = 8,
                ubicacion = "Cangallo, Huanta, Víctor Fajardo",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778828862/vid_herranza_ghixvx.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855354/fiesta_herranza_1_mzekjk.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855350/fiesta_herranza_2_gilm5w.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855347/fiesta_herranza_3_xea4bk.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855343/fiesta_herranza_4_hcodjs.webp"
                ),
                recomendacion = context.getString(R.string.fest_16_rec)
            ),
            Festividad(
                id = 17,
                nombre = context.getString(R.string.fest_17_name),
                descripcion = context.getString(R.string.fest_17_desc),
                fecha = "15/08/2026",
                dia = 15,
                mes = 8,
                ubicacion = "Cangallo, Víctor Fajardo, Castrovirreyna",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778828998/vid_virgen_asuncion_ugxmlh.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855508/fiesta_asuncion_1_guvjwx.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855504/fiesta_asuncion_2_qfyjs1.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855500/fiesta_asuncion_3_xez0nb.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855512/fiesta_asuncion_4_hj54tj.jpg"
                ),
                recomendacion = context.getString(R.string.fest_17_rec)
            ),
            Festividad(
                id = 18,
                nombre = context.getString(R.string.fest_18_name),
                descripcion = context.getString(R.string.fest_18_desc),
                fecha = "21/08/2026",
                dia = 21,
                mes = 8,
                ubicacion = "Lucanas",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778829152/vid_san_isidro_mkq3yw.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855663/fiesta_isidro_1_jadf1s.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855667/fiesta_isidro_4_fgfra3.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855660/fiesta_isidro_2_imywe0.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778855656/fiesta_isidro_3_ww0fwb.jpg"
                ),
                recomendacion = context.getString(R.string.fest_18_rec)
            ),
            Festividad(
                id = 19,
                nombre = context.getString(R.string.fest_19_name),
                descripcion = context.getString(R.string.fest_19_desc),
                fecha = "24/08/2026",
                dia = 24,
                mes = 8,
                ubicacion = "Cangallo",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778829279/vid_san_bartolome_lpikib.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856055/bartolome_1_igur9w.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856053/bartolome_2_ofuikm.png",

                    ),
                recomendacion = context.getString(R.string.fest_19_rec)
            ),
            Festividad(
                id = 20,
                nombre = context.getString(R.string.fest_20_name),
                descripcion = context.getString(R.string.fest_20_desc),
                fecha = "26/08/2026",
                dia = 26,
                mes = 8,
                ubicacion = "La Mar",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778829371/vid_san_miguel_wbdprb.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856333/fiesta_san_miguel_1_ewo2mh.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856337/fiesta_san_miguel_4_lvwyct.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856329/fiesta_san_miguel_2_hdaiiq.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856325/fiesta_san_miguel_3_a4da6p.jpg"
                ),
                recomendacion = context.getString(R.string.fest_20_rec)
            ),

            // SETIEMBRE 2026
            Festividad(
                id = 21,
                nombre = context.getString(R.string.fest_21_name),
                descripcion = context.getString(R.string.fest_21_desc),
                fecha = "08/09/2026",
                dia = 8,
                mes = 9,
                ubicacion = "Lucanas",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778829466/vid_yarqa_aspi_o7wrlo.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856601/yarqa_aspi_1_l8wxtl.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856605/yarqa_aspi_2_nbqz9l.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856597/yarqa_aspi_3_ockvdt.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856594/yarqa_aspi_4_hgkhva.jpg"
                ),
                recomendacion = context.getString(R.string.fest_21_rec)
            ),
            Festividad(
                id = 22,
                nombre = context.getString(R.string.fest_22_name),
                descripcion = context.getString(R.string.fest_22_desc),
                fecha = "12/09/2026",
                dia = 12,
                mes = 9,
                ubicacion = "Huamanga",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778829554/vid_se%C3%B1or_quinuapata_hlfuyt.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856760/fiesta_quinuapata_1_cszegg.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856756/fiesta_quinuapata_2_mr0gke.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856752/fiesta_quinuapata_4_xwtlrx.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856748/fiesta_quinuapata_3_fpxill.jpg"
                ),
                recomendacion = context.getString(R.string.fest_22_rec)
            ),
            Festividad(
                id = 23,
                nombre = context.getString(R.string.fest_23_name),
                descripcion = context.getString(R.string.fest_23_desc),
                fecha = "14/09/2026",
                dia = 14,
                mes = 9,
                ubicacion = "Lucanas",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778829670/vid_fiesta_agua_nqhkjw.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856901/fiesta_agua_1_rsdhey.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856897/fiesta_agua_2_fjeckd.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856893/fiesta_agua_3_iqassy.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778856889/fiesta_agua_4_onnvom.webp"
                ),
                recomendacion = context.getString(R.string.fest_23_rec)
            ),
            Festividad(
                id = 24,
                nombre = context.getString(R.string.fest_24_name),
                descripcion = context.getString(R.string.fest_24_desc),
                fecha = "24/09/2026",
                dia = 24,
                mes = 9,
                ubicacion = "Cangallo",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778829865/vid_viegen_mercedes_iswd2z.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778857079/fiesta_mercedes_xvblyk.jpg",
                imagenesGaleria = listOf(
                    "",
                    "",
                    ""
                ),
                recomendacion = context.getString(R.string.fest_24_rec)
            ),

            // OCTUBRE 2026
            Festividad(
                id = 25,
                nombre = context.getString(R.string.fest_25_name),
                descripcion = context.getString(R.string.fest_25_desc),
                fecha = "04/10/2026",
                dia = 4,
                mes = 10,
                ubicacion = "Parinacochas",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778830029/san_francisco_vid_upapj4.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778857168/fiesta_san_francisco_1_t2juxn.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778857172/fiesta_san_francisco_3_w9ift7.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778857163/fiesta_san_francisco_2_v0fo0w.jpg",

                    ),
                recomendacion = context.getString(R.string.fest_25_rec)
            ),

            // NOVIEMBRE 2026
            Festividad(
                id = 26,
                nombre = context.getString(R.string.fest_26_name),
                descripcion = context.getString(R.string.fest_26_desc),
                fecha = "01/11/2026",
                dia = 1,
                mes = 11,
                ubicacion = "Huamanga",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778830134/todo_los_santos_vid_ixitmn.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778857489/santos_1_llwlit.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778857481/santos_3_uqdw4f.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778857484/santos_2_hk7k7i.jpg",

                    ),
                recomendacion = context.getString(R.string.fest_26_rec)
            ),
            Festividad(
                id = 27,
                nombre = context.getString(R.string.fest_27_name),
                descripcion = context.getString(R.string.fest_27_desc),
                fecha = "07/11/2026",
                dia = 6,
                mes = 11,
                ubicacion = "Ayacucho",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778830256/folclor_ayacuchano_vid_unl3ey.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778857623/fiesta_folclor_1_ir8z3a.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778857618/fiesta_folclor_2_nznxir.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778857614/fiesta_folclor_4_yn3jgh.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778857610/fiesta_folclor_3_o8qk7n.jpg"
                ),
                recomendacion = context.getString(R.string.fest_27_rec)
            ),

            // DICIEMBRE 2026
            Festividad(
                id = 30,
                nombre = context.getString(R.string.fest_30_name),
                descripcion = context.getString(R.string.fest_30_desc),
                fecha = "09/12/2026",
                dia = 9,
                mes = 12,
                ubicacion = "Huamanga, Ayacucho",
                videoUrl = "https://res.cloudinary.com/dcvm5i1yh/video/upload/q_auto/f_auto/v1778830699/vid_batalla_ayacucho_nidw67.mp4",
                imagenUrl = "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778857830/batalla_1_a17nuj.jpg",
                imagenesGaleria = listOf(
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778857825/batalla_3_lonz6w.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778857821/batalla_2_vgb0nv.jpg",
                    "https://res.cloudinary.com/dcvm5i1yh/image/upload/q_auto/f_auto/v1778857816/batalla_4_dq0nvs.jpg"
                ),
                recomendacion = context.getString(R.string.fest_30_rec)
            )
        )
    }
}
