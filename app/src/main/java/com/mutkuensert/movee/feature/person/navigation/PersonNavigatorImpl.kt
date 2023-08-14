package com.mutkuensert.movee.feature.person.navigation

import com.mutkuensert.movee.navigation.NavigationDispatcher
import com.mutkuensert.movee.navigation.NavigationType
import com.mutkuensert.movee.navigation.navigator.PersonNavigator
import javax.inject.Inject

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