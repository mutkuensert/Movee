package movee.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import javax.inject.Inject
import movee.presentation.navigation.navigator.MainNavigator
import timber.log.Timber

class NavigationBuilder @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    private val builders: Set<@JvmSuppressWildcards FeatureNavigationBuilder>,
    private val mainNavigator: MainNavigator,
) {
    fun buildNavGraph(navGraphBuilder: NavGraphBuilder) {
        builders.forEach {
            it.build(navGraphBuilder)
        }
    }

    @Composable
    fun ObserveNavigation(navController: NavHostController) {
        mainNavigator.navController = navController

        LaunchedEffect(navController) {
            navigationDispatcher.commands.collect {
                when (it) {
                    is NavigationType.Back -> navController.popBackStack()
                    is NavigationType.Up -> navigateUp(navController, it)
                    is NavigationType.ToRoute -> navController.navigate(it.getRoute())
                    is NavigationType.ToTab -> navigateToTab(navController, it.route)
                }
            }
        }
    }

    private fun navigateToTab(navController: NavHostController, route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    private fun navigateUp(
        navController: NavHostController,
        command: NavigationType.Up
    ) {
        if (command.route != null) {
            val startDestination = navController.graph.startDestinationRoute
            val isPopped = navController.popBackStack(command.route, command.isInclusive)

            if (!isPopped && startDestination != null) {
                Timber.e(
                    "Back stack could not be popped. " +
                            "The user has been navigated to start destination route."
                )

                navController.navigate(startDestination)
            }
        } else {
            navController.navigateUp()
        }
    }
}