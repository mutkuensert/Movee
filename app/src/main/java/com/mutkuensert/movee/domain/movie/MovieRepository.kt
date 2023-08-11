package com.mutkuensert.movee.domain.movie

import androidx.paging.PagingData
import com.github.michaelbull.result.Result
import com.mutkuensert.movee.domain.Failure
import com.mutkuensert.movee.domain.movie.model.MovieCast
import com.mutkuensert.movee.domain.movie.model.MovieDetails
import com.mutkuensert.movee.domain.movie.model.MovieNowPlaying
import com.mutkuensert.movee.domain.movie.model.PopularMovie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMoviesPagingFlow(): Flow<PagingData<PopularMovie>>
    fun getMoviesNowPlayingPagingFlow(): Flow<PagingData<MovieNowPlaying>>
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails, Failure>
    suspend fun getMovieCast(movieId: Int): Result<List<MovieCast>, Failure>

}