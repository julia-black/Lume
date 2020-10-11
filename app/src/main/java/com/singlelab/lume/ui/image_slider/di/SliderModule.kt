package com.singlelab.lume.ui.image_slider.di

import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.image_slider.SliderPresenter
import com.singlelab.lume.ui.image_slider.interactor.SliderInteractor
import com.singlelab.lume.ui.image_slider.interactor.SliderInteractorImpl
import com.singlelab.net.repositories.events.EventsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object SliderModule {
    @Provides
    fun providePresenter(interactor: SliderInteractor): SliderPresenter {
        return SliderPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: EventsRepository): SliderInteractor {
        return SliderInteractorImpl(repository)
    }
}