package com.singlelab.lume.ui.my_profile.interactor

import com.singlelab.lume.model.profile.Badge
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.model.profile.Profile

interface MyProfileInteractor {
    suspend fun loadProfile(): Profile?

    suspend fun loadFriends(personUid: String): List<Person>?

    suspend fun updateImageProfile(imageStr: String): String?

    suspend fun updatePushToken(token: String?)

    suspend fun clearDatabase()

    suspend fun removePushToken()

    suspend fun loadBadges(personUid: String): List<Badge>?
}