package com.singlelab.lume.ui.event.di

import com.singlelab.data.repositories.events.EventsRepository
import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.event.EventPresenter
import com.singlelab.lume.ui.event.interactor.EventInteractor
import com.singlelab.lume.ui.event.interactor.EventInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@InstallIn(ActivityComponent::class)
@Module
object EventModule {
    @Provides
    fun providePresenter(interactor: EventInteractor): EventPresenter {
        return EventPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: EventsRepository): EventInteractor {
        return EventInteractorImpl(repository)
    }
}