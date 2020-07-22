package com.singlelab.net.repositories.person

import com.singlelab.net.model.ImageUidResponse
import com.singlelab.net.model.MessageResponse
import com.singlelab.net.model.person.ProfileResponse

interface PersonRepository {

    suspend fun getProfile(): ProfileResponse?

    suspend fun getProfile(personUid: String): ProfileResponse?

    suspend fun updateImageProfile(imageStr: String): ImageUidResponse?

    suspend fun addToFriends(personUid: String)

    suspend fun removeFromFriends(personUid: String)
}