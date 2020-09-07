package com.singlelab.lume.ui.creating_event.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.net.model.event.EventRequest
import com.singlelab.net.model.event.EventUidResponse
import com.singlelab.net.model.event.UpdateEventRequest
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.events.EventsRepository

class CreatingEventInteractorImpl(private val repository: EventsRepository) :
    CreatingEventInteractor, BaseInteractor(repository as BaseRepository) {

    override suspend fun createEvent(event: EventRequest): EventUidResponse? {
        return repository.createEvent(event)
    }

    override suspend fun updateEvent(request: UpdateEventRequest) {
        repository.updateEvent(request)
    }
}