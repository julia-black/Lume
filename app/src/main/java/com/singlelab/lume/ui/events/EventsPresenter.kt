package com.singlelab.lume.ui.events

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.event.EventStatus
import com.singlelab.lume.model.event.EventSummary
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.events.interactor.EventsInteractor
import com.singlelab.lume.util.toCalendarDay
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.exceptions.NotConnectionException
import com.singlelab.net.model.event.ParticipantStatus
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class EventsPresenter @Inject constructor(
    private val interactor: EventsInteractor,
    preferences: Preferences?
) : BasePresenter<EventsView>(preferences, interactor as BaseInteractor) {

    private var allDays: MutableMap<CalendarDay, MutableList<EventSummary>> = mutableMapOf()

    private var allEvents: List<EventSummary>? = null

    private var pastEvents = mutableListOf<EventSummary>()
    private var newInviteEvents = mutableListOf<EventSummary>()
    private var futureEvents = mutableListOf<EventSummary>()

    var currentDayPosition: Int? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadEvents()
    }

    fun loadEvents(currentEventUid: String? = null) {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                allEvents = interactor.getEvents()
                parseEventsToDays(allEvents)
                filterEvents(allEvents)
                val countInvites = allEvents?.count {
                    it.participantStatus == ParticipantStatus.WAITING_FOR_APPROVE_FROM_USER
                } ?: 0
                runOnMainThread {
                    viewState.showLoading(false)
                    allEvents?.let {
                        viewState.showEvents(parseToList(allDays), countInvites)
                        viewState.showEventsOnCalendar(
                            pastEvents,
                            newInviteEvents,
                            futureEvents,
                            getCurrentDay(currentEventUid)
                        )
                    }
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(false)
                    viewState.showError(
                        message = e.message,
                        withRetry = e is NotConnectionException,
                        callRetry = { loadEvents(currentEventUid) }
                    )
                }
            }
        }
    }

    private fun getCurrentDay(currentEventUid: String?): CalendarDay? {
        currentEventUid ?: return null
        allDays.forEach {
            if (it.value.find { event -> event.eventUid == currentEventUid } != null) {
                return it.key
            }
        }
        return null
    }

    private fun parseToList(allDays: MutableMap<CalendarDay, MutableList<EventSummary>>): List<Pair<CalendarDay, List<EventSummary>>> {
        return allDays.map {
            Pair(it.key, it.value)
        }
    }

    private fun parseEventsToDays(allEvents: List<EventSummary>?) {
        allDays.clear()
        allEvents?.forEach {
            val calendarDay = it.startTime.toCalendarDay(Const.DATE_FORMAT_TIME_ZONE)
            if (allDays.contains(calendarDay)) {
                allDays[calendarDay]?.add(it)
            } else {
                allDays[calendarDay] = mutableListOf(it)
            }
        }
    }

    private fun filterEvents(allEvents: List<EventSummary>?) {
        pastEvents.clear()
        newInviteEvents.clear()
        futureEvents.clear()
        allEvents?.forEach {
            when {
                it.status == EventStatus.ENDED || it.status == EventStatus.CANCELLED -> {
                    pastEvents.add(it)
                }
                it.participantStatus == ParticipantStatus.WAITING_FOR_APPROVE_FROM_USER -> {
                    newInviteEvents.add(it)
                }
                else -> {
                    futureEvents.add(it)
                }
            }
        }
    }

    fun onShowCurrentDay(
        futureDaysWithEvent: List<CalendarDay>,
        pastDaysWithEvent: List<CalendarDay>,
        currentDay: CalendarDay?
    ) {
        if (currentDay == null) {
            if (futureDaysWithEvent.isEmpty()) {
                if (pastDaysWithEvent.isNotEmpty()) {
                    val lastDay = pastDaysWithEvent.last()
                    viewState.showCurrentDayOnPager(lastDay)
                }
            } else {
                val nextDay = futureDaysWithEvent[0]
                viewState.showCurrentDayOnPager(nextDay)
            }
        } else {
            viewState.showCurrentDayOnPager(currentDay)
        }
    }
}