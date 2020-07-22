package com.singlelab.lume.ui.person.di

import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.person.PersonRepository
import com.singlelab.data.repositories.person.PersonRepositoryImpl
import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.person.PersonPresenter
import com.singlelab.lume.ui.person.interactor.PersonInteractor
import com.singlelab.lume.ui.person.interactor.PersonInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object PersonModule {

    @Provides
    fun providePresenter(
        interactor: PersonInteractor
    ): PersonPresenter {
        return PersonPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: PersonRepository): PersonInteractor {
        return PersonInteractorImpl(repository)
    }

    @Provides
    fun providesRepository(apiUnit: ApiUnit): PersonRepository {
        return PersonRepositoryImpl(apiUnit)
    }
}