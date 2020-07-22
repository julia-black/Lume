package com.singlelab.lume.ui.event.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.model.event.Event
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.events.EventsRepository

class EventInteractorImpl(private val repository: EventsRepository) : EventInteractor,
    BaseInteractor(repository as BaseRepository) {

    override suspend fun getEvent(uid: String): Event? {
        return Event.fromResponse(repository.getEvent(uid))
    }
}