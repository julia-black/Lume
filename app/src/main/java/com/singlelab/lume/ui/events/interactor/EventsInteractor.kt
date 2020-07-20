package com.singlelab.lume.ui.events.interactor

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.event.EventSummary

interface EventsInteractor {
    @Throws(ApiException::class)
    suspend fun getEvents(): List<EventSummary>?
}