package com.singlelab.lume.ui.friends.interactor

import com.singlelab.data.model.profile.Person

interface FriendsInteractor {
    suspend fun getFriends() : List<Person>?
}