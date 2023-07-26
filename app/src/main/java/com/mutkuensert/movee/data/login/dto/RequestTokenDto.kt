package com.mutkuensert.movee.data.login.dto

import com.squareup.moshi.Json

data class RequestTokenDto(
    val success: Boolean,
    @Json(name = "expires_at") val expiresAt: String,
    @Json(name = "request_token") val requestToken: String
)
