package movee.presentation.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import movee.presentation.movie.navigation.GRAPH_MOVIE
import movee.presentation.navigation.NavigationBuilder
import movee.presentation.theme.AppColors
import movee.resources.R

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigationBuilder: NavigationBuilder
) {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<HomeViewModel>()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomNavBar(
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

    navigationBuilder.ObserveNavigation(navController = navController)
}

@Composable
private fun BottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavController, viewModel: HomeViewModel
) {
    val lastTabItemRoute by navController.currentTabDestinationAsState(
        TabItem.all().map { it.route })

    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color.White,
        contentColor = Color.DarkGray
    ) {
        BottomNavigationItem(
            selected = lastTabItemRoute == TabItem.Movie.route,
            unselectedContentColor = Color.Gray,
            selectedContentColor = AppColors.DarkCyan,
            onClick = viewModel::navigateToMovie,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_movie),
                    contentDescription = null
                )
            })

        BottomNavigationItem(
            selected = lastTabItemRoute == TabItem.TvShow.route,
            unselectedContentColor = Color.Gray,
            selectedContentColor = AppColors.DarkCyan,
            onClick = viewModel::navigateToTvShow,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tvshow),
                    contentDescription = null
                )
            })

        BottomNavigationItem(
            selected = lastTabItemRoute == TabItem.MultiSearch.route,
            unselectedContentColor = Color.Gray,
            selectedContentColor = AppColors.DarkCyan,
            onClick = viewModel::navigateToMultiSearch,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null
                )
            })

        BottomNavigationItem(
            selected = lastTabItemRoute == TabItem.Login.route,
            unselectedContentColor = Color.Gray,
            selectedContentColor = AppColors.DarkCyan,
            onClick = viewModel::navigateToLogin,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_account),
                    contentDescription = null
                )
            })
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun NavController.currentTabDestinationAsState(tabDestinations: List<String>): State<String?> {
    val destination = remember { mutableStateOf<String?>(null) }

    DisposableEffect(this) {
        val listener =
            NavController.OnDestinationChangedListener { _, _, _ ->
                var latestItem: String? = null
                val reversedBackStackDestinations = currentBackStack.value.reversed()
                    .map { it.destination.route }
                val backStackDestinations =
                    listOf(currentDestination?.route) + reversedBackStackDestinations

                for (route in backStackDestinations) {
                    val tabDestination =
                        tabDestinations.firstOrNull { it == route }

                    if (tabDestination != null) {
                        latestItem = tabDestination
                        break
                    }
                }

                destination.value = latestItem
            }

        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return destination
}
