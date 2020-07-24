package com.singlelab.net.model.event

class EventRequest(
    val name: String,
    val minAge: Int?,
    val maxAge: Int?,
    val xCoordinate: Float,
    val yCoordinate: Float,
    val description: String?,
    val startTime: String,
    val endTime: String,
    val type: Int = 0,
    val eventImageContentUid: String? = null,
    val isOpenForInvitations: Boolean = true
)