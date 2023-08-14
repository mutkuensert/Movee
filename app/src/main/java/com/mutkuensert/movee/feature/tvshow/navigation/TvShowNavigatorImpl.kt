package com.mutkuensert.movee.feature.tvshow.navigation

import com.mutkuensert.movee.navigation.NavigationDispatcher
import com.mutkuensert.movee.navigation.NavigationType
import com.mutkuensert.movee.navigation.navigator.TvShowNavigator
import javax.inject.Inject

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