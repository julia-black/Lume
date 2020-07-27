package com.singlelab.lume.ui.swiper_person.interactor

import com.singlelab.lume.model.profile.Person
import com.singlelab.net.model.event.ParticipantRequest
import com.singlelab.net.model.person.RandomPersonRequest

interface SwiperPersonInteractor {
    suspend fun getRandomPerson(randomPersonRequest: RandomPersonRequest): Person?

    suspend fun invitePerson(participantRequest: ParticipantRequest)
}