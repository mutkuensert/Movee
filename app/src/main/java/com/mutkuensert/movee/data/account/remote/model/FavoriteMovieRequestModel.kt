package com.mutkuensert.movee.data.account.remote.model

import com.squareup.moshi.Json

data class FavoriteMovieRequestModel(
    val favorite: Boolean,
    @Json(name = "media_id") val mediaId: Int,
    @Json(name = "media_type") val mediaType: String
)