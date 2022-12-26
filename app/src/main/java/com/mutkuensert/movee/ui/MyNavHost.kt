package com.mutkuensert.movee.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.mutkuensert.movee.ui.moviedetails.MovieDetails
import com.mutkuensert.movee.ui.movies.Movies
import com.mutkuensert.movee.ui.multisearch.MultiSearch
import com.mutkuensert.movee.ui.person.Person
import com.mutkuensert.movee.ui.tvdetails.TvDetails
import com.mutkuensert.movee.ui.tvshows.TvShows
import com.mutkuensert.movee.util.*

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MOVIE_NAV_GRAPH
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        navigation(startDestination = MOVIES, route = MOVIE_NAV_GRAPH) {
            composable(MOVIES) {
                Movies() { movieId: Int ->
                    navController.navigate("$MOVIE_DETAILS/$movieId")
                }
            }

            composable(
                "$MOVIE_DETAILS/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                MovieDetails(movieId = backStackEntry.arguments?.getInt("movieId")){ personId ->
                    navController.navigate("$PERSON/$personId")
                }
            }
        }

        navigation(startDestination = TV_SHOWS, route = TV_SHOWS_NAV_GRAPH) {
            composable(TV_SHOWS) {
                TvShows() { tvId ->
                    navController.navigate("$TV_SHOW_DETAILS/$tvId")
                }
            }

            composable(
                "$TV_SHOW_DETAILS/{tvId}",
                arguments = listOf(navArgument("tvId"){type= NavType.IntType})
            ){ backStackEntry ->
                TvDetails(tvId = backStackEntry.arguments?.getInt("tvId"),
                    navigateToPersonDetails = { personId ->
                        navController.navigate("$PERSON/${personId}")
                    })
            }
        }

        composable(
            "$PERSON/{personId}",
        arguments = listOf(navArgument("personId"){type = NavType.IntType})
        ){ backStackEntry ->
            Person(personId = backStackEntry.arguments?.getInt("personId"),
                navigateToMovieDetails = { movieId ->
                    navController.navigate("$MOVIE_DETAILS/$movieId")
            },
                navigateToTvDetails = { tvId ->
                    navController.navigate("$TV_SHOW_DETAILS/$tvId")
                })
        }
        
        
        composable(route = MULTI_SEARCH_ROUTE){
            MultiSearch(
                navigateToMovieDetails = { movieId ->
                                         navController.navigate("$MOVIE_DETAILS/$movieId")
                },
                navigateToTvDetails = { tvId ->
                                      navController.navigate("$TV_SHOW_DETAILS/$tvId")
                },
                navigateToPersonDetails = { personId ->
                    navController.navigate("$PERSON/$personId")
                }
            )
        }
    }
}