package movee.presentation.multisearch.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import javax.inject.Inject
import movee.presentation.multisearch.MultiSearchScreen
import movee.presentation.navigation.FeatureNavigationBuilder

const val ROUTE_MULTI_SEARCH = "multiSearchRoute"
const val GRAPH_MULTI_SEARCH = "multiSearchGraph"

class MultiSearchNavigationBuilder @Inject constructor() : FeatureNavigationBuilder {
    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.navigation(
            startDestination = ROUTE_MULTI_SEARCH,
            route = GRAPH_MULTI_SEARCH
        ) {
            composable(route = ROUTE_MULTI_SEARCH) {
                MultiSearchScreen()
            }
        }
    }
}