package com.mutkuensert.movee.feature.movie.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.mutkuensert.movee.feature.movie.moviedetails.MovieDetailsScreen
import com.mutkuensert.movee.feature.movie.movies.MoviesScreen
import com.mutkuensert.movee.navigation.FeatureNavigationBuilder
import javax.inject.Inject

const val KEY_MOVIE_ID = "movieId"
const val ROUTE_MOVIE_DETAILS = "movieDetailsRoute/{$KEY_MOVIE_ID}"
const val ROUTE_MOVIES = "moviesRoute"
const val GRAPH_MOVIE = "graphMovieRoute"

class MovieNavigationBuilder @Inject constructor() : FeatureNavigationBuilder {
    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.navigation(
            startDestination = ROUTE_MOVIES,
            route = GRAPH_MOVIE
        ) {
            composable(route = ROUTE_MOVIES) {
                MoviesScreen()
            }

            composable(
                route = ROUTE_MOVIE_DETAILS,
                arguments = listOf(navArgument(KEY_MOVIE_ID) { type = NavType.IntType }
                )
            ) {
                MovieDetailsScreen()
            }
        }
    }
}