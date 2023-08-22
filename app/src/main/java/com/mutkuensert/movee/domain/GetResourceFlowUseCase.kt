package com.mutkuensert.movee.domain

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.mutkuensert.movee.core.invokeSuspendingForResult
import com.mutkuensert.movee.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Create a new instance for every use case.
 */
class GetResourceFlowUseCase<T> {
    private val _resource: MutableStateFlow<Resource<T>> = MutableStateFlow(Resource.Standby())

    suspend fun execute(block: suspend () -> Result<T, Failure>): StateFlow<Resource<T>> {
        runBlock(block = block)

        return _resource.asStateFlow()
    }

    private suspend fun runBlock(block: suspend () -> Result<T, Failure>) {
        _resource.value = Resource.Loading()

        invokeSuspendingForResult(block)
            .onSuccess { result ->
                result.onSuccess {
                    _resource.value = Resource.Success(data = it)
                }
                    .onFailure {
                        _resource.value = Resource.Error(error = it, message = it.message)
                    }
            }
            .onFailure {
                _resource.value = Resource.Error(error = it, message = it.message)
            }
    }
}