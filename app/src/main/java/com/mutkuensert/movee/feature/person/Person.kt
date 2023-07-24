package com.mutkuensert.movee.feature.person

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.mutkuensert.movee.data.person.model.PersonDetailsModel
import com.mutkuensert.movee.data.person.model.PersonMovieCastModel
import com.mutkuensert.movee.data.person.model.PersonTvCastModel
import com.mutkuensert.movee.util.IMAGE_BASE_URL
import com.mutkuensert.movee.util.SIZE_ORIGINAL
import com.mutkuensert.movee.util.Status

private const val TAG = "Person screen composable"

@Composable
fun Person(
    personId: Int?,
    viewModel: PersonViewModel = hiltViewModel(),
    navigateToMovieDetails: (movieId: Int) -> Unit,
    navigateToTvDetails: (tvId: Int) -> Unit
) {
    val personDetails = viewModel.personDetails.collectAsStateWithLifecycle()
    val personMovieCast = viewModel.personMovieCast.collectAsStateWithLifecycle()
    val personTvCast = viewModel.personTvCast.collectAsStateWithLifecycle()

    if (personId != null) {
        LaunchedEffect(key1 = true) {
            Log.i(TAG, "viewModel.getPersonDetails(personId = personId!!)")
            viewModel.getPersonDetails(personId)
        }
        if (personDetails.value.status == Status.SUCCESS) {
            LaunchedEffect(key1 = personDetails) {
                viewModel.getPersonCast(personId = personId)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            //Person Details Section

            item {
                if (personDetails.value.status == Status.LOADING) {
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

            item {
                if (personDetails.value.status == Status.SUCCESS) {
                    personDetails.value.data?.let { personDetails ->
                        PersonDetailsItem(personDetails = personDetails)
                    }
                }
            }

            item {
                if (personDetails.value.status == Status.ERROR) {
                    Toast.makeText(
                        LocalContext.current,
                        "${personDetails.value.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            //Person Movie Cast Section

            item {
                if (personMovieCast.value.status == Status.LOADING) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
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

            if (personMovieCast.value.status == Status.SUCCESS && personMovieCast.value.data != null) {
                items(personMovieCast.value.data!!) { personMovieCastModel ->
                    PersonMovieCastItem(
                        movie = personMovieCastModel,
                        navigateToMovieDetails = navigateToMovieDetails
                    )
                }
            }

            item {
                if (personMovieCast.value.status == Status.ERROR) {
                    Toast.makeText(
                        LocalContext.current,
                        "${personMovieCast.value.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }


            //Person Tv Cast Section


            item {
                if (personTvCast.value.status == Status.LOADING) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
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

            if (personTvCast.value.status == Status.SUCCESS && personTvCast.value.data != null) {
                items(personTvCast.value.data!!) { personTvCastModel ->
                    PersonTvCastItem(
                        tv = personTvCastModel,
                        navigateToTvDetails = navigateToTvDetails
                    )
                }
            }

            item {
                if (personTvCast.value.status == Status.ERROR) {
                    Toast.makeText(
                        LocalContext.current,
                        "${personTvCast.value.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }
}

@Composable
fun PersonDetailsItem(personDetails: PersonDetailsModel) {
    val readMore = remember { mutableStateOf(false) }

    personDetails.profilePath?.let { posterPathNonNull ->
        Card(
            elevation = 10.dp,
            shape = RectangleShape
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("$IMAGE_BASE_URL$SIZE_ORIGINAL${posterPathNonNull}")
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
            text = personDetails.name,
            color = Color.DarkGray,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        if (readMore.value) {
            Text(text = personDetails.biography)
            Text(text = "... read less", modifier = Modifier.clickable { readMore.value = false })
        } else {
            Text(text = personDetails.biography, maxLines = 4)
            Text(text = "... read more", modifier = Modifier.clickable { readMore.value = true })
        }



        Spacer(modifier = Modifier.height(15.dp))

    }

}

@Composable
fun PersonMovieCastItem(
    movie: PersonMovieCastModel,
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
fun PersonTvCastItem(tv: PersonTvCastModel, navigateToTvDetails: (tvId: Int) -> Unit) {

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
