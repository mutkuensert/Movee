package com.mutkuensert.movee.data.login.dto

import com.squareup.moshi.Json

data class ValidRequestTokenDto(
    @Json(name = "request_token") val validRequestToken: String
)
