package com.singlelab.data.repositories.my_profile

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.profile.Profile
import com.singlelab.data.model.profile.ResponseImageUid

interface MyProfileRepository {

    @Throws(ApiException::class)
    suspend fun getProfile(): Profile?

    @Throws(ApiException::class)
    suspend fun updateImageProfile(imageStr: String): ResponseImageUid?
}