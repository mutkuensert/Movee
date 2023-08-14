package com.mutkuensert.movee.feature.main

import com.mutkuensert.movee.feature.login.navigation.ROUTE_LOGIN
import com.mutkuensert.movee.feature.movie.navigation.GRAPH_MOVIE
import com.mutkuensert.movee.feature.multisearch.navigation.ROUTE_MULTI_SEARCH
import com.mutkuensert.movee.feature.tvshow.navigation.GRAPH_TV_SHOW

sealed class Tabs(val route: String, val titleKey: String) {
    object Movie : Tabs(route = GRAPH_MOVIE, titleKey = "Movie")
    object TvShow : Tabs(route = GRAPH_TV_SHOW, titleKey = "Tv Show")
    object MultiSearch :
        Tabs(route = ROUTE_MULTI_SEARCH, titleKey = "Multi Search")

    object Login : Tabs(route = ROUTE_LOGIN, titleKey = "Login")
}