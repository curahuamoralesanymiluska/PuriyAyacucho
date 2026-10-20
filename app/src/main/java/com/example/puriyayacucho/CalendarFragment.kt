package com.example.puriyayacucho

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.puriyayacucho.adapters.FestividadCalendarAdapter
import com.example.puriyayacucho.data.Festividad
import com.example.puriyayacucho.data.FestividadFactory
import com.google.android.material.card.MaterialCardView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.*
import org.threeten.bp.DayOfWeek

class CalendarFragment : Fragment() {

    private lateinit var calendarView: MaterialCalendarView
    private lateinit var cvNextEvent: MaterialCardView
    private lateinit var tvNextEventName: TextView
    private lateinit var tvNextEventDate: TextView
    private lateinit var ivNextEventImage: ImageView
    private lateinit var layoutNearAlert: View
    private lateinit var rvFestividadesMes: RecyclerView
    private lateinit var tvMonthEventsTitle: TextView
    private lateinit var cvPlayVideoButton: MaterialCardView
    private lateinit var adapter: FestividadCalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        view.findViewById<View>(R.id.cardEventosBoletos)?.setOnClickListener {
            startActivity(Intent(requireContext(), EventosActivity::class.java))
        }
        setupRecyclerView()
        setupCalendar()
        showNextFestivity()

        // Cargar festividades del mes actual al iniciar
        val mesActual = Calendar.getInstance().get(Calendar.MONTH) + 1
        actualizarListaFestividades(mesActual)
    }

    private fun initializeViews(view: View) {
        calendarView = view.findViewById(R.id.calendarView)
        cvNextEvent = view.findViewById(R.id.cvNextEvent)
        tvNextEventName = view.findViewById(R.id.tvNextEventName)
        tvNextEventDate = view.findViewById(R.id.tvNextEventDate)
        ivNextEventImage = view.findViewById(R.id.ivNextEventImage)
        layoutNearAlert = view.findViewById(R.id.layoutNearAlert)
        rvFestividadesMes = view.findViewById(R.id.rvFestividadesMes)
        tvMonthEventsTitle = view.findViewById(R.id.tvMonthEventsTitle)
        cvPlayVideoButton = view.findViewById(R.id.cvPlayVideoButton)
    }

    private fun setupRecyclerView() {
        adapter = FestividadCalendarAdapter(emptyList()) { festividad ->
            abrirDetallesFestividad(festividad)
        }
        rvFestividadesMes.layoutManager = LinearLayoutManager(context)
        rvFestividadesMes.adapter = adapter
    }

    private fun setupCalendar() {
        // Configuración inicial del calendario
        calendarView.state().edit()
            .setFirstDayOfWeek(DayOfWeek.SUNDAY)
            .commit()

        // Decorador para el día de HOY
        calendarView.addDecorator(TodayDecorator())

        // Decorar los días que tienen festividades
        val context = requireContext()
        val festividades = FestividadFactory.obtenerFestividadesAyacucho(context)
        val eventDates = festividades.map {
            CalendarDay.from(2026, it.mes, it.dia)
        }.toSet()

        calendarView.addDecorator(EventDecorator(Color.parseColor("#FF3355"), eventDates))

        calendarView.setOnDateChangedListener { _, day, _ ->
            val selectedDate = String.format("%02d/%02d", day.day, day.month)
            val festividad = festividades.find {
                it.fecha.startsWith(selectedDate)
            }

            if (festividad != null) {
                abrirDetallesFestividad(festividad)
            }
        }

        calendarView.setOnMonthChangedListener { _, date ->
            actualizarListaFestividades(date.month)
        }
    }

    private fun actualizarListaFestividades(mes: Int) {
        val festividadesMes = FestividadFactory.obtenerFestividadesAyacucho(requireContext())
            .filter { it.mes == mes }
            .sortedBy { it.dia }

        if (festividadesMes.isEmpty()) {
            tvMonthEventsTitle.text = getString(R.string.no_festivities, obtenerNombreMes(mes))
        } else {
            tvMonthEventsTitle.text = getString(R.string.month_festivities, obtenerNombreMes(mes))
        }

        adapter.updateData(festividadesMes)
    }

    private fun obtenerNombreMes(mes: Int): String {
        return when (mes) {
            1 -> "ENERO"
            2 -> "FEBRERO"
            3 -> "MARZO"
            4 -> "ABRIL"
            5 -> "MAYO"
            6 -> "JUNIO"
            7 -> "JULIO"
            8 -> "AGOSTO"
            9 -> "SEPTIEMBRE"
            10 -> "OCTUBRE"
            11 -> "NOVIEMBRE"
            12 -> "DICIEMBRE"
            else -> ""
        }
    }

    private fun showNextFestivity() {
        val hoy = Calendar.getInstance()
        val mesActual = hoy.get(Calendar.MONTH) + 1
        val diaActual = hoy.get(Calendar.DAY_OF_MONTH)

        val festividades = FestividadFactory.obtenerFestividadesAyacucho(requireContext())

        val proxima = festividades.filter {
            it.mes > mesActual || (it.mes == mesActual && it.dia >= diaActual)
        }.minByOrNull { (it.mes * 100) + it.dia } ?: festividades.first()

        tvNextEventName.text = proxima.nombre
        tvNextEventDate.text = proxima.fecha

        Glide.with(this)
            .load(proxima.imagenUrl)
            .placeholder(R.drawable.plaza)
            .centerCrop()
            .into(ivNextEventImage)

        val diffDays = (proxima.dia - diaActual) + (proxima.mes - mesActual) * 30
        layoutNearAlert.visibility = if (diffDays in 0..7) View.VISIBLE else View.GONE

        cvNextEvent.setOnClickListener {
            abrirDetallesFestividad(proxima)
        }

        // Configurar el botón de video para la próxima festividad
        cvPlayVideoButton.setOnClickListener {
            if (proxima.videoUrl.isNotEmpty()) {
                val intent = Intent(context, VideoActivity::class.java).apply {
                    putExtra("videoId", proxima.videoUrl)
                    putExtra("FESTIVIDAD_NAME", proxima.nombre)
                }
                startActivity(intent)
            }
        }
    }

    private fun abrirDetallesFestividad(festividad: Festividad) {
        val intent = Intent(context, PlaceDetailsActivity::class.java).apply {
            putExtra("placeName", festividad.nombre)
            putExtra("description", festividad.descripcion)
            putExtra("category", "Festividad")
            putExtra("hours", "Fecha: ${festividad.fecha}")
            putExtra("location", festividad.ubicacion)
            putExtra("recommendation", festividad.recomendacion)
            putExtra("imageUrl", festividad.imagenUrl)
            putExtra("videoUrl", festividad.videoUrl)
            putExtra("placeId", festividad.id + 5000)
            putStringArrayListExtra("galleryUrls", ArrayList(festividad.imagenesGaleria))
        }
        startActivity(intent)
    }

    // Decorador para resaltar el día de HOY
    inner class TodayDecorator : DayViewDecorator {
        private val today = CalendarDay.today()
        private val background: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.today_marker)

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day == today
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(StyleSpan(Typeface.BOLD))
            view.addSpan(RelativeSizeSpan(1.2f))
            if (background != null) {
                view.setBackgroundDrawable(background)
            }
        }
    }

    // Decorador para marcar los días con puntos o resaltado
    inner class EventDecorator(private val color: Int, private val dates: Collection<CalendarDay>) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(color))
            view.addSpan(StyleSpan(Typeface.BOLD))
            view.addSpan(DotSpan(8f, color))
        }
    }
}
