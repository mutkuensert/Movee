package movee.presentation.tvshow.tvshowdetails

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
import movee.domain.tvshow.model.Person
import movee.domain.tvshow.model.TvShowDetails
import movee.presentation.components.Loading
import movee.presentation.components.Poster
import movee.presentation.core.UiState
import movee.presentation.core.setStatusBarAppearanceByDrawable
import movee.presentation.theme.appTypography

@Composable
fun TvDetailsScreen(
    viewModel: TvShowDetailsViewModel = hiltViewModel()
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
        Details(tvShowDetails = details)

        Spacer(modifier = Modifier.height(15.dp))

        Cast(
            cast = cast,
            navigateToPersonDetails = viewModel::navigateToPersonDetails
        )
    }
}

@Composable
private fun Details(modifier: Modifier = Modifier, tvShowDetails: UiState<TvShowDetails>) {
    val context = LocalContext.current

    when (tvShowDetails) {
        is UiState.Empty -> {}
        is UiState.Loading -> Loading()
        is UiState.Success -> {
            val details = tvShowDetails.data

            Column(
                modifier = modifier.padding(bottom = 30.dp)
            ) {
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
                        contentDescription = "Tv Poster",
                        contentScale = ContentScale.FillWidth
                    )
                }

                Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = details.name,
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
                            text = "Seasons: ",
                            style = MaterialTheme.appTypography.h4Bold
                        )

                        Text(
                            text = details.seasonCount.toString(),
                            style = MaterialTheme.appTypography.h4Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Row {
                        Text(
                            text = "Total Episodes: ",
                            style = MaterialTheme.appTypography.h4Bold
                        )

                        Text(
                            text = details.totalEpisodeNumber.toString(),
                            style = MaterialTheme.appTypography.h4Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Column {
                        Text(
                            text = details.overview,
                            style = MaterialTheme.appTypography.bodyMRegular
                        )
                    }
                }
            }
        }
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
                        imageUrl = person.imageUrl,
                        character = person.character,
                        navigateToPersonDetails = { navigateToPersonDetails(person.id) })
                }
            }
        }
    }
}

@Composable
private fun Person(
    modifier: Modifier = Modifier,
    name: String,
    imageUrl: String?,
    character: String,
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