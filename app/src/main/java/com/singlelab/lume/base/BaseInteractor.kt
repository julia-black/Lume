package com.singlelab.lume.base

import com.singlelab.lume.database.repository.ProfileRepository
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.OnRefreshTokenListener

open class BaseInteractor(
    private val baseRepository: BaseRepository,
    private val profileRepository: ProfileRepository? = null
) {

    fun setOnRefreshTokenListener(listener: OnRefreshTokenListener?) {
        baseRepository.setOnRefreshTokenListener(listener)
    }

    suspend fun updateProfile(accessToken: String, refreshToken: String) {
        val profile = profileRepository?.getProfile() ?: return
        profile.updateAuth(accessToken, refreshToken)
        profileRepository.update(profile)
    }

    suspend fun clearProfile() {
        profileRepository?.clear()
    }
}