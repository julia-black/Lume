package com.singlelab.net.model.event

import com.singlelab.net.model.person.PersonResponse

class EventResponse(
    val eventUid: String? = null,
    val name: String,
    val minAge: Int?,
    val maxAge: Int?,
    val xCoordinate: Float,
    val yCoordinate: Float,
    val description: String?,
    val startTime: String,
    val endTime: String,
    val status: Int = 0,
    val type: Int = 0,
    val eventImageContentUid: String? = null,
    val participants: List<PersonResponse> = listOf(),
    val administrator: PersonResponse? = null
)