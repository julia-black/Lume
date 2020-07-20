package com.singlelab.data.model.event

class Event(
    val name: String,
    val minAge: Int?,
    val maxAge: Int?,
    val xCoordinate: Float,
    val yCoordinate: Float,
    val description: String? = null,
    val startTime: String,
    val endTime: String,
    val status: Int = 0,
    val type: Int = 0
)