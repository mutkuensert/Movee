package com.mutkuensert.movee.ui.tvdetails

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.mutkuensert.movee.data.model.remote.tvshows.TvDetailsModel
import com.mutkuensert.movee.util.IMAGE_BASE_URL
import com.mutkuensert.movee.util.SIZE_ORIGINAL
import com.mutkuensert.movee.util.Resource
import com.mutkuensert.movee.util.Status

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun TvDetails(tvId: Int?, viewModel: TvDetailsViewModel = hiltViewModel()) {

    val data = viewModel.tvDetails.collectAsStateWithLifecycle()
    if (tvId != null) {
        viewModel.getTvDetails(tvId!!)
        TvDetailsDataObserver(data = data)
    }
}

@Composable
fun TvDetailsDataObserver(data: State<Resource<TvDetailsModel>>) {

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
            data.value.data?.let { tvDetails: TvDetailsModel ->
                TvDetailsItem(tvDetails)
            }
        }

        Status.ERROR -> {
            Toast.makeText(LocalContext.current, "${data.value.message}", Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun TvDetailsItem(tvDetails: TvDetailsModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 30.dp)
    ) {

        tvDetails.posterPath?.let { posterPathNonNull ->

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
                text = tvDetails.name,
                color = Color.DarkGray,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = tvDetails.voteAverage.toString(),
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row() {
                Text(
                    text = "Seasons: ",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    text = tvDetails.seasons.size.toString(),
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row() {
                Text(
                    text = "Episodes: ",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    text = tvDetails.seasons.map { it.episode_count }.sum().toString(),
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Column {
                Text(text = tvDetails.overview)
            }
        }
    }
}