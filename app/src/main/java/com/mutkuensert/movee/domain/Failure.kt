package com.mutkuensert.movee.domain

data class Failure(
    val statusCode: Int? = null,
    override val message: String,
    override val cause: Throwable? = null,
) : Throwable() {

    companion object {
        fun empty(): Failure {
            return Failure(message = "Unknown")
        }
    }
}
