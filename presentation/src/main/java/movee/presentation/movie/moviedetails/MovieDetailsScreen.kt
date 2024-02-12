package movee.presentation.movie.moviedetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import movee.domain.movie.model.MovieDetails
import movee.domain.movie.model.Person
import movee.presentation.components.Loading
import movee.presentation.components.Poster
import movee.presentation.core.UiState
import movee.presentation.core.setStatusBarAppearanceByDrawable
import movee.presentation.theme.appTypography

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val details by viewModel.details.collectAsStateWithLifecycle()
    val cast by viewModel.cast.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.getDetails() }
    LaunchedEffect(details) { if (details is UiState.Success) viewModel.getCast() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 25.dp)
    ) {
        Details(movieDetails = details)

        Spacer(modifier = Modifier.height(15.dp))

        Cast(
            cast = cast,
            navigateToPersonDetails = viewModel::navigateToPerson
        )
    }
}

@Composable
private fun Cast(
    cast: UiState<List<Person>>,
    navigateToPersonDetails: (personId: Int) -> Unit
) {
    when (cast) {
        is UiState.Empty -> {}
        is UiState.Loading -> Loading()
        is UiState.Success -> {
            LazyRow(contentPadding = PaddingValues(horizontal = 10.dp)) {
                items(cast.data.size) { index ->
                    val person = cast.data[index]

                    Person(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        name = person.name,
                        character = person.character,
                        imageUrl = person.imageUrl,
                        navigateToPersonDetails = { navigateToPersonDetails(person.id) })
                }
            }
        }
    }
}

@Composable
private fun Details(modifier: Modifier = Modifier, movieDetails: UiState<MovieDetails>) {
    val context = LocalContext.current

    when (movieDetails) {
        is UiState.Empty -> {}
        is UiState.Loading -> Loading()
        is UiState.Success -> {
            val details = movieDetails.data

            Column(modifier = modifier) {
                if (details.imageUrl != null) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(details.imageUrl)
                            .allowHardware(false)
                            .crossfade(true)
                            .build(),
                        loading = {
                            Loading()
                        },
                        onSuccess = { result ->
                            context.setStatusBarAppearanceByDrawable(drawable = result.result.drawable)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 3.dp),
                        contentDescription = "Poster",
                        contentScale = ContentScale.FillWidth
                    )
                }

                Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = details.title,
                        style = MaterialTheme.appTypography.h2Bold
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = details.voteAverage.toString(),
                        style = MaterialTheme.appTypography.h4Bold
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Row {
                        Text(
                            text = "Runtime(min): ",
                            style = MaterialTheme.appTypography.h4Bold
                        )

                        Text(
                            text = details.runtime.toString(),
                            style = MaterialTheme.appTypography.h4Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Column {
                        if (details.overview != null) {
                            Text(
                                text = details.overview!!,
                                style = MaterialTheme.appTypography.bodyMRegular
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun Person(
    modifier: Modifier = Modifier,
    name: String,
    character: String,
    imageUrl: String?,
    navigateToPersonDetails: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = navigateToPersonDetails),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Poster(
            modifier = Modifier.padding(5.dp),
            posterUrl = imageUrl
        )

        Spacer(Modifier.width(10.dp))

        Text(
            text = name,
            style = MaterialTheme.appTypography.bodyLBold
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = character,
            style = MaterialTheme.appTypography.bodySRegular
        )
    }
}