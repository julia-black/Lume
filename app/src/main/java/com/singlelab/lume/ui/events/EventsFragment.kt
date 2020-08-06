package com.singlelab.lume.ui.events

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.event.EventSummary
import com.singlelab.lume.ui.events.adapter.EventsAdapter
import com.singlelab.lume.ui.events.adapter.OnEventItemClickListener
import com.singlelab.lume.ui.view.calendar.CircleDecorator
import com.singlelab.lume.ui.view.calendar.FutureDaysDecorator
import com.singlelab.lume.ui.view.calendar.PastDaysDecorator
import com.singlelab.lume.ui.view.calendar.SelectorDecorator
import com.singlelab.lume.util.toCalendarDays
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_events.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class EventsFragment : BaseFragment(), EventsView, OnlyForAuthFragments, OnEventItemClickListener,
    OnDateSelectedListener {

    @Inject
    lateinit var daggerPresenter: EventsPresenter

    @InjectPresenter
    lateinit var presenter: EventsPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_events.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        button_create_event.setOnClickListener {
            Navigation.createNavigateOnClickListener(R.id.action_events_to_creating_event)
                .onClick(view)
        }
        presenter.loadEvents()
        context?.let {
            showCalendar(it)
        }
    }

    override fun showEvents(events: List<EventSummary>) {
        recycler_events.apply {
            adapter = EventsAdapter(events.toMutableList(), this@EventsFragment)
        }
    }

    override fun showEventsOnCalendar(
        pastEvents: MutableList<EventSummary>,
        newInviteEvents: MutableList<EventSummary>,
        futureEvents: MutableList<EventSummary>
    ) {
        context?.let { context ->
            val pastDaysWithEvent =
                pastEvents.map { it.startTime }.toCalendarDays(Const.DATE_FORMAT_TIME_ZONE)
            val pastDecorator = CircleDecorator(
                color = ContextCompat.getColor(context, R.color.colorGray),
                style = Paint.Style.STROKE,
                daysWithEvent = pastDaysWithEvent
            )

            val futureDaysWithEvent =
                futureEvents.map { it.startTime }.toCalendarDays(Const.DATE_FORMAT_TIME_ZONE)
            val futureDecorator = CircleDecorator(
                color = ContextCompat.getColor(context, R.color.colorAccent),
                style = Paint.Style.STROKE,
                daysWithEvent = futureDaysWithEvent
            )

            val inviteDaysWithEvent =
                newInviteEvents.map { it.startTime }.toCalendarDays(Const.DATE_FORMAT_TIME_ZONE)
            val inviteDecorator = CircleDecorator(
                color = ContextCompat.getColor(context, R.color.colorNewInvite),
                style = Paint.Style.FILL,
                textColor = Color.WHITE,
                daysWithEvent = inviteDaysWithEvent
            )

            calendar_view.addDecorators(
                pastDecorator, futureDecorator,
                inviteDecorator
            )
        }
    }

    override fun onClickEvent(uid: String) {
        val action = EventsFragmentDirections.actionEventsToEvent(uid)
        findNavController().navigate(action)
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        Toast.makeText(
            context,
            date.date.toString(),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showCalendar(context: Context) {
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_WEEK] = 2
        val yearMon = calendar[Calendar.YEAR]
        val monthMon = calendar[Calendar.MONTH] + 1
        val dayMon = calendar[Calendar.DAY_OF_MONTH]

        calendar[Calendar.DAY_OF_WEEK] = 1
        val yearSun = calendar[Calendar.YEAR]
        val monthSun = calendar[Calendar.MONTH] + 1
        val daySun = calendar[Calendar.DAY_OF_MONTH]

        val today = CalendarDay.today()

        calendar_view.apply {
            calendar_view.addDecorators(
                SelectorDecorator(context),
                PastDaysDecorator(today),
                FutureDaysDecorator(today)
            )
            topbarVisible = false
            setOnDateChangedListener(this@EventsFragment)
            state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .setMinimumDate(CalendarDay.from(yearMon, monthMon, dayMon))
                .setMaximumDate(CalendarDay.from(yearSun, monthSun, daySun))
                .commit()
        }
    }
}