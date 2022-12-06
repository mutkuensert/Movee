package com.mutkuensert.movee.ui.moviedetails

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mutkuensert.movee.data.MovieDetailsModel
import com.mutkuensert.movee.util.*

@Composable
fun MovieDetails(movieId: Int?, viewModel: MovieDetailsViewModel = hiltViewModel()){

    val data = viewModel.movieDetails.observeAsState()
    if(movieId != null){
        viewModel.getMovieDetails(movieId!!)
        MovieDetailsDataObserver(data = data)
    }
}

@Composable
fun MovieDetailsDataObserver(data: State<Resource<MovieDetailsModel>?>){


    when(data.value?.status){
        Status.STANDBY -> {}

        Status.LOADING -> {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(50.dp))

                CircularProgressIndicator(modifier = Modifier.size(100.dp),
                    strokeWidth = 6.dp,
                    color = Color.Gray)
            }
        }

        Status.SUCCESS -> {
            data.value?.data?.let { movieDetails: MovieDetailsModel ->
                MovieDetailsItem(movieDetails)
            }
        }

        Status.ERROR -> { Toast.makeText(LocalContext.current, "${data.value?.message}", Toast.LENGTH_LONG).show() }
    }
}

@Composable
fun MovieDetailsItem(movieDetails: MovieDetailsModel){
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(bottom = 30.dp)) {

        movieDetails.posterPath?.let { posterPathNonNull ->

            Card(elevation = 10.dp,
            shape = RectangleShape) {
                SubcomposeAsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data("$IMAGE_BASE_URL$POSTER_SIZE_ORIGINAL${posterPathNonNull}")
                    .crossfade(true)
                    .build(),
                    loading = {
                        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(Modifier.height(50.dp))
                            CircularProgressIndicator(color = Color.Gray, modifier = Modifier.size(100.dp))
                            Spacer(Modifier.height(50.dp))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = "Movie Poster")
            }
        }

        Column(modifier = Modifier.padding(horizontal = 15.dp)) {
            Spacer(modifier = Modifier.height(15.dp))

            Text(text = movieDetails.originalTitle,
                color = Color.DarkGray,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp)

            Spacer(modifier = Modifier.height(15.dp))

            Text(text = movieDetails.voteAverage.toString(),
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp)

            Spacer(modifier = Modifier.height(15.dp))

            Row() {
                Text(text = "Runtime(min): ",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp)

                Text(text = movieDetails.runtime.toString(),
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(15.dp))

            Column {
                movieDetails.overview?.let { overviewNonNull ->
                    Text(text = overviewNonNull)
                }
            }
        }
    }
}