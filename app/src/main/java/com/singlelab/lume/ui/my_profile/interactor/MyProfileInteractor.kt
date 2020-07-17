package com.singlelab.lume.ui.my_profile.interactor

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.profile.Profile
import com.singlelab.data.model.profile.ResponseImageUid

interface MyProfileInteractor {
    @Throws(ApiException::class)
    suspend fun loadProfile(): Profile?

    @Throws(ApiException::class)
    suspend fun updateImageProfile(imageStr: String): ResponseImageUid?
}