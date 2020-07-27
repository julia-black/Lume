package com.singlelab.lume.ui.swiper_person.di

import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.swiper_person.SwiperPersonPresenter
import com.singlelab.lume.ui.swiper_person.interactor.SwiperPersonInteractor
import com.singlelab.lume.ui.swiper_person.interactor.SwiperPersonInteractorImpl
import com.singlelab.net.repositories.events.EventsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@InstallIn(ActivityComponent::class)
@Module
object SwiperPersonModule {
    @Provides
    fun providePresenter(interactor: SwiperPersonInteractor): SwiperPersonPresenter {
        return SwiperPersonPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: EventsRepository): SwiperPersonInteractor {
        return SwiperPersonInteractorImpl(repository)
    }
}