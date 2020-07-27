package com.singlelab.net.model.event

class SearchEventRequest(
    val query: String,
    val minAge: Int? = null,
    val maxAge: Int? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val type: Int? = null,
    val status: Int? = null,
    val isOpenForInvitations: Boolean? = null
)