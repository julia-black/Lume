package com.singlelab.lume.ui.map.di

import com.singlelab.lume.ui.map.MapPresenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@InstallIn(ActivityComponent::class)
@Module
object MapModule {
    @Provides
    fun providePresenter(): MapPresenter {
        return MapPresenter()
    }
}