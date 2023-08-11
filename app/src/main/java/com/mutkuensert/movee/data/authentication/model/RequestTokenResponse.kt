package com.mutkuensert.movee.data.authentication.model

import com.squareup.moshi.Json

data class RequestTokenResponse(
    val success: Boolean,
    @Json(name = "expires_at") val expiresAt: String,
    @Json(name = "request_token") val requestToken: String
)
