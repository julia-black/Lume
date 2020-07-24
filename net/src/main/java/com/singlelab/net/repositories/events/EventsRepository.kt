package com.singlelab.net.repositories.events

import com.singlelab.net.model.event.*

interface EventsRepository {
    suspend fun createEvent(event: EventRequest): EventUidResponse?

    suspend fun getEvents(): List<EventSummaryResponse>?

    suspend fun getEvent(uid: String): EventResponse?

    suspend fun getRandomEvent(randomEventRequest: RandomEventRequest): EventResponse?

    suspend fun addParticipantsAsync(participantRequest: ParticipantRequest)
}