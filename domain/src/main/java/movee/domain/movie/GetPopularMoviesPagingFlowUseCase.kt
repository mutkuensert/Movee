package movee.domain.movie

import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import movee.domain.movie.model.PopularMovie

class GetPopularMoviesPagingFlowUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    fun execute(): Flow<PagingData<PopularMovie>> = movieRepository.getPopularMoviesPagingFlow()
}