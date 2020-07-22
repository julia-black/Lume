package com.singlelab.data.repositories.person

import com.singlelab.data.model.ResponseMessage
import com.singlelab.data.model.profile.Profile
import com.singlelab.data.model.profile.ResponseImageUid

interface PersonRepository {

    suspend fun getProfile(): Profile?

    suspend fun getProfile(personUid: String): Profile?

    suspend fun updateImageProfile(imageStr: String): ResponseImageUid?

    suspend fun addToFriends(personUid: String): ResponseMessage?

    suspend fun removeFromFriends(personUid: String): ResponseMessage?
}