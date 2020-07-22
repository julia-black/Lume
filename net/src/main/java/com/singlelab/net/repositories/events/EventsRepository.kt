package com.singlelab.net.repositories.events

import com.singlelab.net.model.event.EventRequest
import com.singlelab.net.model.event.EventResponse
import com.singlelab.net.model.event.EventSummaryResponse
import com.singlelab.net.model.event.EventUidResponse

interface EventsRepository {
    suspend fun createEvent(event: EventRequest): EventUidResponse?

    suspend fun getEvents(): List<EventSummaryResponse>?

    suspend fun getEvent(uid: String): EventResponse?
}