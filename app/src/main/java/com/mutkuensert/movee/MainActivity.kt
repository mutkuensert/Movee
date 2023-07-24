package com.mutkuensert.movee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mutkuensert.movee.feature.MyNavHost
import com.mutkuensert.movee.theme.MoveeTheme
import com.mutkuensert.movee.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val currentDestination = navController.currentDestination?.route
            MoveeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        bottomBar = {
                            MyBottomAppBar(
                                currentDestination = currentDestination,
                                navController = navController
                            )
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
}

@Composable
fun MyBottomAppBar(currentDestination: String?, navController: NavController) {
    BottomNavigation(
        modifier = Modifier.border(width = 1.dp, color = Color.LightGray),
        backgroundColor = Color.White,
        contentColor = Color.DarkGray
    ) {
        BottomNavigationItem(
            selected = currentDestination == MOVIE_NAV_GRAPH,
            onClick = { navController.navigate(MOVIES) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.movie),
                    contentDescription = "Go to movies icon"
                )
            })

        BottomNavigationItem(
            selected = currentDestination == TV_SHOWS_NAV_GRAPH,
            onClick = { navController.navigate(TV_SHOWS) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.tvshow),
                    contentDescription = "Go to tv shows icon"
                )
            })

        BottomNavigationItem(
            selected = currentDestination == MULTI_SEARCH,
            onClick = { navController.navigate(MULTI_SEARCH) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Go to search icon"
                )
            })
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    MoveeTheme {
        MyNavHost()
    }
}