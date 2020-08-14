package com.singlelab.net.repositories.reg

import com.singlelab.net.ApiUnit
import com.singlelab.net.model.person.ProfileRequest
import com.singlelab.net.repositories.BaseRepository

class RegistrationRepositoryImpl(private val apiUnit: ApiUnit) : RegistrationRepository,
    BaseRepository(apiUnit) {
    override suspend fun registration(profile: ProfileRequest) {
        safeApiCall(
            call = { apiUnit.personApi.updateProfileAsync(profile).await() },
            errorMessage = "Не удалось зарегистрироваться"
        )
    }
}