package com.singlelab.data.repositories.events

import com.singlelab.data.model.event.Event
import com.singlelab.data.model.event.EventSummary
import com.singlelab.data.model.event.ResponseEventUid

interface EventsRepository {
    suspend fun createEvent(event: Event): ResponseEventUid?

    suspend fun getEvents(): List<EventSummary>?

    suspend fun getEvent(uid: String): Event?
}