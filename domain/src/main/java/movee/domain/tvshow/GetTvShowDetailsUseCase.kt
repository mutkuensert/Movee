package movee.domain.tvshow

import com.mutkuensert.androidphase.Phase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import movee.domain.tvshow.model.TvShowDetails
import movee.domain.util.GetPhaseFlow

class GetTvShowDetailsUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {

    suspend fun execute(tvShowId: Int): Flow<Phase<TvShowDetails>> {
        return GetPhaseFlow.execute {
            tvShowRepository.getTvShowDetails(tvShowId)
        }
    }
}