package com.singlelab.lume.ui.search_event.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.events.EventsRepository

class SearchEventInteractorImpl(private val repository: EventsRepository) : SearchEventInteractor,
    BaseInteractor(repository as BaseRepository) {
}