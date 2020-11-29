package com.singlelab.lume.ui.my_profile.di

import com.singlelab.lume.LumeApplication
import com.singlelab.lume.database.LumeDatabase
import com.singlelab.lume.database.repository.ProfileRepository
import com.singlelab.lume.database.repository.RoomProfileRepository
import com.singlelab.lume.ui.my_profile.MyProfilePresenter
import com.singlelab.lume.ui.my_profile.interactor.MyProfileInteractor
import com.singlelab.lume.ui.my_profile.interactor.MyProfileInteractorImpl
import com.singlelab.net.repositories.person.PersonRepository
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
    fun provideInteractor(
        repository: PersonRepository,
        localRepository: ProfileRepository,
        database: LumeDatabase
    ): MyProfileInteractor {
        return MyProfileInteractorImpl(repository, localRepository, database)
    }

    @Provides
    fun provideLocalRepository(database: LumeDatabase): ProfileRepository {
        return RoomProfileRepository(database)
    }
}