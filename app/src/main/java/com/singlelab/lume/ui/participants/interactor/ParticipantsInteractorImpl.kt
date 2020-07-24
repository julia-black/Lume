package com.singlelab.lume.ui.participants.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.events.EventsRepository

class ParticipantsInteractorImpl(private val repository: EventsRepository) : ParticipantsInteractor,
    BaseInteractor(repository as BaseRepository) {

}