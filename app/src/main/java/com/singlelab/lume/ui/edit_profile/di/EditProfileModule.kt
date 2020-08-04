package com.singlelab.lume.ui.edit_profile.di

import com.singlelab.lume.LumeApplication
import com.singlelab.lume.ui.edit_profile.EditProfilePresenter
import com.singlelab.lume.ui.edit_profile.interactor.EditProfileInteractor
import com.singlelab.lume.ui.edit_profile.interactor.EditProfileInteractorImpl
import com.singlelab.net.repositories.person.PersonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@InstallIn(ActivityComponent::class)
@Module
object EditProfileModule {
    @Provides
    fun providePresenter(interactor: EditProfileInteractor): EditProfilePresenter {
        return EditProfilePresenter(interactor, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: PersonRepository): EditProfileInteractor {
        return EditProfileInteractorImpl(repository)
    }
}