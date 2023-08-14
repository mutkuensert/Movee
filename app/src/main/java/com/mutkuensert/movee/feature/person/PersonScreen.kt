package com.mutkuensert.movee.feature.person

import android.widget.Toast
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
import com.mutkuensert.movee.domain.person.model.PersonDetails
import com.mutkuensert.movee.domain.person.model.PersonMovieCast
import com.mutkuensert.movee.domain.person.model.PersonTvCast
import com.mutkuensert.movee.util.IMAGE_BASE_URL
import com.mutkuensert.movee.util.Resource
import com.mutkuensert.movee.util.SIZE_ORIGINAL
import com.mutkuensert.movee.util.Status

@Composable
fun PersonScreen(
    viewModel: PersonViewModel = hiltViewModel()
) {
    val personDetails by viewModel.personDetails.collectAsStateWithLifecycle()
    val personMovieCast by viewModel.personMovieCast.collectAsStateWithLifecycle()
    val personTvCast by viewModel.personTvCast.collectAsStateWithLifecycle()
    val personId = viewModel.personId

    LaunchedEffect(true) {
        viewModel.getPersonDetails(personId)
    }

    if (personDetails.status == Status.SUCCESS) {
        LaunchedEffect(true) {
            viewModel.getPersonCast(personId = personId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        PersonDetails(personDetails = personDetails)

        PersonMovieCast(
            cast = personMovieCast,
            navigateToMovieDetails = viewModel::navigateToMovieDetails
        )

        PersonTvCast(
            cast = personTvCast,
            navigateToTvDetails = viewModel::navigateToTvShowDetails
        )
    }
}

@Composable
private fun Loading(status: Status) {
    val visible = status == Status.LOADING

    if (visible) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(50.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(100.dp),
                strokeWidth = 6.dp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun PersonDetails(personDetails: Resource<PersonDetails>) {
    val readMore = remember { mutableStateOf(false) }

    Column {
        Loading(status = personDetails.status)

        if (personDetails.status == Status.SUCCESS && personDetails.data != null) {
            val profilePath = personDetails.data.profilePath

            if (profilePath != null) {
                Card(
                    elevation = 10.dp,
                    shape = RectangleShape
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("$IMAGE_BASE_URL$SIZE_ORIGINAL$profilePath")
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
            }

            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = personDetails.data.name,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp
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

    val context = LocalContext.current

    LaunchedEffect(personDetails.status) {
        if (personDetails.status == Status.ERROR) {
            Toast.makeText(
                context,
                "${personDetails.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

@Composable
private fun PersonMovieCast(
    cast: Resource<List<PersonMovieCast>>,
    navigateToMovieDetails: (movieId: Int) -> Unit
) {
    Column {
        Loading(status = cast.status)

        if (cast.status == Status.SUCCESS && cast.data != null) {
            cast.data.forEach { item ->
                PersonMovieCastItem(movie = item, navigateToMovieDetails = navigateToMovieDetails)
            }
        }
    }
    val context = LocalContext.current

    LaunchedEffect(cast.status) {
        if (cast.status == Status.ERROR) {
            Toast.makeText(
                context,
                "${cast.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
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
                            .data("$IMAGE_BASE_URL$SIZE_ORIGINAL${movie.posterPath}")
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
private fun PersonTvCast(
    cast: Resource<List<PersonTvCast>>,
    navigateToTvDetails: (tvId: Int) -> Unit
) {
    Column {
        Loading(status = cast.status)

        if (cast.status == Status.SUCCESS && cast.data != null) {
            cast.data.forEach { item ->
                PersonTvCastItem(tv = item, navigateToTvDetails = navigateToTvDetails)
            }
        }
    }
    val context = LocalContext.current

    LaunchedEffect(cast.status) {
        if (cast.status == Status.ERROR) {
            Toast.makeText(
                context,
                "${cast.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
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
                            .data("$IMAGE_BASE_URL$SIZE_ORIGINAL${tv.posterPath}")
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
