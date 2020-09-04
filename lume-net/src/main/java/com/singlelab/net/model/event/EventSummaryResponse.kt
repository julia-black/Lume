package com.singlelab.net.model.event

class EventSummaryResponse(
    val eventUid: String,
    val name: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val types: List<Int>,
    val eventPrimaryImageContentUid: String?,
    val xCoordinate: Double?,
    val yCoordinate: Double?,
    val status: Int,
    val isAdministrator: Boolean = false,
    val participantStatus: Int = 0,
    val anyPersonWaitingForApprove: Boolean,
    val isOnline: Boolean,
    val chatUid: String
)