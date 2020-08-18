package com.singlelab.lume.ui.feedback.di

import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.feedback.FeedbackPresenter
import com.singlelab.lume.ui.feedback.interactor.FeedbackInteractor
import com.singlelab.lume.ui.feedback.interactor.FeedbackInteractorImpl
import com.singlelab.net.repositories.person.PersonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object FeedbackModule {
    @Provides
    fun providePresenter(interactor: FeedbackInteractor): FeedbackPresenter {
        return FeedbackPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: PersonRepository): FeedbackInteractor {
        return FeedbackInteractorImpl(repository)
    }
}