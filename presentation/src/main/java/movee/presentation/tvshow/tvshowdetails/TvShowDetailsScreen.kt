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
import androidx.compose.foundation.lazy.items
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
import com.mutkuensert.androidphase.Phase
import com.mutkuensert.phasecomposeextension.Execute
import movee.domain.tvshow.model.Person
import movee.domain.tvshow.model.TvShowDetails
import movee.presentation.components.Loading
import movee.presentation.components.Poster
import movee.presentation.core.setStatusBarAppearanceByDrawable
import movee.presentation.core.showToastIfNotNull
import movee.presentation.theme.appTypography

@Composable
fun TvDetailsScreen(
    viewModel: TvShowDetailsViewModel = hiltViewModel()
) {
    val details by viewModel.details.collectAsStateWithLifecycle()
    val cast by viewModel.cast.collectAsStateWithLifecycle()

    LaunchedEffect(true) { viewModel.getDetails() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 25.dp)
    ) {
        OnPhaseDetails(
            phase = details,
            getCast = viewModel::getCast
        )

        Spacer(modifier = Modifier.height(15.dp))

        OnPhaseCast(
            phase = cast,
            navigateToPersonDetails = viewModel::navigateToPersonDetails
        )
    }
}

@Composable
private fun OnPhaseDetails(
    phase: Phase<TvShowDetails>,
    getCast: () -> Unit
) {
    phase.Execute(
        onLoading = { Loading() },
        onSuccess = {
            Details(tvDetails = it)
            LaunchedEffect(Unit) { getCast() }
        },
        onError = { LocalContext.current.showToastIfNotNull(it.message) })
}

@Composable
private fun Details(modifier: Modifier = Modifier, tvDetails: TvShowDetails) {
    val context = LocalContext.current

    Column(
        modifier = modifier.padding(bottom = 30.dp)
    ) {
        if (tvDetails.imageUrl != null) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(tvDetails.imageUrl)
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
                text = tvDetails.name,
                style = MaterialTheme.appTypography.h2Bold
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = tvDetails.voteAverage.toString(),
                style = MaterialTheme.appTypography.h4Bold
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row {
                Text(
                    text = "Seasons: ",
                    style = MaterialTheme.appTypography.h4Bold
                )

                Text(
                    text = tvDetails.seasonCount.toString(),
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
                    text = tvDetails.totalEpisodeNumber.toString(),
                    style = MaterialTheme.appTypography.h4Bold
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Column {
                Text(
                    text = tvDetails.overview,
                    style = MaterialTheme.appTypography.bodyMRegular
                )
            }
        }
    }
}

@Composable
private fun OnPhaseCast(
    phase: Phase<List<Person>>,
    navigateToPersonDetails: (personId: Int) -> Unit
) {
    phase.Execute(
        onLoading = { Loading() },
        onSuccess = {
            LazyRow(contentPadding = PaddingValues(horizontal = 10.dp)) {
                items(it) { item ->
                    Person(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        person = item,
                        navigateToPersonDetails = { navigateToPersonDetails(item.id) })
                }
            }
        },
        onError = { LocalContext.current.showToastIfNotNull(it.message) })
}

@Composable
private fun Person(
    modifier: Modifier = Modifier,
    person: Person,
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
            posterUrl = person.imageUrl
        )

        Spacer(Modifier.width(10.dp))

        Text(
            text = person.name,
            style = MaterialTheme.appTypography.bodyLBold
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = person.character,
            style = MaterialTheme.appTypography.bodySRegular
        )
    }
}