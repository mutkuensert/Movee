package movee.data.tvshow

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.getOrElse
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import movee.data.network.toResult
import movee.data.tvshow.local.TvShowDao
import movee.data.tvshow.local.model.PopularTvShowEntity
import movee.data.tvshow.local.model.TopRatedTvShowEntity
import movee.data.tvshow.remote.mapper.PopularTvShowDtoMapper
import movee.data.tvshow.remote.mapper.TopRatedTvShowDtoMapper
import movee.data.util.GenericRemoteMediator
import movee.data.util.getImageUrl
import movee.data.util.withDecimalDigits
import movee.domain.Failure
import movee.domain.tvshow.TvShowRepository
import movee.domain.tvshow.model.PopularTvShow
import movee.domain.tvshow.model.TopRatedTvShow
import movee.domain.tvshow.model.Person
import movee.domain.tvshow.model.TvShowDetails

class TvShowRepositoryImpl @Inject constructor(
    private val tvShowsApi: TvShowsApi,
    private val tvShowDao: TvShowDao,
    private val topRatedTvShowDtoMapper: TopRatedTvShowDtoMapper,
    private val popularTvShowDtoMapper: PopularTvShowDtoMapper,
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
                voteAverage = response.voteAverage.withDecimalDigits(1)
            )
        })
    }

    override suspend fun getTvShowCast(tvShowId: Int): Result<List<Person>, Failure> {
        return tvShowsApi.getTvShowCredits(tvShowId).toResult(mapper = { response ->
            response.cast.map {
                Person(
                    id = it.id,
                    name = it.name,
                    imageUrl = getImageUrl(it.profilePath),
                    character = it.character
                )
            }
        })
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPopularTvShowsPagingFlow(): Flow<PagingData<PopularTvShow>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = GenericRemoteMediator(
                fetch = ::fetchPopularTvShows,
                getLastPageInLocal = { tvShowDao.getAllPopularTvShows().lastOrNull()?.page }
            ),
            pagingSourceFactory = { tvShowDao.getPopularTvShowsPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map {
                PopularTvShow(
                    imageUrl = getImageUrl(it.posterPath),
                    id = it.id,
                    voteAverage = it.voteAverage,
                    name = it.title,
                    isFavorite = it.isFavorite
                )
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getTopRatedTvShowsPagingFlow(): Flow<PagingData<TopRatedTvShow>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = GenericRemoteMediator(
                fetch = ::fetchTopRatedTvShows,
                getLastPageInLocal = { tvShowDao.getAllTopRatedTvShows().lastOrNull()?.page }
            ),
            pagingSourceFactory = { tvShowDao.getTopRatedTvShowsPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map {
                TopRatedTvShow(
                    imageUrl = getImageUrl(it.posterPath),
                    id = it.id,
                    voteAverage = it.voteAverage,
                    name = it.title,
                    isFavorite = it.isFavorite
                )
            }
        }
    }

    override suspend fun addTvShowToFavorites(tvShowId: Int) {
        tvShowDao.getPopularTvShow(tvShowId)
            ?.copyWithPrimaryKey(isFavorite = true)
            ?.let {
                tvShowDao.updatePopularTvShow(it)
            }

        tvShowDao.getTopRatedTvShow(tvShowId)
            ?.copyWithPrimaryKey(isFavorite = true)
            ?.let {
                tvShowDao.updateTopRatedTvShow(it)
            }
    }

    override suspend fun removeTvShowFromFavorites(tvShowId: Int) {
        tvShowDao.getPopularTvShow(tvShowId)
            ?.copyWithPrimaryKey(isFavorite = false)
            ?.let {
                tvShowDao.updatePopularTvShow(it)
            }

        tvShowDao.getTopRatedTvShow(tvShowId)
            ?.copyWithPrimaryKey(isFavorite = false)
            ?.let {
                tvShowDao.updateTopRatedTvShow(it)
            }
    }

    private suspend fun fetchTopRatedTvShows(
        page: Int,
        clearLocalData: Boolean
    ): Result<List<TopRatedTvShowEntity>, Failure> {
        return try {
            coroutineScope {
                withContext(Dispatchers.IO) {
                    val response = tvShowsApi.getTopRatedTvShows(page).toResult()
                        .getOrElse { throw Failure.empty() }

                    if (clearLocalData) {
                        tvShowDao.clearAllTopRatedTvShows()
                    }

                    response.results.map {
                        topRatedTvShowDtoMapper.mapToEntity(it, page)
                    }.also {
                        tvShowDao.insertTopRatedTvShows(it)
                    }.run { Ok(this) }
                }
            }
        } catch (failure: Failure) {
            Err(failure)
        }
    }

    private suspend fun fetchPopularTvShows(
        page: Int,
        clearLocalData: Boolean
    ): Result<List<PopularTvShowEntity>, Failure> {
        return try {
            coroutineScope {
                withContext(Dispatchers.IO) {
                    val response = tvShowsApi.getPopularTvShows(page).toResult()
                        .getOrElse { throw Failure.empty() }

                    if (clearLocalData) {
                        tvShowDao.clearAllPopularTvShows()
                    }

                    response.results.map {
                        popularTvShowDtoMapper.mapToEntity(it, page)
                    }.also {
                        tvShowDao.insertPopularTvShows(it)
                    }.run { Ok(this) }
                }
            }
        } catch (failure: Failure) {
            Err(failure)
        }
    }
}