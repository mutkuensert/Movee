package movee.presentation.login.login

import android.content.Intent
import android.net.Uri
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
import movee.domain.login.LoginEvent
import movee.presentation.core.showToast
import movee.presentation.login.navigation.getLoginRouteWithEntity
import movee.resources.R

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val loginEvent by viewModel.loginEvent.collectAsStateWithLifecycle(null)
    val requestToken by viewModel.requestToken.collectAsStateWithLifecycle()

    LoginScreen(login = viewModel::login, requestToken = requestToken)

    val context = LocalContext.current

    LaunchedEffect(loginEvent) {
        if (loginEvent == LoginEvent.Login) {
            context.showToast("Logged in successfully.")
        } else if (loginEvent == LoginEvent.Logout) {
            context.showToast("Logged out successfully.")
        }
    }
}

@Composable
private fun LoginScreen(login: () -> Unit, requestToken: String?) {
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
