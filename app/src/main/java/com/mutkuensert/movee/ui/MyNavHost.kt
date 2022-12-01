package com.mutkuensert.movee.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mutkuensert.movee.ui.home.Home
import com.mutkuensert.movee.util.MOVEE_HOME

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MOVEE_HOME
) {
    NavHost(modifier = modifier,
    navController = navController,
    startDestination = startDestination){
        composable(MOVEE_HOME){ Home()}
    }
}