package com.singlelab.net.repositories.events

import com.singlelab.net.ApiUnit
import com.singlelab.net.model.event.*
import com.singlelab.net.repositories.BaseRepository

class EventsRepositoryImpl(private val apiUnit: ApiUnit) : EventsRepository, BaseRepository() {
    override suspend fun createEvent(event: EventRequest): EventUidResponse? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.addEventAsync(event).await() },
            errorMessage = "Не удалось создать событие"
        )
    }

    override suspend fun getEvents(): List<EventSummaryResponse>? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.getEventsAsync().await() },
            errorMessage = "Не удалось получить список событий"
        )
    }

    override suspend fun getEvent(uid: String): EventResponse? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.getEventAsync(uid).await() },
            errorMessage = "Не удалось получить события"
        )
    }

    override suspend fun getRandomEvent(randomEventRequest: RandomEventRequest): EventResponse? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.getRandomEventAsync(randomEventRequest).await() },
            errorMessage = "Не удалось получить событие"
        )
    }

    override suspend fun addParticipantsAsync(participantRequest: ParticipantRequest) {
        safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.addParticipantsAsync(participantRequest).await() },
            errorMessage = "Не удалось пригласить пользователя"
        )
    }

    override suspend fun updateParticipantsAsync(participantRequest: ParticipantRequest) {
        safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.updateParticipantsAsync(participantRequest).await() },
            errorMessage = "Не удалось подтвердить"
        )
    }
}