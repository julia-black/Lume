package com.singlelab.lume.ui.reg.di

import com.singlelab.net.ApiUnit
import com.singlelab.net.repositories.reg.RegistrationRepository
import com.singlelab.net.repositories.reg.RegistrationRepositoryImpl
import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.reg.RegistrationPresenter
import com.singlelab.lume.ui.reg.interactor.RegistrationInteractor
import com.singlelab.lume.ui.reg.interactor.RegistrationInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object RegistrationModule {
    @Provides
    fun providePresenter(interactor: RegistrationInteractor): RegistrationPresenter {
        return RegistrationPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: RegistrationRepository): RegistrationInteractor {
        return RegistrationInteractorImpl(repository)
    }

    @Provides
    fun provideRepository(apiUnit: ApiUnit): RegistrationRepository {
        return RegistrationRepositoryImpl(apiUnit)
    }
}