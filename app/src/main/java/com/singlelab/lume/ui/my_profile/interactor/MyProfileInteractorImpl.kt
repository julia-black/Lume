package com.singlelab.lume.ui.my_profile.interactor

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.repositories.BaseRepository
import com.singlelab.data.repositories.my_profile.MyProfileRepository
import com.singlelab.lume.base.BaseInteractor

class MyProfileInteractorImpl(private val repository: MyProfileRepository) : MyProfileInteractor,
    BaseInteractor(repository as BaseRepository) {

    @Throws(ApiException::class)
    override suspend fun loadProfile() = repository.getProfile()
}