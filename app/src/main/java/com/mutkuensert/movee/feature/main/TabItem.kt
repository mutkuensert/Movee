package com.mutkuensert.movee.feature.main

import com.mutkuensert.movee.feature.login.navigation.ROUTE_LOGIN
import com.mutkuensert.movee.feature.movie.navigation.GRAPH_MOVIE
import com.mutkuensert.movee.feature.multisearch.navigation.ROUTE_MULTI_SEARCH
import com.mutkuensert.movee.feature.tvshow.navigation.GRAPH_TV_SHOW

sealed class TabItem(val route: String, val titleKey: String) {
    object Movie : TabItem(route = GRAPH_MOVIE, titleKey = "Movie")
    object TvShow : TabItem(route = GRAPH_TV_SHOW, titleKey = "Tv Show")
    object MultiSearch :
        TabItem(route = ROUTE_MULTI_SEARCH, titleKey = "Multi Search")

    object Login : TabItem(route = ROUTE_LOGIN, titleKey = "Login")

    companion object {
        fun all(): List<TabItem> {
            return listOf(Movie, TvShow, MultiSearch, Login)
        }
    }
}