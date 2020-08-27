package com.singlelab.net.model.event

class RandomEventRequest(
    val personXCoordinate: Double? = null,
    val personYCoordinate: Double? = null,
    val distance: Double? = null,
    val cityId: Int? = null,
    val eventTypes: List<Int>? = null,
    val isOnline: Boolean? = null,
    val minimalStartTime: String? = null,
    val maximalEndTime: String? = null
)