package com.singlelab.lume.ui.events.interactor

import com.singlelab.data.repositories.BaseRepository
import com.singlelab.data.repositories.events.EventsRepository
import com.singlelab.lume.base.BaseInteractor

class EventsInteractorImpl(private val repository: EventsRepository) : EventsInteractor,
    BaseInteractor(repository as BaseRepository) {
}