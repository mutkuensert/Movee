package com.mutkuensert.movee.navigation.navigator

import androidx.navigation.NavController

interface MainNavigator {
    var navController: NavController?

    fun navigateToHome()
    fun navigateUp(popUpTo: String? = null, isInclusive: Boolean = false)
    fun navigateBack()
    fun navigateToTab(route: String)
}