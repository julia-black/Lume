package com.singlelab.lume.ui.reg.interactor

import com.singlelab.net.model.person.ProfileRequest

interface RegistrationInteractor {
    suspend fun registration(profile: ProfileRequest)

    suspend fun addPhoto(image: String)
}