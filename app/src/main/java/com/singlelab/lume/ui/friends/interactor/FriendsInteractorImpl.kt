package com.singlelab.lume.ui.friends.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.model.profile.Person
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.friends.FriendsRepository

class FriendsInteractorImpl(private val repository: FriendsRepository) : FriendsInteractor,
    BaseInteractor(repository as BaseRepository) {
    override suspend fun getFriends(): List<Person>? {
        return repository.getFriends()?.mapNotNull {
            Person.fromResponse(it)
        }
    }
}