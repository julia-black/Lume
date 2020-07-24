package com.singlelab.lume.ui.participants.interactor

import com.singlelab.net.model.event.ParticipantRequest

interface ParticipantsInteractor {
    suspend fun approvePerson(participantRequest: ParticipantRequest)
}