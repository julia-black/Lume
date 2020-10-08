package com.singlelab.lume.model.profile

import android.os.Parcelable
import com.singlelab.net.model.event.ParticipantStatus
import com.singlelab.net.model.person.PersonResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
class Person(
    val personUid: String,
    val name: String,
    val login: String,
    val description: String?,
    val age: Int,
    val cityName: String?,
    val imageContentUid: String?,
    var isFriend: Boolean = false,
    var isInvited: Boolean = false,
    var friendshipApprovalRequired: Boolean = false,
    var participantStatus: ParticipantStatus? = null
) : Parcelable {
    companion object {
        fun fromResponse(personResponse: PersonResponse?): Person? {
            return if (personResponse?.personUid != null &&
                personResponse.name != null &&
                personResponse.login != null &&
                personResponse.age != null
            ) {
                Person(
                    personResponse.personUid!!,
                    personResponse.name!!,
                    personResponse.login!!,
                    personResponse.description,
                    personResponse.age!!,
                    personResponse.cityName,
                    personResponse.imageContentUid,
                    personResponse.isFriend,
                    false,
                    personResponse.friendshipApprovalRequired,
                    ParticipantStatus.findStatus(personResponse.participantStatus)
                )
            } else {
                null
            }
        }
    }
}