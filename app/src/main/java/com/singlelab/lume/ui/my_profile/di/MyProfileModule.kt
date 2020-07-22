package com.singlelab.lume.ui.my_profile.di

import com.singlelab.data.repositories.person.PersonRepository
import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.my_profile.MyProfilePresenter
import com.singlelab.lume.ui.my_profile.interactor.MyProfileInteractor
import com.singlelab.lume.ui.my_profile.interactor.MyProfileInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object MyProfileModule {

    @Provides
    fun providePresenter(
        interactor: MyProfileInteractor
    ): MyProfilePresenter {
        return MyProfilePresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: PersonRepository): MyProfileInteractor {
        return MyProfileInteractorImpl(repository)
    }
}