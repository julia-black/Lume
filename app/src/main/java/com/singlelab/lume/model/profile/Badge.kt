package com.singlelab.lume.model.profile

import com.singlelab.net.model.person.BadgeResponse

class Badge(
    val badgeImageUid: String,
    val name: String,
    val description: String,
    val isReceived: Boolean = false
) {
    companion object {
        fun fromResponse(response: BadgeResponse?): Badge? {
            return if (response == null) {
                null
            } else {
                Badge(
                    response.badgeImageUid,
                    response.name,
                    response.description,
                    response.received
                )
            }
        }
    }
}