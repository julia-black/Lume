package com.singlelab.lume.ui.events.interactor

import com.singlelab.lume.model.event.EventSummary

interface EventsInteractor {
    suspend fun getEvents(): List<EventSummary>?
}