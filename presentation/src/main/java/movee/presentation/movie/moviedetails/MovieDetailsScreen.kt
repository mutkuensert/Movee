package movee.presentation.movie.moviedetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.mutkuensert.androidphase.Phase
import com.mutkuensert.phasecomposeextension.Execute
import movee.domain.movie.model.MovieCast
import movee.domain.movie.model.MovieDetails
import movee.presentation.components.Loading
import movee.presentation.core.setStatusBarAppearanceByDrawable
import movee.presentation.core.showToastIfNotNull
import movee.presentation.theme.appTypography

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val movieDetails by viewModel.movieDetails.collectAsStateWithLifecycle()
    val movieCast by viewModel.movieCast.collectAsStateWithLifecycle()

    LaunchedEffect(true) { viewModel.getMovieDetails() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 25.dp)
    ) {
        MovieDetails(
            phase = movieDetails,
            loadCastIfSuccessful = viewModel::getMovieCast
        )

        Spacer(modifier = Modifier.height(15.dp))

        MovieCast(
            phase = movieCast,
            navigateToPersonDetails = viewModel::navigateToPerson
        )
    }
}

@Composable
private fun MovieDetails(
    phase: Phase<MovieDetails>,
    loadCastIfSuccessful: () -> Unit
) {
    phase.Execute(
        onLoading = { Loading() },
        onSuccess = {
            MovieDetailsItem(movieDetails = it)
            LaunchedEffect(Unit) { loadCastIfSuccessful() }
        },
        onError = { LocalContext.current.showToastIfNotNull(it.message) }
    )
}

@Composable
private fun MovieCast(
    phase: Phase<List<MovieCast>>,
    navigateToPersonDetails: (personId: Int) -> Unit
) {
    phase.Execute(
        onLoading = { Loading() },
        onSuccess = {
            LazyRow {
                items(it) { item ->
                    MovieCastItem(
                        cast = item,
                        navigateToPersonDetails = { navigateToPersonDetails(item.id) })
                }
            }
        },
        onError = { LocalContext.current.showToastIfNotNull(it.message) }
    )
}

@Composable
private fun MovieDetailsItem(movieDetails: MovieDetails) {
    val context = LocalContext.current

    if (movieDetails.imageUrl != null) {
        Card(
            elevation = 10.dp,
            shape = RectangleShape
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movieDetails.imageUrl)
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
                contentDescription = "Movie Poster",
                contentScale = ContentScale.FillWidth
            )
        }
    }

    Column(modifier = Modifier.padding(horizontal = 15.dp)) {
        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = movieDetails.title,
            style = MaterialTheme.appTypography.showDetailShowTitle
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = movieDetails.voteAverage.toString(),
            style = MaterialTheme.appTypography.showDetailVoteAverage
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row {
            Text(
                text = "Runtime(min): ",
                style = MaterialTheme.appTypography.showDetailRuntime
            )

            Text(
                text = movieDetails.runtime.toString(),
                style = MaterialTheme.appTypography.showDetailRuntime
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Column {
            if (movieDetails.overview != null) {
                Text(text = movieDetails.overview!!)
            }
        }
    }

}


@Composable
private fun MovieCastItem(cast: MovieCast, navigateToPersonDetails: () -> Unit) {
    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(elevation = 10.dp, modifier = Modifier
            .clickable { navigateToPersonDetails() }
        ) {
            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Card(elevation = 10.dp) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(cast.imageUrl)
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
                            .height(150.dp),
                        contentDescription = "Movie Poster"
                    )
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = cast.name,
                    style = MaterialTheme.appTypography.showDetailCastName
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = cast.character,
                    style = MaterialTheme.appTypography.showDetailCharacterName
                )
            }
        }
    }
}