package com.singlelab.lume.di

import com.singlelab.lume.ui.my_profile.MyProfilePresenter
import com.singlelab.lume.ui.my_profile.interactor.MyProfileInteractor
import com.singlelab.lume.ui.my_profile.interactor.MyProfileInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@InstallIn(ActivityComponent::class)
@Module
object MyProfileModule {

//    @Binds
//    abstract fun bindMyProfilePresenter(impl: MyProfilePresenter): MyProfilePresenter

    @Provides
    fun providePresenter(interactor: MyProfileInteractor): MyProfilePresenter {
        return MyProfilePresenter(interactor)
    }

    @Provides
    fun provideInteractor(): MyProfileInteractor {
        return MyProfileInteractorImpl()
    }
}