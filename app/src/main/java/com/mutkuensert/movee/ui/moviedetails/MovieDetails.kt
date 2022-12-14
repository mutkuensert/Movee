package com.mutkuensert.movee.ui.moviedetails

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mutkuensert.movee.data.model.remote.movies.MovieDetailsModel
import com.mutkuensert.movee.data.model.remote.movies.credits.MovieCast
import com.mutkuensert.movee.util.*

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun MovieDetails(movieId: Int?, viewModel: MovieDetailsViewModel = hiltViewModel()) {

    val movieDetails = viewModel.movieDetails.collectAsStateWithLifecycle()
    val movieCast = viewModel.movieCast.collectAsStateWithLifecycle()

    if (movieId != null) {
        viewModel.getMovieDetails(movieId!!)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 25.dp)
        ) {
            MovieDetailsDataObserver(
                data = movieDetails,
            loadCastIfSuccessful = { viewModel.getMovieCast(movieId!!) })
            Spacer(modifier = Modifier.height(15.dp))
            MovieCastDataObserver(data = movieCast)
            
        }
    }
}

@Composable
fun MovieDetailsDataObserver(data: State<Resource<MovieDetailsModel>>, loadCastIfSuccessful: () -> Unit) {

    when (data.value.status) {
        Status.STANDBY -> {}

        Status.LOADING -> {
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

        Status.SUCCESS -> {
            data.value.data?.let { movieDetails: MovieDetailsModel ->
                MovieDetailsItem(movieDetails)
                loadCastIfSuccessful()
            }
        }

        Status.ERROR -> {
            Toast.makeText(LocalContext.current, "${data.value.message}", Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun MovieCastDataObserver(data: State<Resource<List<MovieCast>>>) {

    when (data.value.status) {
        Status.STANDBY -> {}

        Status.LOADING -> {
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

        Status.SUCCESS -> {
            data.value.data?.let { movieCast: List<MovieCast> ->
                LazyRow{
                    items(movieCast){ item ->
                        MovieCastItem(cast = item)
                    }
                }
            }
        }

        Status.ERROR -> {
            Toast.makeText(LocalContext.current, "${data.value.message}", Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun MovieDetailsItem(movieDetails: MovieDetailsModel) {

    movieDetails.posterPath?.let { posterPathNonNull ->

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
                contentDescription = "Movie Poster",
                contentScale = ContentScale.FillWidth
            )
        }
    }

    Column(modifier = Modifier.padding(horizontal = 15.dp)) {
        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = movieDetails.title,
            color = Color.DarkGray,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = movieDetails.voteAverage.toString(),
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row() {
            Text(
                text = "Runtime(min): ",
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = movieDetails.runtime.toString(),
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Column {
            movieDetails.overview?.let { overviewNonNull ->
                Text(text = overviewNonNull)
            }
        }
    }

}


@Composable
fun MovieCastItem(cast: MovieCast){
    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(elevation = 10.dp, modifier = Modifier
            //.clickable { onClick() }
        ) {

            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Card(elevation = 10.dp) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("$IMAGE_BASE_URL$SIZE_ORIGINAL${cast.profilePath}")
                            .crossfade(true)
                            .build(),
                        loading = {
                            CircularProgressIndicator(color = Color.Gray)
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
                    color = Color.DarkGray,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = cast.character,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )

            }
        }

    }
}