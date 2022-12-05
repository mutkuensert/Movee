package com.mutkuensert.movee.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mutkuensert.movee.ui.moviedetails.MovieDetails
import com.mutkuensert.movee.ui.popularmovies.PopularMovies
import com.mutkuensert.movee.util.HOME
import com.mutkuensert.movee.util.MOVIE_DETAILS

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = HOME
) {
    NavHost(modifier = modifier,
    navController = navController,
    startDestination = startDestination){
        composable(HOME){ PopularMovies(navController = navController) }
        composable(MOVIE_DETAILS){ MovieDetails() }
    }
}