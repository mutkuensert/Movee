package com.mutkuensert.movee.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import retrofit2.Response

fun <T> Response<T>.getBodyIfSuccessful(): T? {
    if (this.isSuccessful) body()

    return null
}

fun <T> Response<T>.toResult(): Result<T, Error> {
    return if (isSuccessful && body() != null) {
        Ok(body()!!)
    } else {
        Err(Error(errorBody()?.string() ?: ""))
    }
}

fun <T, R> Response<T>.toResult(mapper: (T) -> R): Result<R, Error> {
    return if (isSuccessful && body() != null) {
        Ok(mapper(body()!!))
    } else {
        Err(Error(errorBody()?.string() ?: ""))
    }
}