package movee.presentation.person.navigation

import javax.inject.Inject
import movee.presentation.navigation.NavigationDispatcher
import movee.presentation.navigation.NavigationType
import movee.presentation.navigation.navigator.PersonNavigator

class PersonNavigatorImpl @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher
) : PersonNavigator {
    override fun navigateToPerson(personId: Int) {
        navigationDispatcher.navigate(
            NavigationType.ToRoute(
                route = ROUTE_PERSON,
                args = listOf(
                    KEY_PERSON_ID to personId
                )
            )
        )
    }
}