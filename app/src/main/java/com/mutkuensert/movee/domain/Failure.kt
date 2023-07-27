package com.mutkuensert.movee.domain

data class Failure(
    val statusCode: Int? = null,
    val statusMessage: String,
) {
    companion object {
        fun empty(): Failure {
            return Failure(statusMessage = "Unknown")
        }
    }
}
