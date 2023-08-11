package com.mutkuensert.movee.data.movie.remote.model

import com.squareup.moshi.Json

data class PopularMoviesResponse(
    val page: Int,
    val results: List<PopularMoviesResultDto>,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int
)

data class PopularMoviesResultDto(
    @Json(name = "poster_path") val posterPath: String?,
    val title: String,
    val id: Int,
    @Json(name = "vote_average") val voteAverage: Double
)