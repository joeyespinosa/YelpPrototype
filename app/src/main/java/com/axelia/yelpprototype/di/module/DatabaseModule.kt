package com.axelia.yelpprototype.di.module

import android.app.Application
import com.axelia.yelpprototype.data.local.YelpDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application) = YelpDatabase.getInstance(application)

    @Singleton
    @Provides
    fun provideBusinessDao(database: YelpDatabase) = database.getBusinessDao()
}