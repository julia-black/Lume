package com.singlelab.lume.ui.swiper_event.interactor

import com.singlelab.data.model.event.Event
import com.singlelab.data.repositories.BaseRepository
import com.singlelab.data.repositories.events.EventsRepository
import com.singlelab.lume.base.BaseInteractor

class SwiperEventInteractorImpl(private val repository: EventsRepository) : SwiperEventInteractor,
    BaseInteractor(repository as BaseRepository) {

    override suspend fun getEvent(uid: String): Event? {
        return repository.getEvent(uid)
    }
}