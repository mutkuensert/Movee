package com.mutkuensert.movee.data.authentication.dto

import com.squareup.moshi.Json

data class LoginDto(
    val username: String,
    val password: String,
    @Json(name = "request_token") val requestToken: String
)
