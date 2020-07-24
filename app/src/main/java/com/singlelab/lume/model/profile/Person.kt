package com.singlelab.lume.model.profile

import com.singlelab.net.model.person.PersonResponse

class Person(
    val personUid: String,
    val name: String,
    val imageContentUid: String?,
    var isFriend: Boolean = false,
    var isInvited: Boolean = false
) {
    companion object {
        fun fromResponse(personResponse: PersonResponse?): Person? {
            return if (personResponse != null) {
                Person(
                    personResponse.personUid,
                    personResponse.name,
                    personResponse.imageContentUid,
                    personResponse.isFriend
                )
            } else {
                null
            }
        }
    }
}