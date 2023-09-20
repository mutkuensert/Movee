package movee.presentation.person

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mutkuensert.androidphase.Phase
import com.mutkuensert.phasecomposeextension.Execute
import movee.domain.person.model.PersonDetails
import movee.domain.person.model.PersonMovieCast
import movee.domain.person.model.PersonTvCast
import movee.presentation.components.Loading
import movee.presentation.core.showToastIfNotNull

@Composable
fun PersonScreen(
    viewModel: PersonViewModel = hiltViewModel()
) {
    val details by viewModel.details.collectAsStateWithLifecycle()
    val movieCasting by viewModel.movieCasting.collectAsStateWithLifecycle()
    val tvCasting by viewModel.tvCasting.collectAsStateWithLifecycle()

    LaunchedEffect(true) { viewModel.getPersonDetails() }

    details.Execute(
        onSuccess = {
            LaunchedEffect(true) { viewModel.getCasting() }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        PersonDetails(phase = details)

        PersonMovieCasting(
            phase = movieCasting,
            navigateToMovieDetails = viewModel::navigateToMovieDetails
        )

        PersonTvCasting(
            phase = tvCasting,
            navigateToTvDetails = viewModel::navigateToTvShowDetails
        )
    }
}

@Composable
private fun PersonDetails(phase: Phase<PersonDetails>) {
    val readMore = remember { mutableStateOf(false) }

    phase.Execute(
        onLoading = {
            Loading()
        },
        onSuccess = {
            Card(
                elevation = 10.dp,
                shape = RectangleShape
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it.imageUrl)
                        .crossfade(true)
                        .build(),
                    loading = {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(Modifier.height(50.dp))

                            CircularProgressIndicator(
                                color = Color.Gray,
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(Modifier.height(50.dp))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = "Tv Poster",
                    contentScale = ContentScale.FillWidth
                )
            }

            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = it.name,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp
                )

                Spacer(modifier = Modifier.height(15.dp))

                if (readMore.value) {
                    Text(text = it.biography)

                    Text(
                        text = "... read less",
                        modifier = Modifier.clickable { readMore.value = false })
                } else {
                    Text(text = it.biography, maxLines = 4)

                    Text(
                        text = "... read more",
                        modifier = Modifier.clickable { readMore.value = true })
                }

                Spacer(modifier = Modifier.height(15.dp))

            }
        },
        onError = { LocalContext.current.showToastIfNotNull(it.message) })
}

@Composable
private fun PersonMovieCasting(
    phase: Phase<List<PersonMovieCast>>,
    navigateToMovieDetails: (movieId: Int) -> Unit
) {
    phase.Execute(
        onLoading = { Loading() },
        onSuccess = {
            it.forEach { item ->
                PersonMovieCastItem(movie = item, navigateToMovieDetails = navigateToMovieDetails)
            }
        },
        onError = { LocalContext.current.showToastIfNotNull(it.message) })
}

@Composable
private fun PersonMovieCastItem(
    movie: PersonMovieCast,
    navigateToMovieDetails: (movieId: Int) -> Unit
) {
    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(elevation = 10.dp, modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToMovieDetails(movie.id) }) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(elevation = 10.dp) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(movie.imageUrl)
                            .crossfade(true)
                            .build(),
                        loading = {
                            CircularProgressIndicator(
                                color = Color.Gray,
                                modifier = Modifier.size(150.dp)
                            )
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .width(150.dp),
                        contentDescription = "Movie Poster"
                    )
                }

                Spacer(Modifier.width(20.dp))

                Text(
                    text = movie.title,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
private fun PersonTvCasting(
    phase: Phase<List<PersonTvCast>>,
    navigateToTvDetails: (tvId: Int) -> Unit
) {
    phase.Execute(
        onLoading = { Loading() },
        onSuccess = {
            it.forEach { item ->
                PersonTvCastItem(
                    tv = item,
                    navigateToTvDetails = navigateToTvDetails
                )
            }
        },
        onError = { LocalContext.current.showToastIfNotNull(it.message) })
}

@Composable
private fun PersonTvCastItem(tv: PersonTvCast, navigateToTvDetails: (tvId: Int) -> Unit) {
    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(elevation = 10.dp, modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToTvDetails(tv.id) }) {

            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Card(elevation = 10.dp) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(tv.imageUrl)
                            .crossfade(true)
                            .build(),
                        loading = {
                            CircularProgressIndicator(
                                color = Color.Gray,
                                modifier = Modifier.size(150.dp)
                            )
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .width(150.dp),
                        contentDescription = "Movie Poster"
                    )
                }

                Spacer(Modifier.width(20.dp))

                Text(
                    text = tv.name,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
            }
        }
    }
}
