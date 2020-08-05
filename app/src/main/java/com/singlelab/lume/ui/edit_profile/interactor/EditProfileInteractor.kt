package com.singlelab.lume.ui.edit_profile.interactor

import com.singlelab.lume.model.profile.NewProfile

interface EditProfileInteractor {
    suspend fun updateProfile(newProfile: NewProfile)
}