package com.singlelab.net.model.event

class UpdateEventRequest(
    val eventUid: String,
    val status: Int? = null
)