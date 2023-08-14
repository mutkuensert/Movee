package com.mutkuensert.movee.feature.person.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mutkuensert.movee.feature.person.PersonScreen
import com.mutkuensert.movee.navigation.FeatureNavigationBuilder
import javax.inject.Inject

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