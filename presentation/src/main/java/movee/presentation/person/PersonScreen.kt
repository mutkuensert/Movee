package movee.presentation.person

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import movee.domain.person.model.PersonDetails
import movee.domain.person.model.PersonMovieCasting
import movee.domain.person.model.PersonTvCasting
import movee.presentation.components.Loading
import movee.presentation.core.UiState
import movee.presentation.core.setStatusBarAppearanceByDrawable
import movee.presentation.theme.appTypography

@Composable
fun PersonScreen(
    viewModel: PersonViewModel = hiltViewModel()
) {
    val details by viewModel.details.collectAsStateWithLifecycle()
    val movieCasting by viewModel.movieCasting.collectAsStateWithLifecycle()
    val tvCasting by viewModel.tvCasting.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.getPersonDetails() }
    LaunchedEffect(details) { if (details is UiState.Success) viewModel.getCasting() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Details(personDetails = details)

        MovieCasting(
            movieCasting = movieCasting,
            navigateToMovieDetails = viewModel::navigateToMovieDetails
        )

        TvCasting(
            tvCasting = tvCasting,
            navigateToTvDetails = viewModel::navigateToTvShowDetails
        )
    }
}

@Composable
private fun Details(personDetails: UiState<PersonDetails>) {
    val readMore = remember { mutableStateOf(false) }
    val context = LocalContext.current

    when (personDetails) {
        is UiState.Empty -> {}
        is UiState.Loading -> Loading()
        is UiState.Success -> {
            Card(
                elevation = 10.dp,
                shape = RectangleShape
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(personDetails.data.imageUrl)
                        .allowHardware(false)
                        .crossfade(true)
                        .build(),
                    loading = {
                        Loading()
                    },
                    onSuccess = { result ->
                        context.setStatusBarAppearanceByDrawable(drawable = result.result.drawable)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = "Tv Poster",
                    contentScale = ContentScale.FillWidth
                )
            }

            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = personDetails.data.name,
                    style = MaterialTheme.appTypography.bodyMRegular
                )

                Spacer(modifier = Modifier.height(15.dp))

                if (readMore.value) {
                    Text(text = personDetails.data.biography)

                    Text(
                        text = "... read less",
                        modifier = Modifier.clickable { readMore.value = false })
                } else {
                    Text(text = personDetails.data.biography, maxLines = 4)

                    Text(
                        text = "... read more",
                        modifier = Modifier.clickable { readMore.value = true })
                }

                Spacer(modifier = Modifier.height(15.dp))

            }
        }
    }
}

@Composable
private fun MovieCasting(
    movieCasting: UiState<List<PersonMovieCasting>>,
    navigateToMovieDetails: (movieId: Int) -> Unit
) {
    when (movieCasting) {
        is UiState.Empty -> {}
        is UiState.Loading -> Loading()
        is UiState.Success -> {
            Column {
                movieCasting.data.forEach { item ->
                    Casting(
                        id = item.id,
                        title = item.title,
                        character = item.character,
                        imageUrl = item.imageUrl,
                        navigateToDetails = navigateToMovieDetails,
                    )
                }
            }
        }
    }
}

@Composable
private fun Casting(
    id: Int,
    title: String,
    character: String,
    imageUrl: String?,
    navigateToDetails: (movieId: Int) -> Unit
) {
    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(elevation = 10.dp, modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToDetails(id) }) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(elevation = 10.dp) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color.Gray)
                            }
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .width(150.dp),
                        contentDescription = "Movie Poster"
                    )
                }

                Spacer(Modifier.width(20.dp))

                Text(
                    text = title,
                    style = MaterialTheme.appTypography.h3Bold
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = character,
                    style = MaterialTheme.appTypography.h3Bold
                )
            }
        }
    }
}

@Composable
private fun TvCasting(
    tvCasting: UiState<List<PersonTvCasting>>,
    navigateToTvDetails: (tvId: Int) -> Unit
) {
    when (tvCasting) {
        is UiState.Empty -> {}
        is UiState.Loading -> Loading()
        is UiState.Success -> {
            Column {
                tvCasting.data.forEach { item ->
                    Casting(
                        id = item.id,
                        title = item.name,
                        character = item.character,
                        imageUrl = item.imageUrl,
                        navigateToDetails = navigateToTvDetails
                    )
                }
            }
        }
    }
}