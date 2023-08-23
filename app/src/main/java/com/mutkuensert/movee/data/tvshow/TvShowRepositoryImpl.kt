package com.mutkuensert.movee.data.tvshow

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.github.michaelbull.result.Result
import com.mutkuensert.movee.data.tvshow.source.PopularTvShowsPagingSource
import com.mutkuensert.movee.data.tvshow.source.TopRatedTvShowsPagingSource
import com.mutkuensert.movee.data.util.getImageUrl
import com.mutkuensert.movee.domain.Failure
import com.mutkuensert.movee.domain.tvshow.TvShowRepository
import com.mutkuensert.movee.domain.tvshow.model.PopularTvShow
import com.mutkuensert.movee.domain.tvshow.model.TopRatedTvShow
import com.mutkuensert.movee.domain.tvshow.model.TvShowCast
import com.mutkuensert.movee.domain.tvshow.model.TvShowDetails
import com.mutkuensert.movee.network.toResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TvShowRepositoryImpl @Inject constructor(
    private val tvShowsApi: TvShowsApi,
) : TvShowRepository {
    override suspend fun getTvShowDetails(tvShowId: Int): Result<TvShowDetails, Failure> {
        return tvShowsApi.getTvShowDetails(tvShowId).toResult(mapper = { response ->
            TvShowDetails(
                id = response.id,
                name = response.name,
                overview = response.overview,
                imageUrl = getImageUrl(response.posterPath),
                seasonCount = response.seasons.size,
                totalEpisodeNumber = response.seasons.sumOf { it.episodeCount },
                voteAverage = response.voteAverage
            )
        })
    }

    override suspend fun getTvShowCast(tvShowId: Int): Result<List<TvShowCast>, Failure> {
        return tvShowsApi.getTvShowCredits(tvShowId).toResult(mapper = { response ->
            response.cast.map {
                TvShowCast(
                    id = it.id,
                    name = it.name,
                    imageUrl = getImageUrl(it.profilePath),
                    character = it.character
                )
            }
        })
    }

    override fun getPopularTvShowsPagingFlow(): Flow<PagingData<PopularTvShow>> {
        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            PopularTvShowsPagingSource(tvShowsApi::getPopularTvShows)
        }.flow.map { pagingData ->
            pagingData.map {
                PopularTvShow(
                    imageUrl = getImageUrl(it.posterPath),
                    id = it.id,
                    voteAverage = it.voteAverage,
                    name = it.name
                )
            }
        }
    }

    override fun getTopRatedTvShowsPagingFlow(): Flow<PagingData<TopRatedTvShow>> {
        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            TopRatedTvShowsPagingSource(tvShowsApi::getTopRatedTvShows)
        }.flow.map { pagingData ->
            pagingData.map {
                TopRatedTvShow(
                    imageUrl = getImageUrl(it.posterPath),
                    id = it.id,
                    voteAverage = it.voteAverage,
                    name = it.name
                )
            }
        }
    }
}