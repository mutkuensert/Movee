package com.mutkuensert.movee.domain.login

enum class AuthenticationError(val statusCode: Int, val statusMessage: String) {
    RESOURCE_NOT_FOUND(34, "The resource you requested could not be found.")
}