package com.mutkuensert.movee.data.account.remote.model

import com.squareup.moshi.Json

data class FavoriteMoviesResponseModel(
    val page: Int,
    val results: List<FavoriteMoviesResultResponseModel>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
)