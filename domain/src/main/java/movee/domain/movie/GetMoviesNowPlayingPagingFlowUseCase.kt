package movee.domain.movie

import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import movee.domain.movie.model.MovieNowPlaying

class GetMoviesNowPlayingPagingFlowUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    fun execute(): Flow<PagingData<MovieNowPlaying>> =
        movieRepository.getMoviesNowPlayingPagingFlow()
}