package com.singlelab.net.model.event

import com.singlelab.net.model.person.PersonResponse

class EventResponse(
    val eventUid: String? = null,
    val name: String,
    val minAge: Int?,
    val maxAge: Int?,
    val xCoordinate: Float,
    val yCoordinate: Float,
    val description: String?,
    val startTime: String,
    val endTime: String,
    val status: Int = 0,
    val type: Int = 0,
    val eventImageContentUid: String? = null,
    val chatUid: String? = null,
    val participants: List<PersonResponse> = listOf(),
    val administrator: PersonResponse? = null,
    val isOpenForInvitations: Boolean = true
) {
    fun getApprovedParticipants(): List<PersonResponse> {
        return participants.filter {
            it.participantStatus == ParticipantStatus.ACTIVE.id
        }
    }

    fun getNotApprovedParticipants(): List<PersonResponse> {
        return participants.filter {
            it.participantStatus == ParticipantStatus.WAITING_FOR_APPROVE_FROM_EVENT.id
        }
    }
}