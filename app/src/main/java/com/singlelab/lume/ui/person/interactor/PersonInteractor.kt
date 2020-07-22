package com.singlelab.lume.ui.person.interactor

import com.singlelab.data.model.ResponseMessage
import com.singlelab.data.model.profile.Profile

interface PersonInteractor {
    suspend fun loadPerson(personUid: String): Profile?

    suspend fun addToFriends(personUid: String): ResponseMessage?

    suspend fun removeFromFriends(personUid: String): ResponseMessage?
}