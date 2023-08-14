package com.mutkuensert.movee.feature.tvshow.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.mutkuensert.movee.feature.tvshow.tvshowdetails.TvDetailsScreen
import com.mutkuensert.movee.feature.tvshow.tvshows.TvShowsScreen
import com.mutkuensert.movee.navigation.FeatureNavigationBuilder
import javax.inject.Inject

const val ROUTE_TV_SHOWS = "tvShowsRoute"
const val GRAPH_TV_SHOW = "tvShowsNavGraph"
const val KEY_TV_SHOW_ID = "tvShowId"
const val ROUTE_TV_SHOW_DETAILS = "tvShowDetailsRoute/{$KEY_TV_SHOW_ID}"

class TvShowNavigationBuilder @Inject constructor() : FeatureNavigationBuilder {
    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.navigation(
            startDestination = ROUTE_TV_SHOWS,
            route = GRAPH_TV_SHOW
        ) {
            composable(route = ROUTE_TV_SHOWS) {
                TvShowsScreen()
            }

            composable(
                route = ROUTE_TV_SHOW_DETAILS,
                arguments = listOf(navArgument(KEY_TV_SHOW_ID) { type = NavType.IntType })
            ) {
                TvDetailsScreen()
            }
        }
    }
}