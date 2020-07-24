package com.singlelab.lume.ui.search_event.di

import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.search_event.SearchEventPresenter
import com.singlelab.lume.ui.search_event.interactor.SearchEventInteractor
import com.singlelab.lume.ui.search_event.interactor.SearchEventInteractorImpl
import com.singlelab.lume.ui.swiper_event.SwiperEventPresenter
import com.singlelab.lume.ui.swiper_event.interactor.SwiperEventInteractor
import com.singlelab.lume.ui.swiper_event.interactor.SwiperEventInteractorImpl
import com.singlelab.net.repositories.events.EventsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@InstallIn(ActivityComponent::class)
@Module
object SearchEventModule {
    @Provides
    fun providePresenter(interactor: SearchEventInteractor): SearchEventPresenter {
        return SearchEventPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: EventsRepository): SearchEventInteractor {
        return SearchEventInteractorImpl(repository)
    }
}