package com.singlelab.lume.ui.my_profile.interactor

import com.singlelab.lume.model.profile.Profile

interface MyProfileInteractor {
    suspend fun loadProfile(): Profile?

    suspend fun updateImageProfile(imageStr: String): String?
}