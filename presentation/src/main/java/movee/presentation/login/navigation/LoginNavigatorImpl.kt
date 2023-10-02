package movee.presentation.login.navigation

import javax.inject.Inject
import movee.presentation.navigation.NavigationDispatcher
import movee.presentation.navigation.NavigationType
import movee.presentation.navigation.navigator.LoginNavigator

class LoginNavigatorImpl @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher
) : LoginNavigator {
    override fun navigateToProfileScreen() {
        navigationDispatcher.navigate(
            NavigationType.ToRoute(
                route = ROUTE_PROFILE
            )
        )
    }
}