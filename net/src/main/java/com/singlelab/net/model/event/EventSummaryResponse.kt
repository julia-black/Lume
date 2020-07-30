package com.singlelab.net.model.event

class EventSummaryResponse(
    val eventUid: String,
    val name: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val type: String,
    val eventPrimaryImageContentUid: String?,
    val xCoordinate: Float,
    val yCoordinate: Float,
    val status: Int,
    val isAdministrator: Boolean = false,
    val participantStatus: Int = 0,
    val anyPersonWaitingForApprove: Boolean,
    val isOnline: Boolean
)