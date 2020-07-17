package com.singlelab.data.repositories.reg

import com.singlelab.data.model.profile.Profile
import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.BaseRepository

class RegistrationRepositoryImpl(private val apiUnit: ApiUnit) : RegistrationRepository,
    BaseRepository() {
    override suspend fun registration(profile: Profile) {
        safeApiCall(
            apiUnit,
            call = { apiUnit.profileApi.updateProfileAsync(profile).await() },
            errorMessage = "Не удалось зарегистрироваться"
        )
    }
}