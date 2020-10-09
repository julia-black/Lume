package com.singlelab.lume.ui.receive_reward.di

import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.receive_reward.ReceiveRewardPresenter
import com.singlelab.lume.ui.receive_reward.interactor.ReceiveRewardInteractor
import com.singlelab.lume.ui.receive_reward.interactor.ReceiveRewardInteractorImpl
import com.singlelab.net.repositories.person.PersonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object ReceiveRewardModule {
    @Provides
    fun providePresenter(interactor: ReceiveRewardInteractor): ReceiveRewardPresenter {
        return ReceiveRewardPresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: PersonRepository): ReceiveRewardInteractor {
        return ReceiveRewardInteractorImpl(repository)
    }
}