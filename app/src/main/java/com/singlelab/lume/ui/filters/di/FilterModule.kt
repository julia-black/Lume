package com.singlelab.lume.ui.filters.di

import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.filters.FilterPresenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@InstallIn(ActivityComponent::class)
@Module
object FilterModule {
    @Provides
    fun providePresenter(): FilterPresenter {
        return FilterPresenter(LumeApplication.preferences)
    }
}