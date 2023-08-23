package com.mutkuensert.movee.domain.tvshow

import com.mutkuensert.androidphase.Phase
import com.mutkuensert.movee.core.GetPhaseFlow
import com.mutkuensert.movee.domain.tvshow.model.TvShowCast
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetTvShowCastUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {

    suspend fun execute(tvShowId: Int): Flow<Phase<List<TvShowCast>>> {
        return GetPhaseFlow<List<TvShowCast>>().execute {
            tvShowRepository.getTvShowCast(tvShowId)
        }
    }
}