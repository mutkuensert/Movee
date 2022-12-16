package com.mutkuensert.movee.ui.person

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mutkuensert.movee.data.model.remote.person.PersonDetailsModel
import com.mutkuensert.movee.data.model.remote.person.PersonMovieCastModel
import com.mutkuensert.movee.util.*

private const val TAG = "Person screen composable"

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun Person(personId: Int?, viewModel: PersonViewModel = hiltViewModel(), navigateToMovieDetails: (movieId: Int) -> Unit){
    val personDetails = viewModel.personDetails.collectAsStateWithLifecycle()
    val personCast = viewModel.personCast.collectAsStateWithLifecycle()

    if(personId != null){
        viewModel.getPersonDetails(personId!!)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 25.dp)
        ){
            PersonDetailsDataObserver(data = personDetails, loadCastIfSuccessful = { viewModel.getPersonCast(personId=personId!!) })
            Spacer(modifier = Modifier.height(15.dp))
            PersonCastDataObserver(data = personCast, modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp))
        }

    }
}

@Composable
fun PersonDetailsDataObserver(data: State<Resource<PersonDetailsModel>>, loadCastIfSuccessful: () -> Unit) {

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
            data.value.data?.let { personDetails: PersonDetailsModel ->
                PersonDetailsItem(personDetails = personDetails)
                loadCastIfSuccessful()
            }
        }

        Status.ERROR -> {
            Toast.makeText(LocalContext.current, "${data.value.message}", Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun PersonDetailsItem(personDetails: PersonDetailsModel) {

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

        Text(text = personDetails.biography)

        Spacer(modifier = Modifier.height(15.dp))

    }

}

@Composable
fun PersonCastDataObserver(data: State<Resource<List<PersonMovieCastModel>>>, modifier: Modifier = Modifier){
    Column(modifier = modifier) {
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
                data.value.data?.let { castList: List<PersonMovieCastModel> ->
                    LazyColumn {
                        items(castList){ personMovieCastModel: PersonMovieCastModel ->
                            PersonCastItem(movie = personMovieCastModel)
                        }
                    }
                }

            }

            Status.ERROR -> {
                Toast.makeText(LocalContext.current, "${data.value.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


}

@Composable
fun PersonCastItem(movie: PersonMovieCastModel){
    Log.i(TAG, "PopularMoviesItem composable image url: $IMAGE_BASE_URL$POSTER_SIZE_W500${movie.posterPath}")

    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(elevation = 10.dp, modifier = Modifier
            .fillMaxWidth()
            .clickable { }) {

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
                            CircularProgressIndicator(color = Color.Gray, modifier = Modifier.size(150.dp))
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