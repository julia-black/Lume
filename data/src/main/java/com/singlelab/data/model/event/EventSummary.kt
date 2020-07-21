package com.singlelab.data.model.event

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
    val isAdministrator: Boolean = false
)