package com.singlelab.lume.ui.events

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.event.EventSummary
import com.singlelab.lume.ui.events.adapter.DaysAdapter
import com.singlelab.lume.ui.events.adapter.OnEventItemClickListener
import com.singlelab.lume.ui.view.calendar.CircleDecorator
import com.singlelab.lume.ui.view.calendar.FutureDaysDecorator
import com.singlelab.lume.ui.view.calendar.PastDaysDecorator
import com.singlelab.lume.ui.view.calendar.SelectorDecorator
import com.singlelab.lume.util.*
import com.singlelab.net.model.event.ParticipantStatus
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

    private var currentDayDecorator: CircleDecorator? = null

    private var firstDayInWeek: Int? = null

    private val callbackBackPressed: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showViewPager()
        button_create_event.setOnClickListener {
            Navigation.createNavigateOnClickListener(R.id.action_events_to_creating_event)
                .onClick(view)
        }
        initWeekCalendar()
        showToday()
        showCalendar(Calendar.getInstance())
        button_calendar.setOnClickListener {
            showFullCalendar(!calendar_full_view.isVisible)
        }
    }

    private fun initWeekCalendar() {
        val today = CalendarDay.today()
        calendar_week_view.apply {
            addDecorators(
                SelectorDecorator(context),
                PastDaysDecorator(today),
                FutureDaysDecorator(today)
            )
            topbarVisible = false
            setOnDateChangedListener(this@EventsFragment)
            state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit()
        }
    }

    private fun showViewPager() {
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.page_margin)
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.page_offset)
        view_pager_events?.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            setPageTransformer { page, position ->
                val viewPager = page.parent.parent as ViewPager2
                val offset = position * -(2 * offsetPx + pageMarginPx)
                if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.translationX = -offset
                    } else {
                        page.translationX = offset
                    }
                } else {
                    page.translationY = offset
                }
            }
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    onDaySelected(position)
                }
            })
        }
    }

    override fun showEvents(days: List<Pair<CalendarDay, List<EventSummary>>>) {
        view_pager_events?.apply {
            adapter = DaysAdapter(days, this@EventsFragment)
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
            val decorators = listOf(pastDecorator, futureDecorator, inviteDecorator)
            calendar_week_view.addDecorators(decorators)
            calendar_full_view.showView(this, decorators)
            presenter.onShowCurrentDay(futureDaysWithEvent, pastDaysWithEvent)
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
        val days = (view_pager_events.adapter as DaysAdapter).getList()
        val position = days.indexOfFirst { it.first == date }
        if (position > 0) {
            showFullCalendar(false)
            view_pager_events.setCurrentItem(position, true)
        }
    }

    override fun showCurrentDayOnPager(day: CalendarDay) {
        view_pager_events.apply {
            val days = (view_pager_events.adapter as DaysAdapter).getList()
            val position = days.indexOfFirst { it.first == day }
            if (presenter.currentDayPosition != position) {
                presenter.currentDayPosition = position
                setCurrentItem(position, true)
            }
        }
    }

    private fun showToday() {
        val calendar = Calendar.getInstance()
        val dayOfWeek =
            calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        day_of_week.text = dayOfWeek?.toUpFirstSymbol()
        date.text = calendar.parseToString(Const.DATE_FORMAT_SIMPLE)
    }

    private fun showCalendar(calendar: Calendar) {
        calendar.time
        val firstAndLastWeekDays = calendar.getFirstAndLastDayOfWeek()
        if (firstAndLastWeekDays.first.day != firstDayInWeek) {
            firstDayInWeek = firstAndLastWeekDays.first.day
            calendar_week_view.state().edit()
                .setMinimumDate(firstAndLastWeekDays.first)
                .setMaximumDate(firstAndLastWeekDays.second)
                .commit()
        }
    }

    private fun showFullCalendar(isShow: Boolean) {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callbackBackPressed
        )
        calendar_full_view.addDecorator(currentDayDecorator)
        calendar_full_view.visibility = if (isShow) View.VISIBLE else View.INVISIBLE
    }

    private fun onDaySelected(position: Int) {
        (view_pager_events.adapter as DaysAdapter).collapseCards()
        val dayWithEvent = (view_pager_events.adapter as DaysAdapter).getList()[position]
        val isInvite =
            dayWithEvent.second.find { it.participantStatus == ParticipantStatus.WAITING_FOR_APPROVE_FROM_USER } != null
        val day = dayWithEvent.first
        val today = CalendarDay.today()
        context?.let {
            showCalendar(day.toCalendar())
            calendar_week_view.removeDecorator(currentDayDecorator)
            if (!isInvite || day.isBefore(today)) {
                currentDayDecorator = CircleDecorator(
                    color = ContextCompat.getColor(
                        it,
                        if (day.isAfter(CalendarDay.today())) R.color.colorAccent else R.color.colorGray
                    ),
                    style = Paint.Style.FILL,
                    daysWithEvent = listOf(day),
                    textColor = Color.WHITE
                )
                calendar_week_view.addDecorator(currentDayDecorator)
            }
        }
    }

    private fun onBackPressed() {
        showFullCalendar(false)
        callbackBackPressed.remove()
    }
}