package com.mutkuensert.movee.feature

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDeepLink
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.mutkuensert.movee.feature.login.LoginScreen
import com.mutkuensert.movee.feature.movie.moviedetails.MovieDetailsScreen
import com.mutkuensert.movee.feature.movie.movies.MoviesScreen
import com.mutkuensert.movee.feature.multisearch.MultiSearchScreen
import com.mutkuensert.movee.feature.person.PersonScreen
import com.mutkuensert.movee.feature.tvshow.tvshowdetails.TvDetailsScreen
import com.mutkuensert.movee.feature.tvshow.tvshows.TvShowsScreen
import com.mutkuensert.movee.util.APP_DEEP_LINK
import com.mutkuensert.movee.util.NavConstants

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavConstants.Movie.GRAPH_MOVIE
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        navigation(
            startDestination = NavConstants.Movie.ROUTE_MOVIES,
            route = NavConstants.Movie.GRAPH_MOVIE
        ) {
            composable(NavConstants.Movie.ROUTE_MOVIES) {
                MoviesScreen() { movieId: Int ->
                    navController.navigate("${NavConstants.Movie.ROUTE_MOVIE_DETAILS}/$movieId")
                }
            }

            composable(
                "${NavConstants.Movie.ROUTE_MOVIE_DETAILS}/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                MovieDetailsScreen(movieId = backStackEntry.arguments?.getInt("movieId")) { personId ->
                    navController.navigate("${NavConstants.Person.ROUTE_PERSON}/$personId")
                }
            }
        }

        navigation(
            startDestination = NavConstants.TvShow.ROUTE_TV_SHOWS,
            route = NavConstants.TvShow.GRAPH_TV_SHOWS
        ) {
            composable(NavConstants.TvShow.ROUTE_TV_SHOWS) {
                TvShowsScreen() { tvId ->
                    navController.navigate("${NavConstants.TvShow.ROUTE_TV_SHOW_DETAILS}/$tvId")
                }
            }

            composable(
                "${NavConstants.TvShow.ROUTE_TV_SHOW_DETAILS}/{tvId}",
                arguments = listOf(navArgument("tvId") { type = NavType.IntType })
            ) { backStackEntry ->
                TvDetailsScreen(tvId = backStackEntry.arguments?.getInt("tvId"),
                    navigateToPersonDetails = { personId ->
                        navController.navigate("${NavConstants.Person.ROUTE_PERSON}/${personId}")
                    })
            }
        }

        composable(
            "${NavConstants.Person.ROUTE_PERSON}/{personId}",
            arguments = listOf(navArgument("personId") { type = NavType.IntType })
        ) { backStackEntry ->
            PersonScreen(personId = backStackEntry.arguments?.getInt("personId"),
                navigateToMovieDetails = { movieId ->
                    navController.navigate("${NavConstants.Movie.ROUTE_MOVIE_DETAILS}/$movieId")
                },
                navigateToTvDetails = { tvId ->
                    navController.navigate("${NavConstants.TvShow.ROUTE_TV_SHOW_DETAILS}/$tvId")
                })
        }


        composable(route = NavConstants.Search.ROUTE_MULTI_SEARCH) {
            MultiSearchScreen(
                navigateToMovieDetails = { movieId ->
                    navController.navigate("${NavConstants.Movie.ROUTE_MOVIE_DETAILS}/$movieId")
                },
                navigateToTvDetails = { tvId ->
                    navController.navigate("${NavConstants.TvShow.ROUTE_TV_SHOW_DETAILS}/$tvId")
                },
                navigateToPersonDetails = { personId ->
                    navController.navigate("${NavConstants.Person.ROUTE_PERSON}/$personId")
                }
            )
        }

        composable(
            route = NavConstants.Login.ROUTE_LOGIN,
            arguments = listOf(navArgument(NavConstants.Login.KEY_IS_REDIRECTED_FROM_TMDB_LOGIN) {
                nullable = true
            }),
            deepLinks = listOf(NavDeepLink(APP_DEEP_LINK + NavConstants.Login.ROUTE_LOGIN))
        ) {
            LoginScreen()
        }
    }
}