package com.mutkuensert.movee.data.account.remote.model

import com.squareup.moshi.Json

data class FavoriteTvShowsResponse(
    val page: Int,
    val results: List<FavoriteTvShowsResultsDto>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
)

data class FavoriteTvShowsResultsDto(
    val adult: Boolean,
    @Json(name = "backdrop_path") val backdropPath: String,
    @Json(name = "genre_ids") val genreIds: List<Int>,
    val id: Int,
    @Json(name = "original_language") val originalLanguage: String,
    @Json(name = "original_name") val originalName: String,
    val overview: String,
    val popularity: Double,
    @Json(name = "poster_path") val posterPath: String,
    @Json(name = "first_air_date") val firstAitDate: String,
    val title: String,
    val video: Boolean,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "vote_count") val voteCount: Int
)

data class FavoriteTvShowDto(
    val favorite: Boolean,
    @Json(name = "media_id") val mediaId: Int,
    @Json(name = "media_type")
    val mediaType: String = "tv"
)