package com.singlelab.lume.di

import android.content.Context
import com.singlelab.lume.database.LumeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LumeDatabase =
        LumeDatabase.create(context)
}