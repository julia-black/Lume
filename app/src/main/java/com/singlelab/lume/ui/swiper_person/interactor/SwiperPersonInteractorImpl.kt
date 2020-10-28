package com.singlelab.lume.ui.swiper_person.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.model.profile.Person
import com.singlelab.net.model.event.ParticipantRequest
import com.singlelab.net.model.person.RandomPersonRequest
import com.singlelab.net.model.person.ReportPersonRequest
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.events.EventsRepository
import com.singlelab.net.repositories.person.PersonRepository

class SwiperPersonInteractorImpl(
    private val repository: EventsRepository,
    private val personRepository: PersonRepository
) : SwiperPersonInteractor, BaseInteractor(repository as BaseRepository) {
    override suspend fun getRandomPerson(randomPersonRequest: RandomPersonRequest): Person? {
        return Person.fromResponse(repository.getRandomPerson(randomPersonRequest))
    }

    override suspend fun invitePerson(participantRequest: ParticipantRequest) {
        repository.acceptRandomPerson(participantRequest)
    }

    override suspend fun rejectPerson(eventUid: String, personUid: String) {
        repository.rejectRandomPerson(eventUid, personUid)
    }

    override suspend fun sendReport(uid: String, reasonReport: String) {
        personRepository.sendReport(ReportPersonRequest(uid, reasonReport))
    }
}