package com.singlelab.lume.ui.cities.di

import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.cities.CitiesPresenter
import com.singlelab.lume.ui.cities.interactor.CitiesInteractor
import com.singlelab.lume.ui.cities.interactor.CitiesInteractorImpl
import com.singlelab.net.ApiUnit
import com.singlelab.net.repositories.cities.CitiesRepository
import com.singlelab.net.repositories.cities.CitiesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@InstallIn(ActivityComponent::class)
@Module
object CitiesModule {
    @Provides
    fun providePresenter(interactor: CitiesInteractor): CitiesPresenter {
        return CitiesPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: CitiesRepository): CitiesInteractor {
        return CitiesInteractorImpl(repository)
    }

    @Provides
    fun provideRepository(apiUnit: ApiUnit): CitiesRepository {
        return CitiesRepositoryImpl(apiUnit)
    }
}