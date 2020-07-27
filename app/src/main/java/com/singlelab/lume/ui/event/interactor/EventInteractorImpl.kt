package com.singlelab.lume.ui.event.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.model.event.Event
import com.singlelab.net.model.event.ParticipantRequest
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.events.EventsRepository

class EventInteractorImpl(private val repository: EventsRepository) : EventInteractor,
    BaseInteractor(repository as BaseRepository) {

    override suspend fun getEvent(uid: String): Event? {
        return Event.fromResponse(repository.getEvent(uid))
    }

    override suspend fun acceptEvent(participantRequest: ParticipantRequest): Event? {
        return Event.fromResponse(repository.updateParticipants(participantRequest))
    }

    override suspend fun rejectEvent(personUid: String, eventUid: String) {
        repository.removeParticipants(personUid, eventUid)
    }
}