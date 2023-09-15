package movee.domain.movie

import com.mutkuensert.androidphase.Phase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import movee.domain.movie.model.MovieDetails
import movee.domain.util.GetPhaseFlow

class GetMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend fun execute(movieId: Int): Flow<Phase<MovieDetails>> {
        return GetPhaseFlow.execute {
            movieRepository.getMovieDetails(movieId)
        }
    }
}