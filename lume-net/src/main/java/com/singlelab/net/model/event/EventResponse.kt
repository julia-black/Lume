package com.singlelab.net.model.event

import com.singlelab.net.model.person.PersonResponse

class EventResponse(
    val eventUid: String? = null,
    val name: String,
    val minAge: Int?,
    val maxAge: Int?,
    val xCoordinate: Double?,
    val yCoordinate: Double?,
    val description: String?,
    val startTime: String,
    val endTime: String,
    val chatUid: String? = null,
    val status: Int = 0,
    val types: List<Int>,
    val participants: List<PersonResponse> = listOf(),
    val administrator: PersonResponse? = null,
    val isOpenForInvitations: Boolean = true,
    val eventPrimaryImageContentUid: String? = null,
    val images: List<String>? = null,
    val cityId: Int? = null,
    val cityName: String? = null,
    val isOnline: Boolean,
    val promoRequestUid: String? = null
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

    fun getInvitedParticipants(): List<PersonResponse> {
        return participants.filter {
            it.participantStatus == ParticipantStatus.WAITING_FOR_APPROVE_FROM_USER.id
        }
    }
}