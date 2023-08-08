package com.mutkuensert.movee.data.movie

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.data.movie.local.MovieDao
import com.mutkuensert.movee.data.movie.local.entity.PopularMovieEntity
import com.mutkuensert.movee.data.movie.remote.mediator.PopularMoviesRemoteMediator
import com.mutkuensert.movee.domain.movie.MovieRepository
import com.mutkuensert.movee.library.session.SessionManager
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao,
    private val accountDao: AccountDao,
    private val sessionManager: SessionManager,
) : MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPopularMoviesFlow(): Flow<PagingData<PopularMovieEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = PopularMoviesRemoteMediator(
                getPopularMovies = movieApi::getPopularMovies,
                movieDao = movieDao
            ),
            pagingSourceFactory = { movieDao.getPopularMoviesPagingSource() }
        ).flow
    }
}