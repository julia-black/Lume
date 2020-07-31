package com.singlelab.lume.ui.swiper_event.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.model.event.Event
import com.singlelab.net.model.event.ParticipantRequest
import com.singlelab.net.model.event.RandomEventRequest
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.events.EventsRepository

class SwiperEventInteractorImpl(private val repository: EventsRepository) : SwiperEventInteractor,
    BaseInteractor(repository as BaseRepository) {

    override suspend fun getRandomEvent(randomEventRequest: RandomEventRequest): Event? {
        return Event.fromResponse(repository.getRandomEvent(randomEventRequest))
    }

    override suspend fun acceptEvent(participantRequest: ParticipantRequest) {
        return repository.acceptRandomEvent(participantRequest)
    }

    override suspend fun rejectEvent(eventUid: String) {
        return repository.rejectRandomEvent(eventUid)
    }
}