package com.singlelab.lume.ui.my_profile.interactor

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.profile.Profile

interface MyProfileInteractor {
    @Throws(ApiException::class)
    suspend fun loadProfile(): Profile?
}