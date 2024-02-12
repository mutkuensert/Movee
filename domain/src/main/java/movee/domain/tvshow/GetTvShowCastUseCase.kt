package movee.domain.tvshow

import com.mutkuensert.androidphase.Phase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import movee.domain.tvshow.model.Person
import movee.domain.util.GetPhaseFlow

class GetTvShowCastUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {

    suspend fun execute(tvShowId: Int): Flow<Phase<List<Person>>> {
        return GetPhaseFlow.execute {
            tvShowRepository.getTvShowCast(tvShowId)
        }
    }
}