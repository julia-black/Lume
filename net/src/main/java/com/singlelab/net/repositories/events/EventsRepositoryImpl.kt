package com.singlelab.net.repositories.events

import com.singlelab.net.ApiUnit
import com.singlelab.net.model.event.*
import com.singlelab.net.model.person.PersonResponse
import com.singlelab.net.model.person.RandomPersonRequest
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

    override suspend fun addParticipants(participantRequest: ParticipantRequest) {
        safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.addParticipantsAsync(participantRequest).await() },
            errorMessage = "Не удалось пригласить пользователя"
        )
    }

    override suspend fun updateParticipants(participantRequest: ParticipantRequest): EventResponse? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.updateParticipantsAsync(participantRequest).await() },
            errorMessage = "Не удалось подтвердить"
        )
    }

    override suspend fun removeParticipants(personUid: String, eventUid: String): EventResponse? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.removeParticipantsAsync(personUid, eventUid).await() },
            errorMessage = "Не удалось отклонить"
        )
    }

    override suspend fun search(searchEventRequest: SearchEventRequest): List<EventSummaryResponse>? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.searchEventAsync(searchEventRequest).await() },
            errorMessage = "Не удалось выполнить поиск"
        )
    }

    override suspend fun getRandomPerson(randomPersonRequest: RandomPersonRequest): PersonResponse? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = {
                apiUnit.eventsApi.getRandomPersonAsync(randomPersonRequest).await()
            },
            errorMessage = "Не удалось получить пользователя"
        )
    }

    override suspend fun acceptRandomEvent(participantRequest: ParticipantRequest) {
        safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.acceptRandomEventAsync(participantRequest).await() },
            errorMessage = "Не удалось выполнить действие"
        )
    }

    override suspend fun rejectRandomEvent(eventUid: String) {
        safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.rejectRandomEventAsync(eventUid).await() },
            errorMessage = "Не удалось выполнить действие"
        )
    }

    override suspend fun acceptRandomPerson(participantRequest: ParticipantRequest) {
        safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.acceptRandomPersonAsync(participantRequest).await() },
            errorMessage = "Не удалось выполнить действие"
        )
    }

    override suspend fun rejectRandomPerson(eventUid: String, personUid: String) {
        safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.rejectRandomPersonAsync(eventUid, personUid).await() },
            errorMessage = "Не удалось выполнить действие"
        )
    }
}