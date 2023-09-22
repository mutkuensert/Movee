package movee.data.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import movee.domain.Failure
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

/**
 * @return Returns Ok if response is successful and body isn't null,
 * otherwise Failure.
 */
fun <T> Response<T>.toResult(): Result<T, Failure> {
    return if (isSuccessful && body() != null) {
        Ok(body()!!)
    } else {
        Err(errorBody().toFailure())
    }
}

/**
 * @return Returns mapped Ok if response is successful and body isn't null,
 * otherwise Failure.
 */
fun <T, R> Response<T>.toResult(mapper: (T) -> R): Result<R, Failure> {
    return if (isSuccessful && body() != null) {
        Ok(mapper(body()!!))
    } else {
        Err(errorBody().toFailure())
    }
}

/**
 * @return Returns mapped Ok if response is successful and body isn't null,
 * otherwise Failure.
 */
fun <T> Response<T>.toEmptyResult(): Result<Unit, Failure> {
    return if (isSuccessful) {
        Ok(Unit)
    } else {
        Err(errorBody().toFailure())
    }
}

fun ResponseBody?.toFailure(): Failure {
    if (this == null) return Failure.empty()

    return try {
        val jsonObject = JSONObject(this.string())

        Failure(
            statusCode = jsonObject.getInt("status_code"),
            message = jsonObject.getString("status_message")
        )
    } catch (e: JSONException) {
        Failure.empty()
    }
}