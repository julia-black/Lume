package com.singlelab.lume.ui.participants.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.model.event.Event
import com.singlelab.net.model.event.ParticipantRequest
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.events.EventsRepository

class ParticipantsInteractorImpl(private val repository: EventsRepository) : ParticipantsInteractor,
    BaseInteractor(repository as BaseRepository) {

    override suspend fun approvePerson(participantRequest: ParticipantRequest) =
        Event.fromResponse(repository.updateParticipants(participantRequest))

    override suspend fun rejectPerson(personUid: String, eventUid: String) =
        Event.fromResponse(repository.removeParticipants(personUid, eventUid))
}