package movee.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import movee.data.account.AccountRepositoryImpl
import movee.data.authentication.AuthenticationRepositoryImpl
import movee.data.movie.MovieRepositoryImpl
import movee.data.multisearch.MultiSearchRepositoryImpl
import movee.data.person.PersonRepositoryImpl
import movee.data.tvshow.TvShowRepositoryImpl
import movee.domain.account.AccountRepository
import movee.domain.login.AuthenticationRepository
import movee.domain.movie.MovieRepository
import movee.domain.multisearch.MultiSearchRepository
import movee.domain.person.PersonRepository
import movee.domain.tvshow.TvShowRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindAuthenticationRepository(authenticationRepositoryImpl: AuthenticationRepositoryImpl)
            : AuthenticationRepository

    @Singleton
    @Binds
    fun bindAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository

    @Singleton
    @Binds
    fun bindMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository

    @Singleton
    @Binds
    fun bindMultiSearchRepository(multiSearchRepositoryImpl: MultiSearchRepositoryImpl)
            : MultiSearchRepository

    @Singleton
    @Binds
    fun bindPersonRepository(personRepositoryImpl: PersonRepositoryImpl): PersonRepository

    @Singleton
    @Binds
    fun bindTvShowRepository(tvShowRepositoryImpl: TvShowRepositoryImpl): TvShowRepository
}