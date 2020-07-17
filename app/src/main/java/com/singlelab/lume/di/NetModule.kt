package com.singlelab.lume.di

import com.singlelab.data.net.ApiUnit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@InstallIn(ActivityComponent::class)
@Module
class NetModule {

    @Provides
    fun provideApiUnit(): ApiUnit {
        return ApiUnit()
    }
}