package com.singlelab.net.repositories.person

import com.singlelab.net.model.ImageUidResponse
import com.singlelab.net.model.person.PersonResponse
import com.singlelab.net.model.person.ProfileRequest
import com.singlelab.net.model.person.ProfileResponse

interface PersonRepository {

    suspend fun getProfile(): ProfileResponse?

    suspend fun getProfile(personUid: String): ProfileResponse?

    suspend fun getFriends(personUid: String): List<PersonResponse>?

    suspend fun updateImageProfile(imageStr: String): ImageUidResponse?

    suspend fun addToFriends(personUid: String)

    suspend fun removeFromFriends(personUid: String)

    suspend fun updateProfile(profileRequest: ProfileRequest)
}