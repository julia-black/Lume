package com.singlelab.lume.ui.swiper_event.interactor

import com.singlelab.lume.model.event.Event
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.events.EventsRepository
import com.singlelab.lume.base.BaseInteractor

class SwiperEventInteractorImpl(private val repository: EventsRepository) : SwiperEventInteractor,
    BaseInteractor(repository as BaseRepository) {

    override suspend fun getEvent(uid: String): Event? {
        return Event.fromResponse(repository.getEvent(uid))
    }
}