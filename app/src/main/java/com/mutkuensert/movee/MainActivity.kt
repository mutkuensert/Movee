package com.mutkuensert.movee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mutkuensert.movee.feature.MyNavHost
import com.mutkuensert.movee.theme.AppColors
import com.mutkuensert.movee.theme.MoveeTheme
import com.mutkuensert.movee.util.GRAPH_MOVIE
import com.mutkuensert.movee.util.GRAPH_TV_SHOWS
import com.mutkuensert.movee.util.ROUTE_LOGIN
import com.mutkuensert.movee.util.ROUTE_MOVIES
import com.mutkuensert.movee.util.ROUTE_MULTI_SEARCH
import com.mutkuensert.movee.util.ROUTE_TV_SHOWS
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.Forest.plant


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        plantTimber()

        setContent {
            val navController = rememberNavController()
            MoveeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        bottomBar = {
                            MyBottomAppBar(navController = navController)
                        }
                    ) { padding ->
                        MyNavHost(
                            modifier = Modifier.padding(padding),
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    private fun plantTimber() {
        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
    }
}

@Composable
private fun MyBottomAppBar(navController: NavController) {
    val currentRoute = navController.currentParentRouteAsState().value

    BottomNavigation(
        modifier = Modifier.border(width = 1.dp, color = Color.LightGray),
        backgroundColor = Color.White,
        contentColor = Color.DarkGray
    ) {
        BottomNavigationItem(
            selected = currentRoute == GRAPH_MOVIE,
            unselectedContentColor = Color.Gray,
            selectedContentColor = AppColors.DarkCyan,
            onClick = { navController.navigate(ROUTE_MOVIES) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_movie),
                    contentDescription = "Go to movies button icon"
                )
            })

        BottomNavigationItem(
            selected = currentRoute == GRAPH_TV_SHOWS,
            unselectedContentColor = Color.Gray,
            selectedContentColor = AppColors.DarkCyan,
            onClick = { navController.navigate(ROUTE_TV_SHOWS) },
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
            onClick = { navController.navigate(ROUTE_MULTI_SEARCH) },
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
            onClick = { navController.navigate(ROUTE_LOGIN) },
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    MoveeTheme {
        MyNavHost()
    }
}