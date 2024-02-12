package movee.domain.tvshow

import androidx.paging.PagingData
import com.github.michaelbull.result.Result
import movee.domain.Failure
import kotlinx.coroutines.flow.Flow
import movee.domain.tvshow.model.PopularTvShow
import movee.domain.tvshow.model.TopRatedTvShow
import movee.domain.tvshow.model.Person
import movee.domain.tvshow.model.TvShowDetails

interface TvShowRepository {
    suspend fun getTvShowDetails(tvShowId: Int): Result<TvShowDetails, Failure>
    suspend fun getTvShowCast(tvShowId: Int): Result<List<Person>, Failure>
    fun getPopularTvShowsPagingFlow(): Flow<PagingData<PopularTvShow>>
    fun getTopRatedTvShowsPagingFlow(): Flow<PagingData<TopRatedTvShow>>
    suspend fun addTvShowToFavorites(tvShowId: Int)
    suspend fun removeTvShowFromFavorites(tvShowId: Int)
}