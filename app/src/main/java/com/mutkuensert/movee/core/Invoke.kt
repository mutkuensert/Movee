package com.mutkuensert.movee.core

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

fun <T> invoke(block: () -> T): Result<T, Throwable> {
    return try {
        Ok(block.invoke())
    } catch (e: Throwable) {
        Err(e)
    }
}

suspend fun <T> invokeSuspending(block: suspend () -> T): Result<T, Throwable> {
    return try {
        Ok(block.invoke())
    } catch (e: Throwable) {
        Err(e)
    }
}