package com.singlelab.lume.ui.auth.di

import com.singlelab.lume.ui.auth.AuthPresenter
import com.singlelab.lume.ui.auth.interactor.AuthInteractor
import com.singlelab.lume.ui.auth.interactor.AuthInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object AuthModule {
    @Provides
    fun providePresenter(interactor: AuthInteractor): AuthPresenter {
        return AuthPresenter(interactor)
    }

    @Provides
    fun provideInteractor(): AuthInteractor {
        return AuthInteractorImpl()
    }
}