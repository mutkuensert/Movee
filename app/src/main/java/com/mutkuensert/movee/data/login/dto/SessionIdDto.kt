package com.mutkuensert.movee.data.login.dto

import com.squareup.moshi.Json

data class SessionIdDto(
    @Json(name = "session_id") val sessionId: String
)
