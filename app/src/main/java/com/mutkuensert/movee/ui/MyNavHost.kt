package com.mutkuensert.movee.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mutkuensert.movee.ui.moviedetails.MovieDetails
import com.mutkuensert.movee.ui.popularmovies.PopularMovies
import com.mutkuensert.movee.util.POPULAR_MOVIES
import com.mutkuensert.movee.util.MOVIE_DETAILS

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = POPULAR_MOVIES
) {
    NavHost(modifier = modifier,
    navController = navController,
    startDestination = startDestination){
        composable(POPULAR_MOVIES){ PopularMovies(navigateToMovieDetails = { navController.navigate(MOVIE_DETAILS) }) }

        composable(MOVIE_DETAILS){ MovieDetails() }
    }
}