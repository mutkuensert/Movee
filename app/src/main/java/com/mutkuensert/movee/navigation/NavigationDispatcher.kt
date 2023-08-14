package com.mutkuensert.movee.navigation

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@Singleton
class NavigationDispatcher @Inject constructor() {
    private val _commands = Channel<NavigationType>(Channel.CONFLATED)

    internal val commands = _commands.receiveAsFlow()

    fun navigate(navigationType: NavigationType) {
        _commands.trySend(navigationType)
    }
}