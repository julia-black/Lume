package com.singlelab.lume.ui.friends.interactor

import com.singlelab.data.model.profile.Person
import com.singlelab.data.repositories.BaseRepository
import com.singlelab.data.repositories.friends.FriendsRepository
import com.singlelab.lume.base.BaseInteractor

class FriendsInteractorImpl(private val repository: FriendsRepository) : FriendsInteractor,
    BaseInteractor(repository as BaseRepository) {
    override suspend fun getFriends(): List<Person>? {
        return repository.getFriends()
    }

}