package com.singlelab.lume.ui.my_profile.interactor

import com.singlelab.data.model.profile.Profile
import com.singlelab.data.model.profile.ResponseImageUid

interface MyProfileInteractor {
    suspend fun loadProfile(): Profile?

    suspend fun updateImageProfile(imageStr: String): ResponseImageUid?
}