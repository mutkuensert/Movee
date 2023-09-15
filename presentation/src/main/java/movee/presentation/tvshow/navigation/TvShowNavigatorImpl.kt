package movee.presentation.tvshow.navigation

import javax.inject.Inject
import movee.presentation.navigation.NavigationDispatcher
import movee.presentation.navigation.NavigationType
import movee.presentation.navigation.navigator.TvShowNavigator

class TvShowNavigatorImpl @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher
) : TvShowNavigator {
    override fun navigateToDetails(tvShowId: Int) {
        navigationDispatcher.navigate(
            NavigationType.ToRoute(
                route = ROUTE_TV_SHOW_DETAILS, args = listOf(
                    KEY_TV_SHOW_ID to tvShowId
                )
            )
        )
    }
}