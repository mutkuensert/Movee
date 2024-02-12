package movee.domain.movie

import com.mutkuensert.androidphase.Phase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import movee.domain.movie.model.Person
import movee.domain.util.GetPhaseFlow

class GetMovieCastUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend fun execute(movieId: Int): Flow<Phase<List<Person>>> {
        return GetPhaseFlow.execute {
            movieRepository.getMovieCast(movieId)
        }
    }
}