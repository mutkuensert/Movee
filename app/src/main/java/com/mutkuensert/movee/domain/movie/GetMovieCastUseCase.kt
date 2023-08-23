package com.mutkuensert.movee.domain.movie

import com.mutkuensert.androidphase.Phase
import com.mutkuensert.movee.core.GetPhaseFlow
import com.mutkuensert.movee.domain.movie.model.MovieCast
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetMovieCastUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend fun execute(movieId: Int): Flow<Phase<List<MovieCast>>> {
        return GetPhaseFlow.execute {
            movieRepository.getMovieCast(movieId)
        }
    }
}