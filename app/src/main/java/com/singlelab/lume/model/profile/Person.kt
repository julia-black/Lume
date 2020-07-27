package com.singlelab.lume.model.profile

import android.os.Parcelable
import com.singlelab.net.model.event.ParticipantStatus
import com.singlelab.net.model.person.PersonResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
class Person(
    val personUid: String,
    val name: String,
    val description: String?,
    val age: Int,
    val imageContentUid: String?,
    var isFriend: Boolean = false,
    var isInvited: Boolean = false,
    var participantStatus: ParticipantStatus? = null
) : Parcelable {
    companion object {
        fun fromResponse(personResponse: PersonResponse?): Person? {
            return if (personResponse != null) {
                Person(
                    personResponse.personUid,
                    personResponse.name,
                    personResponse.description,
                    personResponse.age,
                    personResponse.imageContentUid,
                    personResponse.isFriend,
                    false,
                    ParticipantStatus.findStatus(personResponse.participantStatus)
                )
            } else {
                null
            }
        }
    }
}