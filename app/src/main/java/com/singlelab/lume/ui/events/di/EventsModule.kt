package com.singlelab.lume.ui.events.di

import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.events.EventsRepository
import com.singlelab.data.repositories.events.EventsRepositoryImpl
import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.events.EventsPresenter
import com.singlelab.lume.ui.events.interactor.EventInteractorImpl
import com.singlelab.lume.ui.events.interactor.EventsInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@InstallIn(ActivityComponent::class)
@Module
object EventsModule {
    @Provides
    fun providePresenter(interactor: EventsInteractor): EventsPresenter {
        return EventsPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: EventsRepository): EventsInteractor {
        return EventInteractorImpl(repository)
    }

    @Provides
    fun provideRepository(apiUnit: ApiUnit): EventsRepository {
        return EventsRepositoryImpl(apiUnit)
    }
}