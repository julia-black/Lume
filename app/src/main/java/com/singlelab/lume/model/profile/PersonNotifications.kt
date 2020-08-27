package com.singlelab.lume.model.profile

import com.singlelab.net.model.person.PersonNotificationsResponse

class PersonNotifications(
    val hasNewFriends: Boolean = false,
    val hasNewEvents: Boolean = false,
    val hasNewChatMessages: Boolean = false,
    val hasNewBadges: Boolean = false
) {
    companion object {
        fun fromResponse(response: PersonNotificationsResponse?): PersonNotifications {
            response ?: return PersonNotifications()
            return PersonNotifications(
                response.newFriendsCount > 0,
                response.newEventInvitationsCount > 0,
                response.anyNewChatMessages,
                response.anyNewBadges
            )
        }
    }
}