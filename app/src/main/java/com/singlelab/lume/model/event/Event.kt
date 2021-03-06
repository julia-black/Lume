package com.singlelab.lume.model.event

import com.singlelab.lume.database.entity.EventSummary
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
    val chatUid: String? = null,
    val status: EventStatus,
    val types: List<EventType>,
    val participants: List<Person> = listOf(),
    val notApprovedParticipants: List<Person> = listOf(),
    val invitedParticipants: List<Person> = listOf(),
    val administrator: Person? = null,
    val isOpenForInvitations: Boolean = true,
    val eventPrimaryImageContentUid: String? = null,
    val images: List<String>? = null,
    val cityId: Int? = null,
    val cityName: String? = null,
    val isOnline: Boolean,
    val promoRequestUid: String? = null
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
                    eventResponse.chatUid,
                    EventStatus.findById(eventResponse.status),
                    eventResponse.types.map { EventType.findById(it) },
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
                    eventResponse.cityId,
                    eventResponse.cityName,
                    eventResponse.isOnline,
                    eventResponse.promoRequestUid
                )
            } else {
                null
            }
        }

        fun fromEventSummaryEntity(eventSummary: EventSummary?): Event? {
            return if (eventSummary != null) {
                Event(
                    eventSummary.eventUid,
                    eventSummary.name,
                    null,
                    null,
                    eventSummary.xCoordinate,
                    eventSummary.yCoordinate,
                    eventSummary.description,
                    eventSummary.startTime,
                    eventSummary.endTime,
                    eventSummary.chatUid,
                    EventStatus.findById(eventSummary.status),
                    eventSummary.types.map { EventType.findById(it) },
                    listOf(),
                    listOf(),
                    listOf(),
                    null,
                    false,
                    null,
                    listOf(),
                    eventSummary.cityId?.toInt(),
                    eventSummary.cityName,
                    eventSummary.isOnline,
                    null
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

    fun isActive(): Boolean {
        return status != EventStatus.ENDED && status != EventStatus.CANCELLED
    }

    fun isCanReceiveReward(): Boolean {
        return status == EventStatus.ENDED && participants.size >= 3 && isOpenForInvitations
    }

    fun getAllParticipants(): List<Person> {
        val list = participants.toMutableList()
        list.addAll(invitedParticipants)
        list.addAll(notApprovedParticipants)
        return list
    }
}