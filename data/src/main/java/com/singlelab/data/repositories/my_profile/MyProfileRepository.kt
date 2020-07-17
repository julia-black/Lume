package com.singlelab.data.repositories.my_profile

import com.singlelab.data.model.profile.Profile

interface MyProfileRepository {
    suspend fun getProfile() : Profile?
}