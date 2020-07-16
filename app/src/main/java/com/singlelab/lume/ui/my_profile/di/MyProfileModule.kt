package com.singlelab.lume.ui.my_profile.di

import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.my_profile.MyProfileRepository
import com.singlelab.data.repositories.my_profile.MyProfileRepositoryImpl
import com.singlelab.lume.LumeApplication
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
        return MyProfilePresenter(interactor, router, LumeApplication.preferences)
    }

    @Provides
    fun provideInteractor(repository: MyProfileRepository): MyProfileInteractor {
        return MyProfileInteractorImpl(repository)
    }

    @Provides
    fun providesRouter(): MyProfileRouter {
        return MyProfileRouterImpl()
    }

    @Provides
    fun providesRepository(apiUnit: ApiUnit): MyProfileRepository {
        return MyProfileRepositoryImpl(apiUnit)
    }
}