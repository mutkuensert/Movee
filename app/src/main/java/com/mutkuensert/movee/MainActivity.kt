package com.mutkuensert.movee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import movee.presentation.main.HomeScreen
import movee.presentation.navigation.NavigationBuilder
import movee.presentation.theme.MoveeTheme
import timber.log.Timber
import timber.log.Timber.Forest.plant

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigationBuilder: NavigationBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        plantTimber()

        setContent {
            MoveeTheme {
                HomeScreen(navigationBuilder)
            }
        }
    }

    private fun plantTimber() {
        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
    }
}