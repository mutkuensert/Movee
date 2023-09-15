package movee.presentation.movie.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import movee.presentation.navigation.FeatureNavigationBuilder
import javax.inject.Inject
import movee.presentation.movie.moviedetails.MovieDetailsScreen
import movee.presentation.movie.movies.MoviesScreen

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