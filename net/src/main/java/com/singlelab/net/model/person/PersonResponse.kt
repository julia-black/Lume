package com.singlelab.net.model.person

class PersonResponse(
    val personUid: String,
    val name: String,
    val login: String,
    val description: String?,
    val age: Int,
    val cityName: String? = null,
    val imageContentUid: String?,
    val isFriend: Boolean = false,
    val friendshipApprovalRequired: Boolean = false,
    val participantStatus: Int? = null
)