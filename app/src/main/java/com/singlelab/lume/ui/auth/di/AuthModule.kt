package com.singlelab.lume.ui.auth.di

import com.singlelab.lume.LumeApplication
import com.singlelab.lume.database.repository.ProfileRepository
import com.singlelab.lume.ui.auth.AuthPresenter
import com.singlelab.lume.ui.auth.interactor.AuthInteractor
import com.singlelab.lume.ui.auth.interactor.AuthInteractorImpl
import com.singlelab.net.ApiUnit
import com.singlelab.net.repositories.auth.AuthRepository
import com.singlelab.net.repositories.auth.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object AuthModule {
    @Provides
    fun providePresenter(interactor: AuthInteractor): AuthPresenter {
        return AuthPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(
        repository: AuthRepository,
        profileRepository: ProfileRepository
    ): AuthInteractor {
        return AuthInteractorImpl(repository, profileRepository)
    }

    @Provides
    fun provideRepository(apiUnit: ApiUnit): AuthRepository {
        return AuthRepositoryImpl(apiUnit)
    }
}