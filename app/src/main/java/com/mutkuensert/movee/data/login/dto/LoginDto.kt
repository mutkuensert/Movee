package com.mutkuensert.movee.data.login.dto

import com.squareup.moshi.Json

data class LoginDto(
    val username: String,
    val password: String,
    @Json(name = "request_token") val requestToken: String
)
