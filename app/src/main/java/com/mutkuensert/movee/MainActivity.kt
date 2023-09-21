package com.mutkuensert.movee

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
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

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT

        setContent {
            MoveeTheme {
                HomeScreen(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                    navigationBuilder = navigationBuilder
                )
            }
        }
    }

    private fun plantTimber() {
        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
    }
}