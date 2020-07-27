package com.singlelab.net.repositories.events

import com.singlelab.net.model.event.*
import com.singlelab.net.model.person.PersonResponse
import com.singlelab.net.model.person.RandomPersonRequest

interface EventsRepository {
    suspend fun createEvent(event: EventRequest): EventUidResponse?

    suspend fun getEvents(): List<EventSummaryResponse>?

    suspend fun getEvent(uid: String): EventResponse?

    suspend fun getRandomEvent(randomEventRequest: RandomEventRequest): EventResponse?

    suspend fun addParticipants(participantRequest: ParticipantRequest)

    suspend fun updateParticipants(participantRequest: ParticipantRequest): EventResponse?

    suspend fun removeParticipants(personUid: String, eventUid: String): EventResponse?

    suspend fun search(searchEventRequest: SearchEventRequest): List<EventSummaryResponse>?

    suspend fun getRandomPerson(randomPersonRequest: RandomPersonRequest): PersonResponse?
}