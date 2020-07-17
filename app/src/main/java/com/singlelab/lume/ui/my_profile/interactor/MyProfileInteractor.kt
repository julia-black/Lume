package com.singlelab.lume.ui.my_profile.interactor

import com.singlelab.data.model.profile.Profile

interface MyProfileInteractor {
    suspend fun loadProfile() : Profile?
}