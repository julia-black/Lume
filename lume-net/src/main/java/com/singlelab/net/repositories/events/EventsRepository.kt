package com.singlelab.net.repositories.events

import com.singlelab.net.model.event.*
import com.singlelab.net.model.person.PersonResponse
import com.singlelab.net.model.person.RandomPersonRequest
import com.singlelab.net.model.promo.PromoRewardResponse

interface EventsRepository {
    suspend fun createEvent(event: EventRequest): EventUidResponse?

    suspend fun getEvents(): List<EventSummaryResponse>?

    suspend fun getEvent(uid: String): EventResponse?

    suspend fun getRandomEvent(randomEventRequest: RandomEventRequest): EventResponse?

    suspend fun addParticipants(participantRequest: ParticipantRequest): EventResponse?

    suspend fun updateParticipants(participantRequest: ParticipantRequest): EventResponse?

    suspend fun removeParticipants(personUid: String, eventUid: String): EventResponse?

    suspend fun search(searchEventRequest: SearchEventRequest): List<EventSummaryResponse>?

    suspend fun getRandomPerson(randomPersonRequest: RandomPersonRequest): PersonResponse?

    suspend fun acceptRandomEvent(participantRequest: ParticipantRequest)

    suspend fun rejectRandomEvent(eventUid: String)

    suspend fun acceptRandomPerson(participantRequest: ParticipantRequest)

    suspend fun rejectRandomPerson(eventUid: String, personUid: String)

    suspend fun updateEvent(request: UpdateEventRequest): EventResponse?

    suspend fun checkCityForPromoReward(cityId: Int): PromoRewardResponse?
}