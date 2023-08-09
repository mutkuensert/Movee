package com.mutkuensert.movee.domain.movie

import androidx.paging.PagingData
import com.mutkuensert.movee.domain.movie.model.PopularMovie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMoviesFlow(): Flow<PagingData<PopularMovie>>
}