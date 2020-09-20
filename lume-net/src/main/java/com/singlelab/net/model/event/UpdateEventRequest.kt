package com.singlelab.net.model.event

class UpdateEventRequest(
    val eventUid: String,
    val status: Int? = null,
    val primaryImage: String? = null,
    val extraImages: List<String>? = null,
    val description: String? = null,
    val xCoordinate: Double? = null,
    val yCoordinate: Double? = null
)