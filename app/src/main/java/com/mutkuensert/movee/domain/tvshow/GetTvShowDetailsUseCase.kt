package com.mutkuensert.movee.domain.tvshow

import com.mutkuensert.androidphase.Phase
import com.mutkuensert.movee.core.GetPhaseFlow
import com.mutkuensert.movee.domain.tvshow.model.TvShowDetails
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetTvShowDetailsUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {

    suspend fun execute(tvShowId: Int): Flow<Phase<TvShowDetails>> {
        return GetPhaseFlow<TvShowDetails>().execute {
            tvShowRepository.getTvShowDetails(tvShowId)
        }
    }
}