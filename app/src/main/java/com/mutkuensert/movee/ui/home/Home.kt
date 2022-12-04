package com.mutkuensert.movee.ui.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mutkuensert.movee.data.PopularMoviesResult
import com.mutkuensert.movee.util.IMAGE_BASE_URL
import com.mutkuensert.movee.util.POSTER_SIZE_W500

private const val TAG = "Home Composable"

@Composable
fun Home(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    HomeDataObserver(viewModel = viewModel)
}

@Composable
fun HomeDataObserver(modifier: Modifier = Modifier, viewModel: HomeViewModel){
    val lazyPagingItems = viewModel.flow.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        LazyColumn{
            if(lazyPagingItems.loadState.refresh == LoadState.Loading || lazyPagingItems.loadState.append == LoadState.Loading){
                item {
                    CircularProgressIndicator(modifier = Modifier.size(100.dp),
                        strokeWidth = 6.dp)
                }
            }

            itemsIndexed(lazyPagingItems){ index, item ->
                item?.let { itemNonNull ->
                    PopularMoviesItem(movie = itemNonNull)
                }
            }

        }

    }
}

@Composable
fun PopularMoviesItem(movie: PopularMoviesResult){

    Log.i(TAG, "PopularMoviesItem composable image url: $IMAGE_BASE_URL$POSTER_SIZE_W500${movie.posterPath}")

    Row(modifier = Modifier.padding(10.dp)) {
        Card(elevation = 10.dp, modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()) {

            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally){

                Card(elevation = 10.dp) {
                    SubcomposeAsyncImage(model = ImageRequest.Builder(LocalContext.current)
                        .data("$IMAGE_BASE_URL$POSTER_SIZE_W500${movie.posterPath}")
                        .crossfade(true)
                        .build(),
                        loading = {
                            CircularProgressIndicator()
                        },
                        modifier = Modifier.clip(RoundedCornerShape(5.dp)),
                        contentDescription = "Movie Poster")
                }

                Spacer(Modifier.height(10.dp))

                Row(modifier = Modifier.padding(top = 10.dp)) {
                    Text(text = movie.originalTitle)

                    Spacer(Modifier.width(10.dp))

                    Text(text = movie.voteAverage.toString())
                }
            }
        }

    }
}