package com.singlelab.lume.model.event

import com.singlelab.lume.model.profile.Person
import com.singlelab.net.model.event.EventResponse
import com.singlelab.net.model.event.ParticipantStatus

class Event(
    val eventUid: String? = null,
    val name: String,
    val minAge: Int?,
    val maxAge: Int?,
    val xCoordinate: Double?,
    val yCoordinate: Double?,
    val description: String?,
    val startTime: String,
    val endTime: String,
    val status: Int = 0,
    val type: Int = 0,
    val chatUid: String? = null,
    val participants: List<Person> = listOf(),
    val notApprovedParticipants: List<Person> = listOf(),
    val invitedParticipants: List<Person> = listOf(),
    val administrator: Person? = null,
    val isOpenForInvitations: Boolean = true,
    val eventPrimaryImageContentUid: String? = null,
    val images: List<String>? = null,
    val cityName: String? = null,
    val isOnline: Boolean
) {
    companion object {
        fun fromResponse(eventResponse: EventResponse?): Event? {
            return if (eventResponse != null) {
                Event(
                    eventResponse.eventUid,
                    eventResponse.name,
                    eventResponse.minAge,
                    eventResponse.maxAge,
                    eventResponse.xCoordinate,
                    eventResponse.yCoordinate,
                    eventResponse.description,
                    eventResponse.startTime,
                    eventResponse.endTime,
                    eventResponse.status,
                    eventResponse.type,
                    eventResponse.chatUid,
                    eventResponse.getApprovedParticipants().mapNotNull {
                        Person.fromResponse(it)
                    },
                    eventResponse.getNotApprovedParticipants().mapNotNull {
                        Person.fromResponse(it)
                    },
                    eventResponse.getInvitedParticipants().mapNotNull {
                        Person.fromResponse(it)
                    },
                    Person.fromResponse(eventResponse.administrator),
                    eventResponse.isOpenForInvitations,
                    eventResponse.eventPrimaryImageContentUid,
                    eventResponse.images,
                    eventResponse.cityName,
                    eventResponse.isOnline
                )
            } else {
                null
            }
        }
    }

    fun getParticipantStatus(personUid: String): ParticipantStatus? {
        var person = participants.find {
            it.personUid == personUid
        }
        if (person == null) {
            person = notApprovedParticipants.find {
                it.personUid == personUid
            }
        }
        if (person == null) {
            person = invitedParticipants.find {
                it.personUid == personUid
            }
        }
        return person?.participantStatus
    }
}