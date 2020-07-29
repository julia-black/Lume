package com.singlelab.lume.ui.creating_event.di

import com.singlelab.lume.LumeApplication
import com.singlelab.lume.database.repository.ProfileRepository
import com.singlelab.lume.ui.creating_event.CreatingEventPresenter
import com.singlelab.lume.ui.creating_event.interactor.CreatingEventInteractor
import com.singlelab.lume.ui.creating_event.interactor.CreatingEventInteractorImpl
import com.singlelab.net.repositories.events.EventsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@InstallIn(ActivityComponent::class)
@Module
object CreatingEventModule {
    @Provides
    fun providePresenter(interactor: CreatingEventInteractor): CreatingEventPresenter {
        return CreatingEventPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(
        eventsRepository: EventsRepository,
        profileRepository: ProfileRepository
    ): CreatingEventInteractor {
        return CreatingEventInteractorImpl(eventsRepository, profileRepository)
    }
}