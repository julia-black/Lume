package com.singlelab.lume.ui.auth.di

import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.auth.AuthRepository
import com.singlelab.data.repositories.auth.AuthRepositoryImpl
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
    fun provideInteractor(repository: AuthRepository): AuthInteractor {
        return AuthInteractorImpl(repository)
    }

    @Provides
    fun provideRepository(): AuthRepository {
        return AuthRepositoryImpl(ApiUnit.authApi)
    }
}