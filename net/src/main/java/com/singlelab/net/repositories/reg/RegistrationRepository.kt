package com.singlelab.net.repositories.reg

import com.singlelab.net.model.person.ProfileRequest

interface RegistrationRepository {
    suspend fun registration(profile: ProfileRequest)

    suspend fun addPhoto(imageStr: String)
}