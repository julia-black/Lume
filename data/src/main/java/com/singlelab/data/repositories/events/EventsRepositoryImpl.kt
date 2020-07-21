package com.singlelab.data.repositories.events

import com.singlelab.data.model.event.Event
import com.singlelab.data.model.event.EventSummary
import com.singlelab.data.model.event.ResponseEventUid
import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.BaseRepository

class EventsRepositoryImpl(private val apiUnit: ApiUnit) : EventsRepository, BaseRepository() {
    override suspend fun createEvent(event: Event): ResponseEventUid? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.addEventAsync(event).await() },
            errorMessage = "Не удалось создать событие"
        )
    }

    override suspend fun getEvents(): List<EventSummary>? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.getEventsAsync().await() },
            errorMessage = "Не удалось получить список событий"
        )
    }

    override suspend fun getEvent(uid: String): Event? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.getEventAsync(uid).await() },
            errorMessage = "Не удалось получить список событий"
        )
    }
}