package com.mutkuensert.movee.domain.movie

import com.mutkuensert.androidphase.Phase
import com.mutkuensert.movee.core.GetPhaseFlow
import com.mutkuensert.movee.domain.movie.model.MovieDetails
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend fun execute(movieId: Int): Flow<Phase<MovieDetails>> {
        return GetPhaseFlow<MovieDetails>().execute {
            movieRepository.getMovieDetails(movieId)
        }
    }
}