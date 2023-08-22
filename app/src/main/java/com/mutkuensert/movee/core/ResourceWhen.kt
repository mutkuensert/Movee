package com.mutkuensert.movee.core

import androidx.compose.runtime.Composable
import com.mutkuensert.movee.domain.util.Resource

/**
 * @param onSuccessWithData Invokes if data is not null.
 */
@Composable
fun <T> Resource<T>.When(
    onStandby: @Composable Resource<T>.() -> Unit = {},
    onLoading: @Composable Resource<T>.() -> Unit = {},
    onSuccess: @Composable Resource<T>.() -> Unit = {},
    onSuccessWithData: (@Composable Resource<T>.(data: T) -> Unit)? = null,
    onError: @Composable Resource<T>.() -> Unit = {}
) {
    when (this) {
        is Resource.Standby -> onStandby.invoke(this)
        is Resource.Loading -> onLoading.invoke(this)
        is Resource.Success -> {
            if (onSuccessWithData == null) {
                onSuccess.invoke(this)
            } else {
                if (data != null) {
                    onSuccessWithData.invoke(this, data!!)
                }
            }
        }

        is Resource.Error -> onError.invoke(this)
    }
}