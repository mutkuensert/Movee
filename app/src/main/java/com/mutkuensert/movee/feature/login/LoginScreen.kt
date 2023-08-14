package com.mutkuensert.movee.feature.login

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mutkuensert.movee.R
import com.mutkuensert.movee.feature.login.navigation.getLoginRouteWithEntity
import com.mutkuensert.movee.util.APP_DEEP_LINK

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val loginEvent by viewModel.loginEvent.collectAsStateWithLifecycle(null)
    val requestToken by viewModel.requestToken.collectAsStateWithLifecycle()

    if (isLoggedIn != null) {
        if (!isLoggedIn!!) {
            LoggedOutScreen(login = viewModel::login, requestToken = requestToken)
        } else {
            LoggedInScreen(logout = viewModel::logout)
        }
    }

    val context = LocalContext.current

    LaunchedEffect(loginEvent) {
        if (loginEvent == LoginEvent.Login) {
            Toast.makeText(context, "Logged in successfully.", Toast.LENGTH_SHORT).show()
        } else if (loginEvent == LoginEvent.Logout) {
            Toast.makeText(context, "Logged out successfully.", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun LoggedInScreen(logout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = logout) {
            Icon(painter = painterResource(R.drawable.ic_logout), contentDescription = null)
        }
    }
}

@Composable
private fun LoggedOutScreen(login: () -> Unit, requestToken: String?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = login) {
            Icon(painter = painterResource(R.drawable.ic_login), contentDescription = null)
        }
    }

    DirectToLoginPage(requestToken = requestToken)
}

@Composable
private fun DirectToLoginPage(requestToken: String?) {
    val context = LocalContext.current

    LaunchedEffect(requestToken) {
        if (requestToken != null) {
            val intent = CustomTabsIntent.Builder()
                .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
                .build()

            intent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            var uri =
                Uri.parse(
                    "https://www.themoviedb.org/authenticate/" +
                            requestToken +
                            "?redirect_to" +
                            "=$APP_DEEP_LINK${getLoginRouteWithEntity(true)}"
                )
            if (uri.scheme == null) {
                uri = uri
                    .buildUpon()
                    .scheme("https")
                    .build()
            }

            intent.launchUrl(context, uri)
        }
    }
}
