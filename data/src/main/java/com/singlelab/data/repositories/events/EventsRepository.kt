package com.singlelab.data.repositories.events

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.event.Event
import com.singlelab.data.model.event.EventSummary
import com.singlelab.data.model.event.ResponseEventUid

interface EventsRepository {
    @Throws(ApiException::class)
    suspend fun createEvent(event: Event): ResponseEventUid?

    @Throws(ApiException::class)
    suspend fun getEvents(): List<EventSummary>?

    @Throws(ApiException::class)
    suspend fun getEvent(uid: String): Event?
}