package com.mutkuensert.movee.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mutkuensert.movee.ui.home.Movies
import com.mutkuensert.movee.ui.moviedetails.MovieDetails
import com.mutkuensert.movee.util.MOVIES
import com.mutkuensert.movee.util.MOVIE_DETAILS

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MOVIES
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
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
            MovieDetails(movieId = backStackEntry.arguments?.getInt("movieId"))
        }
    }
}