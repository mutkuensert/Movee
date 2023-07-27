package com.mutkuensert.movee.domain

data class Failure(
    val success: Boolean,
    val statusCode: Int,
    val statusMessage: String,
) {
    companion object {
        fun empty(): Failure {
            return Failure(success = false, statusCode = -1, statusMessage = "Unknown")
        }
    }
}
