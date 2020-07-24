package com.singlelab.lume.ui.participants.di

import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.participants.ParticipantsPresenter
import com.singlelab.lume.ui.participants.interactor.ParticipantsInteractor
import com.singlelab.lume.ui.participants.interactor.ParticipantsInteractorImpl
import com.singlelab.net.repositories.events.EventsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object ParticipantsModule {

    @Provides
    fun providePresenter(
        interactor: ParticipantsInteractor
    ): ParticipantsPresenter {
        return ParticipantsPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: EventsRepository): ParticipantsInteractor {
        return ParticipantsInteractorImpl(repository)
    }

}