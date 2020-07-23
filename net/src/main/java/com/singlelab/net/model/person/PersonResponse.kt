package com.singlelab.net.model.person

class PersonResponse(
    val personUid: String,
    val name: String,
    val imageContentUid: String?,
    val isFriend: Boolean = false
)