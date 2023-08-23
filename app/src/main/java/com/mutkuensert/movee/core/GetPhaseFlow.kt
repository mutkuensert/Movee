package com.mutkuensert.movee.core

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.mutkuensert.androidphase.Phase
import com.mutkuensert.movee.domain.Failure
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Create a new instance for every use case.
 */
class GetPhaseFlow<T> {
    private val _phase: MutableStateFlow<Phase<T>> = MutableStateFlow(Phase.Standby())

    suspend fun execute(block: suspend () -> Result<T, Failure>): StateFlow<Phase<T>> =
        coroutineScope {
            launch { runBlock(block = block) }

            _phase.asStateFlow()
        }

    private suspend fun runBlock(block: suspend () -> Result<T, Failure>) {
        _phase.value = Phase.Loading()

        invokeSuspendingForResult(block)
            .onSuccess { result ->
                result.onSuccess {
                    _phase.value = Phase.Success(data = it)
                }
                    .onFailure {
                        _phase.value = Phase.Error(error = it, message = it.message)
                    }
            }
            .onFailure {
                _phase.value = Phase.Error(error = it, message = it.message)
            }
    }
}