package com.singlelab.lume.model.event

import com.singlelab.net.model.event.EventSummaryResponse
import com.singlelab.net.model.event.ParticipantStatus

class EventSummary(
    val eventUid: String,
    val name: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val types: List<EventType>,
    val eventPrimaryImageContentUid: String?,
    val xCoordinate: Double?,
    val yCoordinate: Double?,
    val status: EventStatus,
    val isAdministrator: Boolean = false,
    val participantStatus: ParticipantStatus,
    val anyPersonWaitingForApprove: Boolean,
    val isOnline: Boolean,
    val chatUid: String? = null,
    val cityId: String? = null,
    val cityName: String? = null
) {
    companion object {
        fun fromResponse(eventSummaryResponse: EventSummaryResponse?): EventSummary? {
            return if (eventSummaryResponse != null) {
                EventSummary(
                    eventSummaryResponse.eventUid,
                    eventSummaryResponse.name,
                    eventSummaryResponse.description,
                    eventSummaryResponse.startTime,
                    eventSummaryResponse.endTime,
                    eventSummaryResponse.types.map { EventType.findById(it) },
                    eventSummaryResponse.eventPrimaryImageContentUid,
                    eventSummaryResponse.xCoordinate,
                    eventSummaryResponse.yCoordinate,
                    EventStatus.findById(eventSummaryResponse.status),
                    eventSummaryResponse.isAdministrator,
                    ParticipantStatus.findStatus(eventSummaryResponse.participantStatus)!!,
                    eventSummaryResponse.anyPersonWaitingForApprove,
                    eventSummaryResponse.isOnline,
                    eventSummaryResponse.chatUid,
                    eventSummaryResponse.cityId,
                    eventSummaryResponse.cityName
                )
            } else {
                null
            }
        }

        fun fromEntity(eventSummaryEntity: com.singlelab.lume.database.entity.EventSummary?): EventSummary? {
            return if (eventSummaryEntity != null) {
                EventSummary(
                    eventSummaryEntity.eventUid,
                    eventSummaryEntity.name,
                    eventSummaryEntity.description,
                    eventSummaryEntity.startTime,
                    eventSummaryEntity.endTime,
                    eventSummaryEntity.types.map { EventType.findById(it) },
                    eventSummaryEntity.eventPrimaryImageContentUid,
                    eventSummaryEntity.xCoordinate,
                    eventSummaryEntity.yCoordinate,
                    EventStatus.findById(eventSummaryEntity.status),
                    eventSummaryEntity.isAdministrator,
                    ParticipantStatus.findStatus(eventSummaryEntity.participantStatus)!!,
                    eventSummaryEntity.anyPersonWaitingForApprove,
                    eventSummaryEntity.isOnline,
                    eventSummaryEntity.chatUid,
                    eventSummaryEntity.cityId,
                    eventSummaryEntity.cityName
                )
            } else {
                null
            }
        }
    }

    fun toEntity(): com.singlelab.lume.database.entity.EventSummary {
        return com.singlelab.lume.database.entity.EventSummary(
            eventUid,
            name,
            description,
            startTime,
            endTime,
            types.map { it.id },
            eventPrimaryImageContentUid ?: "",
            xCoordinate,
            yCoordinate,
            status.id,
            isAdministrator,
            participantStatus.id,
            anyPersonWaitingForApprove,
            isOnline,
            chatUid,
            cityId,
            cityName
        )
    }

    fun isActive(): Boolean {
        return status != EventStatus.CANCELLED && status != EventStatus.ENDED
    }
}