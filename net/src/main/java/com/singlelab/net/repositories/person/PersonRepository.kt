package com.singlelab.net.repositories.person

import com.singlelab.net.model.person.FeedbackRequest
import com.singlelab.net.model.person.PersonResponse
import com.singlelab.net.model.person.ProfileRequest
import com.singlelab.net.model.person.ProfileResponse

interface PersonRepository {

    suspend fun getProfile(): ProfileResponse?

    suspend fun getProfile(personUid: String): ProfileResponse?

    suspend fun getFriends(personUid: String): List<PersonResponse>?

    suspend fun addToFriends(personUid: String)

    suspend fun removeFromFriends(personUid: String)

    suspend fun updateProfile(profileRequest: ProfileRequest): ProfileResponse?

    suspend fun removePushToken()

    suspend fun addFeedback(request: FeedbackRequest)
}