package com.mutkuensert.movee.core

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.mutkuensert.androidphase.Phase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GetPhaseFlow {

    companion object {

        suspend fun <T : Any, R : Throwable> execute(block: suspend () -> Result<T, R>): StateFlow<Phase<T>> =
            coroutineScope {
                val phase = MutableStateFlow<Phase<T>>(Phase.Standby())

                launch { runBlock(phase = phase, block = block) }

                phase.asStateFlow()
            }

        private suspend fun <T : Any, R : Throwable> runBlock(
            phase: MutableStateFlow<Phase<T>>,
            block: suspend () -> Result<T, R>
        ) {
            phase.value = Phase.Loading()

            block.invoke().onSuccess {
                phase.value = Phase.Success(data = it)
            }
                .onFailure {
                    phase.value = Phase.Error(error = it, message = it.message)
                }
        }
    }
}