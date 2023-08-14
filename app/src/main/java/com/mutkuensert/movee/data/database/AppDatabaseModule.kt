package com.mutkuensert.movee.data.database

import android.content.Context
import androidx.room.Room
import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.data.movie.local.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDatabaseModule {
    private const val APP_DATABASE_NAME = "app-database"

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, APP_DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(appDatabase: AppDatabase): MovieDao = appDatabase.movieDao()

    @Singleton
    @Provides
    fun provideAccountDao(appDatabase: AppDatabase): AccountDao = appDatabase.accountDao()
}