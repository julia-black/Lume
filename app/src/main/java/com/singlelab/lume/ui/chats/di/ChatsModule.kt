package com.singlelab.lume.ui.chats.di

import com.singlelab.lume.LumeApplication
import com.singlelab.lume.database.LumeDatabase
import com.singlelab.lume.database.repository.RoomChatsRepository
import com.singlelab.lume.ui.chats.ChatsPresenter
import com.singlelab.lume.ui.chats.interactor.ChatsInteractor
import com.singlelab.lume.ui.chats.interactor.DefaultChatsInteractor
import com.singlelab.net.ApiUnit
import com.singlelab.net.repositories.chats.DefaultChatsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import com.singlelab.lume.database.repository.ChatsRepository as LocalChatsRepository
import com.singlelab.net.repositories.chats.ChatsRepository as RemoteChatsRepository

@InstallIn(ActivityComponent::class)
@Module
object ChatsModule {
    @Provides
    fun providePresenter(
        interactor: ChatsInteractor
    ): ChatsPresenter = ChatsPresenter(
        interactor = interactor,
        preferences = LumeApplication.preferences
    )

    @Provides
    fun provideInteractor(
        remoteRepository: RemoteChatsRepository,
        localRepository: LocalChatsRepository
    ): ChatsInteractor = DefaultChatsInteractor(
        remoteRepository = remoteRepository,
        localRepository = localRepository
    )

    @Provides
    fun provideRemoteRepository(
        apiUnit: ApiUnit
    ): RemoteChatsRepository = DefaultChatsRepository(
        apiUnit = apiUnit
    )

    @Provides
    fun provideLocalRepository(
        database: LumeDatabase
    ): LocalChatsRepository = RoomChatsRepository(
        db = database
    )
}