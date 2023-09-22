package movee.domain.tvshow

import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import movee.domain.tvshow.model.PopularTvShow

class GetPopularTvShowsPagingFlowUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {

    fun execute(): Flow<PagingData<PopularTvShow>> =
        tvShowRepository.getPopularTvShowsPagingFlow()
}