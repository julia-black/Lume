package com.singlelab.lume.ui.event.interactor

import com.singlelab.data.model.event.Event
import com.singlelab.data.repositories.BaseRepository
import com.singlelab.data.repositories.events.EventsRepository
import com.singlelab.lume.base.BaseInteractor

class EventInteractorImpl(private val repository: EventsRepository) : EventInteractor,
    BaseInteractor(repository as BaseRepository) {

    override suspend fun getEvent(uid: String): Event? {
        return repository.getEvent(uid)
    }
}