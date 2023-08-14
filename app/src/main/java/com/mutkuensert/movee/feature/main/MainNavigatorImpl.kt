package com.mutkuensert.movee.feature.main

import androidx.navigation.NavController
import com.mutkuensert.movee.feature.movie.navigation.GRAPH_MOVIE
import com.mutkuensert.movee.navigation.NavigationDispatcher
import com.mutkuensert.movee.navigation.NavigationType
import com.mutkuensert.movee.navigation.navigator.MainNavigator
import javax.inject.Inject
import timber.log.Timber

class MainNavigatorImpl @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher
) : MainNavigator {
    override var navController: NavController? = null

    override fun navigateToHome() {
        try {
            navController!!.navigate(GRAPH_MOVIE) {
                launchSingleTop = true
                popUpTo(GRAPH_MOVIE) {
                    inclusive = true
                }
            }
        } catch (e: NullPointerException) {
            Timber.e("navController was not set.")
        }
    }

    override fun navigateUp(popUpTo: String?, isInclusive: Boolean) {
        navigationDispatcher.navigate(NavigationType.Up(popUpTo))
    }

    override fun navigateBack() {
        navigationDispatcher.navigate(NavigationType.Back())
    }

    override fun navigateToTab(route: String) {
        navigationDispatcher.navigate(NavigationType.ToTab(route))
    }
}