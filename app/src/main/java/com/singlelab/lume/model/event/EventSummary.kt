package com.singlelab.lume.model.event

import com.singlelab.net.model.event.EventSummaryResponse
import com.singlelab.net.model.event.ParticipantStatus

class EventSummary(
    val eventUid: String,
    val name: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val type: String,
    val eventImageContentUid: String?,
    val xCoordinate: Float,
    val yCoordinate: Float,
    val status: Int,
    val isAdministrator: Boolean = false,
    val participantStatus: ParticipantStatus,
    val anyPersonWaitingForApprove: Boolean
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
                    eventSummaryResponse.type,
                    eventSummaryResponse.eventImageContentUid,
                    eventSummaryResponse.xCoordinate,
                    eventSummaryResponse.yCoordinate,
                    eventSummaryResponse.status,
                    eventSummaryResponse.isAdministrator,
                    ParticipantStatus.findStatus(eventSummaryResponse.participantStatus)!!,
                    eventSummaryResponse.anyPersonWaitingForApprove
                )
            } else {
                null
            }
        }
    }
}