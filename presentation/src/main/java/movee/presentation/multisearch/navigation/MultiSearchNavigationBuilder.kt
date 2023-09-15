package movee.presentation.multisearch.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import movee.presentation.multisearch.MultiSearchScreen
import movee.presentation.navigation.FeatureNavigationBuilder
import javax.inject.Inject

const val ROUTE_MULTI_SEARCH = "multiSearchRoute"

class MultiSearchNavigationBuilder @Inject constructor() : FeatureNavigationBuilder {
    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable(route = ROUTE_MULTI_SEARCH) {
            MultiSearchScreen()
        }
    }
}