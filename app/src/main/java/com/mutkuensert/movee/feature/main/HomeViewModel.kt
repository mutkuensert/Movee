package com.mutkuensert.movee.feature.main

import androidx.lifecycle.ViewModel
import com.mutkuensert.movee.navigation.navigator.MainNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainNavigator: MainNavigator,
) : ViewModel() {
    fun navigateToMovie() {
        mainNavigator.navigateToTab(route = Tabs.Movie.route)
    }

    fun navigateToTvShow() {
        mainNavigator.navigateToTab(route = Tabs.TvShow.route)
    }

    fun navigateToMultiSearch() {
        mainNavigator.navigateToTab(route = Tabs.MultiSearch.route)
    }

    fun navigateToLogin() {
        mainNavigator.navigateToTab(route = Tabs.Login.route)
    }
}