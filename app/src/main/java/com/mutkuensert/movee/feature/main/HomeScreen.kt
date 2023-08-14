package com.mutkuensert.movee.feature.main

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mutkuensert.movee.R
import com.mutkuensert.movee.feature.login.navigation.ROUTE_LOGIN
import com.mutkuensert.movee.feature.movie.navigation.GRAPH_MOVIE
import com.mutkuensert.movee.feature.multisearch.navigation.ROUTE_MULTI_SEARCH
import com.mutkuensert.movee.feature.tvshow.navigation.GRAPH_TV_SHOW
import com.mutkuensert.movee.navigation.NavigationBuilder
import com.mutkuensert.movee.theme.AppColors

@Composable
fun HomeScreen(navigationBuilder: NavigationBuilder) {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<HomeViewModel>()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            bottomBar = {
                MyBottomAppBar(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        ) { padding ->
            NavHost(
                modifier = Modifier.padding(padding),
                navController = navController,
                startDestination = GRAPH_MOVIE,
                builder = navigationBuilder::buildNavGraph
            )
        }
    }

    navigationBuilder.ObserveNavigation(navController = navController)
}

@Composable
private fun MyBottomAppBar(navController: NavController, viewModel: HomeViewModel) {
    val currentRoute by navController.currentParentRouteAsState()

    BottomNavigation(
        modifier = Modifier.border(width = 1.dp, color = Color.LightGray),
        backgroundColor = Color.White,
        contentColor = Color.DarkGray
    ) {
        BottomNavigationItem(
            selected = currentRoute == GRAPH_MOVIE,
            unselectedContentColor = Color.Gray,
            selectedContentColor = AppColors.DarkCyan,
            onClick = viewModel::navigateToMovie,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_movie),
                    contentDescription = "Go to movies button icon"
                )
            })

        BottomNavigationItem(
            selected = currentRoute == GRAPH_TV_SHOW,
            unselectedContentColor = Color.Gray,
            selectedContentColor = AppColors.DarkCyan,
            onClick = viewModel::navigateToTvShow,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tvshow),
                    contentDescription = "Go to tv shows button icon"
                )
            })

        BottomNavigationItem(
            selected = currentRoute == ROUTE_MULTI_SEARCH,
            unselectedContentColor = Color.Gray,
            selectedContentColor = AppColors.DarkCyan,
            onClick = viewModel::navigateToMultiSearch,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Go to search button icon"
                )
            })

        BottomNavigationItem(
            selected = currentRoute == ROUTE_LOGIN,
            unselectedContentColor = Color.Gray,
            selectedContentColor = AppColors.DarkCyan,
            onClick = viewModel::navigateToLogin,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_account),
                    contentDescription = "Go to account button icon"
                )
            })
    }
}

@Composable
private fun NavController.currentParentRouteAsState(): State<String?> {
    val destination = remember { mutableStateOf<String?>(null) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { navController, _, _ ->
            destination.value = navController.currentDestination?.parent?.route
                ?: navController.currentDestination?.route
        }

        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return destination
}