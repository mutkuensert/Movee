package movee.presentation.login.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import movee.domain.login.LoginEvent
import movee.presentation.components.Loading
import movee.presentation.core.showToast
import movee.resources.R

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()
    val loginEvent by viewModel.loginEvent.collectAsStateWithLifecycle(null)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars))

        ProfileHeader(userName = uiModel.userName, logout = viewModel::logout)

        Spacer(modifier = Modifier.height(30.dp))

        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uiModel.profilePictureUrl)
                .crossfade(true)
                .build(),
            loading = {
                Loading()
            },
            modifier = Modifier.fillMaxWidth(),
            contentDescription = "Profile Image",
            contentScale = ContentScale.FillWidth
        )
    }

    LaunchedEffect(Unit) { viewModel.getAccountDetails() }

    BackHandler(onBack = viewModel::onBackPressed)

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
private fun ProfileHeader(userName: String, logout: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = userName)

        IconButton(onClick = logout) {
            Icon(painter = painterResource(R.drawable.ic_logout), contentDescription = null)
        }
    }
}