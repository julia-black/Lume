package com.singlelab.data.model.event

import com.singlelab.data.model.profile.Person

class Event(
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
    val participants: List<Person> = listOf(),
    val administrator: Person? = null
)