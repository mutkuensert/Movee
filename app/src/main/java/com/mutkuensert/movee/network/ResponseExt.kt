package com.mutkuensert.movee.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.mutkuensert.movee.domain.Failure
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

fun <T> Response<T>.toResult(): Result<T, Failure> {
    return if (isSuccessful && body() != null) {
        Ok(body()!!)
    } else {
        Err(errorBody().toFailure())
    }
}

fun <T, R> Response<T>.toResult(mapper: (T) -> R): Result<R, Failure> {
    return if (isSuccessful && body() != null) {
        Ok(mapper(body()!!))
    } else {
        Err(errorBody().toFailure())
    }
}

fun ResponseBody?.toFailure(): Failure {
    if (this == null) return Failure.empty()

    return try {
        val jsonObject = JSONObject(this.string())

        Failure(
            success = jsonObject.getBoolean("success"),
            statusCode = jsonObject.getInt("status_code"),
            statusMessage = jsonObject.getString("status_message")
        )
    } catch (e: JSONException) {
        Failure.empty()
    }
}