package movee.presentation.movie.navigation

import movee.presentation.navigation.NavigationDispatcher
import movee.presentation.navigation.NavigationType
import movee.presentation.navigation.navigator.MovieNavigator
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