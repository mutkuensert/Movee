package com.mutkuensert.movee.util

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?, message: String? = null): Resource<T> {
            return Resource(Status.SUCCESS, data, message)
        }

        fun <T> error(data: T? = null, message: String? = null): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null, message: String? = null): Resource<T> {
            return Resource(Status.LOADING, data, message)
        }

        fun <T> standby(data: T? = null, message: String? = null): Resource<T> {
            return Resource(Status.STANDBY, data, message)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    STANDBY
}