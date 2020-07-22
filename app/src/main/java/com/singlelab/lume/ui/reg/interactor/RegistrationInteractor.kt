package com.singlelab.lume.ui.reg.interactor

import com.singlelab.data.model.profile.Profile

interface RegistrationInteractor {
    suspend fun registration(profile: Profile)
}