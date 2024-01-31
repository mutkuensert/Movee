package movee.presentation.login.navigation

import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import javax.inject.Inject
import movee.presentation.login.login.APP_DEEP_LINK
import movee.presentation.login.login.LoginScreen
import movee.presentation.login.profile.ProfileScreen
import movee.presentation.navigation.FeatureNavigationBuilder

const val KEY_IS_REDIRECTED_FROM_TMDB_LOGIN = "isRedirectedFromTmdbLogin"

const val ROUTE_LOGIN = "loginRoute/{$KEY_IS_REDIRECTED_FROM_TMDB_LOGIN}"
const val GRAPH_LOGIN = "graphLogin"
const val ROUTE_PROFILE = "profileRoute"

fun getLoginRoute(isRedirectedFromTmdbLogin: Boolean) =
    ROUTE_LOGIN.replace(
        KEY_IS_REDIRECTED_FROM_TMDB_LOGIN,
        isRedirectedFromTmdbLogin.toString()
    )

class LoginNavigationBuilder @Inject constructor() : FeatureNavigationBuilder {
    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.navigation(startDestination = ROUTE_LOGIN, route = GRAPH_LOGIN) {
            composable(
                route = ROUTE_LOGIN,
                arguments = listOf(navArgument(KEY_IS_REDIRECTED_FROM_TMDB_LOGIN) {
                    nullable = true
                }),
                deepLinks = listOf(NavDeepLink(APP_DEEP_LINK + ROUTE_LOGIN))
            ) {
                LoginScreen()
            }

            composable(
                route = ROUTE_PROFILE,
            ) {
                ProfileScreen()
            }
        }
    }
}