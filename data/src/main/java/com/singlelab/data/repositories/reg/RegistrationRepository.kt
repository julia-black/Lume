package com.singlelab.data.repositories.reg

import com.singlelab.data.model.profile.Profile

interface RegistrationRepository {
    suspend fun registration(profile: Profile)
}