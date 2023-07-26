package com.mutkuensert.movee.data.authentication.dto

import com.squareup.moshi.Json

data class SessionDto(
    val success: Boolean,
    @Json(name = "session_id") val sessionId: String
)
