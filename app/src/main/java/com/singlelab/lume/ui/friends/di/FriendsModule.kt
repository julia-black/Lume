package com.singlelab.lume.ui.friends.di

import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.friends.FriendsRepository
import com.singlelab.data.repositories.friends.FriendsRepositoryImpl
import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.friends.FriendsPresenter
import com.singlelab.lume.ui.friends.interactor.FriendsInteractor
import com.singlelab.lume.ui.friends.interactor.FriendsInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object FriendsModule {

    @Provides
    fun providePresenter(
        interactor: FriendsInteractor
    ): FriendsPresenter {
        return FriendsPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: FriendsRepository): FriendsInteractor {
        return FriendsInteractorImpl(repository)
    }

    @Provides
    fun providesRepository(apiUnit: ApiUnit): FriendsRepository {
        return FriendsRepositoryImpl(apiUnit)
    }
}