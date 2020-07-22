package com.singlelab.net.repositories.events

import com.singlelab.net.ApiUnit
import com.singlelab.net.model.event.EventRequest
import com.singlelab.net.model.event.EventResponse
import com.singlelab.net.model.event.EventSummaryResponse
import com.singlelab.net.model.event.EventUidResponse
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
            errorMessage = "Не удалось получить список событий"
        )
    }
}