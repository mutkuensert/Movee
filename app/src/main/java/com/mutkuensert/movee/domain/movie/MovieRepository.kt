package com.mutkuensert.movee.domain.movie

import androidx.paging.PagingData
import com.mutkuensert.movee.data.movie.local.entity.PopularMovieEntity
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMoviesFlow(): Flow<PagingData<PopularMovieEntity>>
}