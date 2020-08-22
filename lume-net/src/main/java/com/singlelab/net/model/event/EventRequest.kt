package com.singlelab.net.model.event

class EventRequest(
    val name: String,
    val minAge: Int?,
    val maxAge: Int?,
    val xCoordinate: Double?,
    val yCoordinate: Double?,
    val description: String?,
    val startTime: String,
    val endTime: String,
    val types: Array<Int> = arrayOf(0),
    val isOpenForInvitations: Boolean = true,
    val primaryImage: String? = null,
    val images: List<String>? = null,
    val cityId: Int? = null,
    val isOnline: Boolean? = null
)