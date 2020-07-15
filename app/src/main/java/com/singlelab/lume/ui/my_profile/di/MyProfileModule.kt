package com.singlelab.lume.ui.my_profile.di

import com.singlelab.lume.ui.my_profile.MyProfilePresenter
import com.singlelab.lume.ui.my_profile.interactor.MyProfileInteractor
import com.singlelab.lume.ui.my_profile.interactor.MyProfileInteractorImpl
import com.singlelab.lume.ui.my_profile.router.MyProfileRouter
import com.singlelab.lume.ui.my_profile.router.MyProfileRouterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object MyProfileModule {

    @Provides
    fun providePresenter(
        interactor: MyProfileInteractor,
        router: MyProfileRouter
    ): MyProfilePresenter {
        return MyProfilePresenter(interactor, router)
    }

    @Provides
    fun provideInteractor(): MyProfileInteractor {
        return MyProfileInteractorImpl()
    }

    @Provides
    fun providesRouter(): MyProfileRouter {
        return MyProfileRouterImpl()
    }
}