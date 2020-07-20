package com.singlelab.lume.ui.creating_event.interactor

import com.singlelab.data.model.event.Event
import com.singlelab.data.model.event.ResponseEventUid
import com.singlelab.data.repositories.BaseRepository
import com.singlelab.data.repositories.events.EventsRepository
import com.singlelab.lume.base.BaseInteractor

class CreatingEventInteractorImpl(private val repository: EventsRepository) :
    CreatingEventInteractor,
    BaseInteractor(repository as BaseRepository) {
    override suspend fun createEvent(event: Event): ResponseEventUid? {
        return repository.createEvent(event)
    }
}