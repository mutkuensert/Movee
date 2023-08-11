package com.mutkuensert.movee.data.di

import com.mutkuensert.movee.data.account.AccountRepositoryImpl
import com.mutkuensert.movee.data.authentication.AuthenticationRepositoryImpl
import com.mutkuensert.movee.data.movie.MovieRepositoryImpl
import com.mutkuensert.movee.data.multisearch.MultiSearchRepositoryImpl
import com.mutkuensert.movee.data.person.PersonRepositoryImpl
import com.mutkuensert.movee.data.tvshow.TvShowRepositoryImpl
import com.mutkuensert.movee.domain.account.AccountRepository
import com.mutkuensert.movee.domain.login.AuthenticationRepository
import com.mutkuensert.movee.domain.movie.MovieRepository
import com.mutkuensert.movee.domain.multisearch.MultiSearchRepository
import com.mutkuensert.movee.domain.person.PersonRepository
import com.mutkuensert.movee.domain.tvshow.TvShowRepository
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
    fun bindAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository

    @Singleton
    @Binds
    fun bindMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository

    @Singleton
    @Binds
    fun bindMultiSearchRepository(multiSearchRepositoryImpl: MultiSearchRepositoryImpl): MultiSearchRepository

    @Singleton
    @Binds
    fun bindPersonRepository(personRepositoryImpl: PersonRepositoryImpl): PersonRepository

    @Singleton
    @Binds
    fun bindTvShowRepository(tvShowRepositoryImpl: TvShowRepositoryImpl): TvShowRepository
}