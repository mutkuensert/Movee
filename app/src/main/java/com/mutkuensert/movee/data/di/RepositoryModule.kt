package com.mutkuensert.movee.data.di

import com.mutkuensert.movee.data.account.AccountRepositoryImpl
import com.mutkuensert.movee.data.authentication.AuthenticationRepositoryImpl
import com.mutkuensert.movee.data.movie.MovieRepositoryImpl
import com.mutkuensert.movee.domain.account.AccountRepository
import com.mutkuensert.movee.domain.login.AuthenticationRepository
import com.mutkuensert.movee.domain.movie.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindAuthenticationRepository(loginRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository

    @Singleton
    @Binds
    fun provideAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository

    @Singleton
    @Binds
    fun provideMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository
}