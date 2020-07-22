package com.singlelab.lume.ui.chat.di

import com.singlelab.net.repositories.chat.ChatRepository
import com.singlelab.net.repositories.chat.DefaultChatRepository
import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.chat.ChatPresenter
import com.singlelab.lume.ui.chat.interactor.ChatInteractor
import com.singlelab.lume.ui.chat.interactor.DefaultChatInteractor
import com.singlelab.net.ApiUnit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object ChatModule {
    @Provides
    fun providePresenter(interactor: ChatInteractor): ChatPresenter {
        return ChatPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: ChatRepository): ChatInteractor {
        return DefaultChatInteractor(repository)
    }

    @Provides
    fun provideRepository(apiUnit: ApiUnit): ChatRepository {
        return DefaultChatRepository(/*apiUnit*/)
    }
}