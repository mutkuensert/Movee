package com.mutkuensert.movee.feature.movie.navigation

import com.mutkuensert.movee.navigation.NavigationDispatcher
import com.mutkuensert.movee.navigation.NavigationType
import com.mutkuensert.movee.navigation.navigator.MovieNavigator
import javax.inject.Inject

class MovieNavigatorImpl @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher
) : MovieNavigator {
    override fun navigateToDetails(movieId: Int) {
        navigationDispatcher.navigate(
            NavigationType.ToRoute(
                route = ROUTE_MOVIE_DETAILS,
                args = listOf(KEY_MOVIE_ID to movieId)
            )
        )
    }
}