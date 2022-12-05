package com.mutkuensert.movee.ui.popularmovies

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mutkuensert.movee.data.MoviesNowPlayingResult
import com.mutkuensert.movee.data.PopularMoviesResult
import com.mutkuensert.movee.util.IMAGE_BASE_URL
import com.mutkuensert.movee.util.POSTER_SIZE_W500

private const val TAG = "PopularMovies Composable"

@Composable
fun PopularMovies(modifier: Modifier = Modifier, viewModel: PopularMoviesViewModel = hiltViewModel(), navigateToMovieDetails: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        MoviesNowPlayingDataObserver(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            viewModel = viewModel,
            navigateToMovieDetails = navigateToMovieDetails)

        Spacer(Modifier.height(10.dp))

        Divider(modifier = Modifier.padding(horizontal = 10.dp), thickness = 1.dp, color = Color.Black)

        Spacer(Modifier.height(10.dp))

        PopularMoviesDataObserver(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            viewModel = viewModel,
            navigateToMovieDetails = navigateToMovieDetails)
    }


}

@Composable
fun MoviesNowPlayingDataObserver(modifier: Modifier = Modifier, viewModel:PopularMoviesViewModel, navigateToMovieDetails: () -> Unit){
    val moviesNowPlayingLazyPagingItems = viewModel.moviesNowPlaying.collectAsLazyPagingItems()

    Row(modifier = modifier,
    verticalAlignment = Alignment.CenterVertically) {

        if(moviesNowPlayingLazyPagingItems.loadState.refresh == LoadState.Loading || moviesNowPlayingLazyPagingItems.loadState.append == LoadState.Loading){

            Column(modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(50.dp))

                CircularProgressIndicator(modifier = Modifier.size(100.dp),
                    strokeWidth = 6.dp,
                    color = Color.Gray)

                Spacer(Modifier.height(50.dp))
            }
        }

        LazyRow(modifier = Modifier.padding(horizontal = 10.dp)){
            items(moviesNowPlayingLazyPagingItems){ item ->
                item?.let { itemNonNull ->
                    MoviesNowPlayingItem(movie = itemNonNull, onClick = { navigateToMovieDetails() })
                }
            }
        }

    }
}

@Composable
fun PopularMoviesDataObserver(modifier: Modifier = Modifier, viewModel: PopularMoviesViewModel, navigateToMovieDetails: () -> Unit){
    val popularMoviesLazyPagingItems = viewModel.popularMovies.collectAsLazyPagingItems()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally) {

        if(popularMoviesLazyPagingItems.loadState.refresh == LoadState.Loading || popularMoviesLazyPagingItems.loadState.append == LoadState.Loading){
            Spacer(Modifier.height(50.dp))

            CircularProgressIndicator(modifier = Modifier.size(100.dp),
                strokeWidth = 6.dp,
            color = Color.Gray)

            Spacer(Modifier.height(50.dp))
        }

        LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)){
            items(popularMoviesLazyPagingItems){ item ->
                item?.let { itemNonNull ->
                    PopularMoviesItem(movie = itemNonNull, onClick = { navigateToMovieDetails() })
                }
            }

        }

    }
}

@Composable
fun MoviesNowPlayingItem(movie: MoviesNowPlayingResult, onClick: () -> Unit){

    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(elevation = 10.dp, modifier = Modifier
            .clickable { onClick() }) {

            Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp), horizontalAlignment = Alignment.CenterHorizontally){

                Card(elevation = 10.dp) {
                    SubcomposeAsyncImage(model = ImageRequest.Builder(LocalContext.current)
                        .data("$IMAGE_BASE_URL$POSTER_SIZE_W500${movie.posterPath}")
                        .crossfade(true)
                        .build(),
                        loading = {
                            CircularProgressIndicator(color = Color.Gray)
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .height(150.dp),
                        contentDescription = "Movie Poster")
                }

                Spacer(Modifier.height(10.dp))

                Text(text = movie.originalTitle,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp)

                Spacer(Modifier.height(10.dp))

                Text(text = movie.voteAverage.toString(),
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold)

            }
        }

    }
}

@Composable
fun PopularMoviesItem(movie: PopularMoviesResult, onClick: () -> Unit){

    Log.i(TAG, "PopularMoviesItem composable image url: $IMAGE_BASE_URL$POSTER_SIZE_W500${movie.posterPath}")

    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(elevation = 10.dp, modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }) {

            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically){

                Card(elevation = 10.dp) {
                    SubcomposeAsyncImage(model = ImageRequest.Builder(LocalContext.current)
                        .data("$IMAGE_BASE_URL$POSTER_SIZE_W500${movie.posterPath}")
                        .crossfade(true)
                        .build(),
                        loading = {
                            CircularProgressIndicator(color = Color.Gray)
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .height(150.dp),
                        contentDescription = "Movie Poster")
                }

                Spacer(Modifier.width(20.dp))

                Column() {

                    Text(text = movie.originalTitle,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp)

                    Spacer(Modifier.height(10.dp))

                    Text(text = movie.voteAverage.toString(),
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold)
                }
            }
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewPopularMoviesItem(){
    PopularMoviesItem(movie = PopularMoviesResult(null, "Title", 0,5.0), onClick = {})
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewMoviesNowPlayingItem(){
    MoviesNowPlayingItem(movie = MoviesNowPlayingResult(null, "Title", 0,5.0), onClick = {})
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewPopularMovies(){
    Column(modifier = Modifier.fillMaxSize()) {
        MoviesNowPlayingItem(movie = MoviesNowPlayingResult(null, "Title", 0,5.0), onClick = {})
        Divider(startIndent = 8.dp, thickness = 1.dp, color = Color.Black)
        PopularMoviesItem(movie = PopularMoviesResult(null, "Title", 0,5.0), onClick = {})
    }
}