package com.mutkuensert.movee.domain.tvshow

import androidx.paging.PagingData
import com.github.michaelbull.result.Result
import com.mutkuensert.movee.domain.Failure
import com.mutkuensert.movee.domain.tvshow.model.PopularTvShow
import com.mutkuensert.movee.domain.tvshow.model.TopRatedTvShow
import com.mutkuensert.movee.domain.tvshow.model.TvShowCast
import com.mutkuensert.movee.domain.tvshow.model.TvShowDetails
import kotlinx.coroutines.flow.Flow

interface TvShowRepository {
    suspend fun getTvShowDetails(tvShowId: Int): Result<TvShowDetails, Failure>
    suspend fun getTvShowCast(tvShowId: Int): Result<List<TvShowCast>, Failure>
    fun getPopularTvShowsPagingFlow(): Flow<PagingData<PopularTvShow>>
    fun getTopRatedTvShowsPagingFlow(): Flow<PagingData<TopRatedTvShow>>
}