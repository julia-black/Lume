package com.singlelab.lume.ui.reg.interactor

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.profile.Profile

interface RegistrationInteractor {
    @Throws(ApiException::class)
    suspend fun registration(profile: Profile)
}