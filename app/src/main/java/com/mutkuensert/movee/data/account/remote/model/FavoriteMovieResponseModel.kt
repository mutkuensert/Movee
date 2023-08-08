package com.mutkuensert.movee.data.account.remote.model

import com.squareup.moshi.Json

data class FavoriteMovieResponseModel(
    @Json(name = "status_code")
    val statusCode: Int,
    @Json(name = "status_message")
    val statusMessage: String
)