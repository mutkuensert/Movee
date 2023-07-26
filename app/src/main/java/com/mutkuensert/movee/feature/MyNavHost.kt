package com.mutkuensert.movee.feature

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.mutkuensert.movee.feature.login.Login
import com.mutkuensert.movee.feature.movie.moviedetails.MovieDetails
import com.mutkuensert.movee.feature.movie.movies.Movies
import com.mutkuensert.movee.feature.multisearch.MultiSearch
import com.mutkuensert.movee.feature.person.Person
import com.mutkuensert.movee.feature.tvshow.tvdetails.TvDetails
import com.mutkuensert.movee.feature.tvshow.tvshows.TvShows
import com.mutkuensert.movee.util.GRAPH_MOVIE
import com.mutkuensert.movee.util.GRAPH_TV_SHOWS
import com.mutkuensert.movee.util.ROUTE_LOGIN
import com.mutkuensert.movee.util.ROUTE_MOVIES
import com.mutkuensert.movee.util.ROUTE_MOVIE_DETAILS
import com.mutkuensert.movee.util.ROUTE_MULTI_SEARCH
import com.mutkuensert.movee.util.ROUTE_PERSON
import com.mutkuensert.movee.util.ROUTE_TV_SHOWS
import com.mutkuensert.movee.util.ROUTE_TV_SHOW_DETAILS

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = GRAPH_MOVIE
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        navigation(startDestination = ROUTE_MOVIES, route = GRAPH_MOVIE) {
            composable(ROUTE_MOVIES) {
                Movies() { movieId: Int ->
                    navController.navigate("$ROUTE_MOVIE_DETAILS/$movieId")
                }
            }

            composable(
                "$ROUTE_MOVIE_DETAILS/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                MovieDetails(movieId = backStackEntry.arguments?.getInt("movieId")) { personId ->
                    navController.navigate("$ROUTE_PERSON/$personId")
                }
            }
        }

        navigation(startDestination = ROUTE_TV_SHOWS, route = GRAPH_TV_SHOWS) {
            composable(ROUTE_TV_SHOWS) {
                TvShows() { tvId ->
                    navController.navigate("$ROUTE_TV_SHOW_DETAILS/$tvId")
                }
            }

            composable(
                "$ROUTE_TV_SHOW_DETAILS/{tvId}",
                arguments = listOf(navArgument("tvId") { type = NavType.IntType })
            ) { backStackEntry ->
                TvDetails(tvId = backStackEntry.arguments?.getInt("tvId"),
                    navigateToPersonDetails = { personId ->
                        navController.navigate("$ROUTE_PERSON/${personId}")
                    })
            }
        }

        composable(
            "$ROUTE_PERSON/{personId}",
            arguments = listOf(navArgument("personId") { type = NavType.IntType })
        ) { backStackEntry ->
            Person(personId = backStackEntry.arguments?.getInt("personId"),
                navigateToMovieDetails = { movieId ->
                    navController.navigate("$ROUTE_MOVIE_DETAILS/$movieId")
                },
                navigateToTvDetails = { tvId ->
                    navController.navigate("$ROUTE_TV_SHOW_DETAILS/$tvId")
                })
        }


        composable(route = ROUTE_MULTI_SEARCH) {
            MultiSearch(
                navigateToMovieDetails = { movieId ->
                    navController.navigate("$ROUTE_MOVIE_DETAILS/$movieId")
                },
                navigateToTvDetails = { tvId ->
                    navController.navigate("$ROUTE_TV_SHOW_DETAILS/$tvId")
                },
                navigateToPersonDetails = { personId ->
                    navController.navigate("$ROUTE_PERSON/$personId")
                }
            )
        }

        composable(route = ROUTE_LOGIN) {
            Login()
        }
    }
}