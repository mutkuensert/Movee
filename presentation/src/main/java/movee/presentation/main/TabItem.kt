package movee.presentation.main

import movee.presentation.login.navigation.ROUTE_LOGIN
import movee.presentation.movie.navigation.ROUTE_MOVIES
import movee.presentation.multisearch.navigation.ROUTE_MULTI_SEARCH
import movee.presentation.tvshow.navigation.ROUTE_TV_SHOWS

sealed class TabItem(val route: String, val titleKey: String) {
    object Movie : TabItem(route = ROUTE_MOVIES, titleKey = "Movie")
    object TvShow : TabItem(route = ROUTE_TV_SHOWS, titleKey = "Tv Show")
    object MultiSearch :
        TabItem(route = ROUTE_MULTI_SEARCH, titleKey = "Multi Search")

    object Login : TabItem(route = ROUTE_LOGIN, titleKey = "Login")

    companion object {
        fun all(): List<TabItem> {
            return listOf(Movie, TvShow, MultiSearch, Login)
        }
    }
}