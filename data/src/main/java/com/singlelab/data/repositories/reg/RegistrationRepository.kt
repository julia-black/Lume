package com.singlelab.data.repositories.reg

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.profile.Profile

interface RegistrationRepository {
    @Throws(ApiException::class)
    suspend fun registration(profile: Profile)
}