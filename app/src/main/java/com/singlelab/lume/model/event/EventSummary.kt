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
    val xCoordinate: Float,
    val yCoordinate: Float,
    val status: EventStatus,
    val isAdministrator: Boolean = false,
    val participantStatus: ParticipantStatus,
    val anyPersonWaitingForApprove: Boolean,
    val isOnline: Boolean,
    val chatUid: String? = null
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
                    eventSummaryResponse.isOnline
                )
            } else {
                null
            }
        }
    }
}