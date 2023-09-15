package movee.presentation.person.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import movee.presentation.person.PersonScreen
import javax.inject.Inject
import movee.presentation.navigation.FeatureNavigationBuilder

const val KEY_PERSON_ID = "personId"
const val ROUTE_PERSON = "personRoute/{$KEY_PERSON_ID}"

class PersonNavigationBuilder @Inject constructor() : FeatureNavigationBuilder {
    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable(
            route = ROUTE_PERSON,
            arguments = listOf(navArgument(KEY_PERSON_ID) { type = NavType.IntType })
        ) {
            PersonScreen()
        }
    }
}