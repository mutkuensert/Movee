package movee.presentation.login.navigation

import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import movee.presentation.navigation.FeatureNavigationBuilder
import javax.inject.Inject
import movee.presentation.login.APP_DEEP_LINK
import movee.presentation.login.LoginScreen

const val KEY_IS_REDIRECTED_FROM_TMDB_LOGIN = "isRedirectedFromTmdbLogin"

const val ROUTE_LOGIN = "loginRoute/{$KEY_IS_REDIRECTED_FROM_TMDB_LOGIN}"

fun getLoginRouteWithEntity(isRedirectedFromTmdbLogin: Boolean) =
    ROUTE_LOGIN.replace(
        KEY_IS_REDIRECTED_FROM_TMDB_LOGIN,
        isRedirectedFromTmdbLogin.toString()
    )

class LoginNavigationBuilder @Inject constructor() : FeatureNavigationBuilder {
    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable(
            route = ROUTE_LOGIN,
            arguments = listOf(navArgument(KEY_IS_REDIRECTED_FROM_TMDB_LOGIN) {
                nullable = true
            }),
            deepLinks = listOf(NavDeepLink(APP_DEEP_LINK + ROUTE_LOGIN))
        ) {
            LoginScreen()
        }
    }
}