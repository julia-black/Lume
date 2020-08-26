package com.singlelab.net.model.person

class BadgeResponse(
    val badgeImageUid: String,
    val name: String,
    val description: String,
    val received: Boolean = false
)