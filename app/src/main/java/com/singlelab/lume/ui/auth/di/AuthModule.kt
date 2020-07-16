package com.singlelab.lume.ui.auth.di

import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.auth.AuthRepository
import com.singlelab.data.repositories.auth.AuthRepositoryImpl
import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.auth.AuthPresenter
import com.singlelab.lume.ui.auth.interactor.AuthInteractor
import com.singlelab.lume.ui.auth.interactor.AuthInteractorImpl
import com.singlelab.lume.ui.auth.router.AuthRouter
import com.singlelab.lume.ui.auth.router.AuthRouterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object AuthModule {
    @Provides
    fun providePresenter(interactor: AuthInteractor, router: AuthRouter): AuthPresenter {
        return AuthPresenter(interactor, router, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: AuthRepository): AuthInteractor {
        return AuthInteractorImpl(repository)
    }

    @Provides
    fun provideRepository(apiUnit: ApiUnit): AuthRepository {
        return AuthRepositoryImpl(apiUnit)
    }

    @Provides
    fun provideRouter(): AuthRouter {
        return AuthRouterImpl()
    }
}