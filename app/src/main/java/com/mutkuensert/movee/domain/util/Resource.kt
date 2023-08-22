package com.mutkuensert.movee.domain.util

sealed class Resource<T>(
    open val data: T? = null,
    open val message: String?,
    open val error: Throwable? = null
) {

    class Standby<T>(
        override val data: T? = null,
        override val message: String? = null,
        override val error: Throwable? = null
    ) : Resource<T>(data, message, error)

    class Loading<T>(
        override val data: T? = null,
        override val message: String? = null,
        override val error: Throwable? = null
    ) : Resource<T>(data, message, error)

    class Success<T>(
        override val data: T? = null,
        override val message: String? = null,
        override val error: Throwable? = null
    ) : Resource<T>(data, message, error)

    class Error<T>(
        override val data: T? = null,
        override val message: String? = null,
        override val error: Throwable? = null
    ) : Resource<T>(data, message, error)
}