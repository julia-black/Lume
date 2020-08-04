package com.singlelab.lume.model.profile

import com.singlelab.net.model.person.ProfileResponse

class Profile(
    val personUid: String,
    val name: String,
    val description: String? = null,
    val cityId: Int,
    val cityName: String,
    val age: Int,
    val imageContentUid: String? = null,
    val isFriend: Boolean = false,
    val friends: List<Person> = arrayListOf()
) {
    companion object {
        fun fromResponse(profileResponse: ProfileResponse?): Profile? {
            return if (profileResponse != null) {
                Profile(
                    profileResponse.personUid,
                    profileResponse.name,
                    profileResponse.description,
                    profileResponse.cityId,
                    profileResponse.cityName,
                    profileResponse.age,
                    profileResponse.imageContentUid,
                    profileResponse.isFriend,
                    profileResponse.friends.mapNotNull {
                        Person.fromResponse(it)
                    }
                )
            } else {
                null
            }
        }
    }
}