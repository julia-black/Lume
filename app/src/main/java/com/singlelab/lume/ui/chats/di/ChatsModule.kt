package com.singlelab.lume.ui.chats.di

import com.singlelab.net.ApiUnit
import com.singlelab.net.repositories.chats.ChatsRepository
import com.singlelab.net.repositories.chats.DefaultChatsRepository
import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.chats.ChatsPresenter
import com.singlelab.lume.ui.chats.interactor.ChatsInteractor
import com.singlelab.lume.ui.chats.interactor.DefaultChatsInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object ChatsModule {
    @Provides
    fun providePresenter(interactor: ChatsInteractor): ChatsPresenter {
        return ChatsPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: ChatsRepository): ChatsInteractor {
        return DefaultChatsInteractor(repository)
    }

    @Provides
    fun provideRepository(apiUnit: ApiUnit): ChatsRepository {
        return DefaultChatsRepository(apiUnit)
    }
}