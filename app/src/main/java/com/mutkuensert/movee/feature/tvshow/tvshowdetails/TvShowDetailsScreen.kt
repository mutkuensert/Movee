package com.mutkuensert.movee.feature.tvshow.tvshowdetails

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mutkuensert.movee.domain.tvshow.model.TvShowCast
import com.mutkuensert.movee.domain.tvshow.model.TvShowDetails
import com.mutkuensert.movee.domain.util.Resource
import com.mutkuensert.movee.domain.util.Status

@Composable
fun TvDetailsScreen(
    viewModel: TvShowDetailsViewModel = hiltViewModel()
) {
    val tvShowDetails by viewModel.tvShowDetails.collectAsStateWithLifecycle()
    val tvCast by viewModel.tvCast.collectAsStateWithLifecycle()
    val tvShowId = viewModel.tvShowId

    LaunchedEffect(true) { viewModel.getTvDetails(tvShowId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 25.dp)
    ) {
        TvDetailsDataObserver(
            data = tvShowDetails,
            loadTvCastIfSuccessful = { viewModel.getTvShowCast(tvShowId = tvShowId) })

        Spacer(modifier = Modifier.height(15.dp))

        TvShowsCastDataObserver(
            data = tvCast,
            navigateToPersonDetails = viewModel::navigateToPersonDetails
        )
    }
}

@Composable
private fun TvDetailsDataObserver(
    data: Resource<TvShowDetails>,
    loadTvCastIfSuccessful: () -> Unit
) {
    when (data.status) {
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
            if (data.data != null) {
                TvDetailsItem(data.data)

                loadTvCastIfSuccessful()
            }
        }

        Status.ERROR -> {
            Toast.makeText(LocalContext.current, "${data.message}", Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
private fun TvDetailsItem(tvDetails: TvShowDetails) {
    Column(
        modifier = Modifier
            .padding(bottom = 30.dp)
    ) {
        if (tvDetails.imageUrl != null) {
            Card(
                elevation = 10.dp,
                shape = RectangleShape
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(tvDetails.imageUrl)
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

            Row {
                Text(
                    text = "Seasons: ",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    text = tvDetails.totalEpisodeNumber.toString(),
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row {
                Text(
                    text = "Episodes: ",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    text = tvDetails.totalEpisodeNumber.toString(),
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

@Composable
private fun TvShowsCastDataObserver(
    data: Resource<List<TvShowCast>>,
    navigateToPersonDetails: (personId: Int) -> Unit
) {
    when (data.status) {
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
            if (data.data != null) {
                LazyRow {
                    items(data.data) { item ->
                        TvShowCastItem(
                            cast = item,
                            navigateToPersonDetails = { navigateToPersonDetails(item.id) })
                    }
                }
            }
        }

        Status.ERROR -> {
            Toast.makeText(LocalContext.current, "${data.message}", Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
private fun TvShowCastItem(cast: TvShowCast, navigateToPersonDetails: () -> Unit) {
    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(
            elevation = 10.dp, modifier = Modifier
                .clickable(onClick = navigateToPersonDetails)
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
                            CircularProgressIndicator(color = Color.Gray)
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .height(150.dp),
                        contentDescription = "Tv Show Poster"
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