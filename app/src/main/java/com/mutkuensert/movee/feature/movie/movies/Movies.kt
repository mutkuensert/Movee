package com.mutkuensert.movee.feature.movie.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mutkuensert.movee.data.movie.model.MoviesNowPlayingResult
import com.mutkuensert.movee.data.movie.model.PopularMoviesResult
import com.mutkuensert.movee.util.IMAGE_BASE_URL
import com.mutkuensert.movee.util.POSTER_SIZE_W500
import kotlin.math.roundToInt

@Composable
fun Movies(
    viewModel: MoviesViewModel = hiltViewModel(),
    navigateToMovieDetails: (movieId: Int) -> Unit
) {
    val moviesNowPlayingLazyPagingItems = viewModel.moviesNowPlaying.collectAsLazyPagingItems()
    val popularMoviesLazyPagingItems = viewModel.popularMovies.collectAsLazyPagingItems()

    val stateOfMoviesNowPlaying = rememberLazyListState()
    val stateOfPopularMovies = rememberLazyListState()

    val localDensity = LocalDensity.current

    val itemsAboveHeight = remember { mutableStateOf(0.dp) }
    var itemsAboveHeightPx by remember { mutableFloatStateOf(0f) }
    var itemsAboveOffsetHeightPx by remember { mutableFloatStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = itemsAboveOffsetHeightPx + delta
                itemsAboveOffsetHeightPx = newOffset.coerceIn(-itemsAboveHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        PopularMovies(
            popularMovies = popularMoviesLazyPagingItems,
            lazyListState = stateOfPopularMovies,
            itemsAboveHeight = itemsAboveHeight,
            navigateToMovieDetails = navigateToMovieDetails
        )

        Card(elevation = 10.dp,
            modifier = Modifier
                .onSizeChanged {
                    itemsAboveHeightPx = it.height.toFloat()
                    itemsAboveHeight.value = with(localDensity) { it.height.toDp() }
                }
                .offset { IntOffset(x = 0, y = itemsAboveOffsetHeightPx.roundToInt()) }
                .background(Color.White)) {
            Column {
                Spacer(Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = "Movies in Theaters",
                    color = Color.LightGray,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(10.dp))

                MoviesNowPlaying(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    moviesNowPlaying = moviesNowPlayingLazyPagingItems,
                    navigateToMovieDetails = navigateToMovieDetails,
                    lazyListState = stateOfMoviesNowPlaying
                )

                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun MoviesNowPlaying(
    modifier: Modifier = Modifier,
    moviesNowPlaying: LazyPagingItems<MoviesNowPlayingResult>,
    lazyListState: LazyListState,
    navigateToMovieDetails: (movieId: Int) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (moviesNowPlaying.loadState.refresh == LoadState.Loading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(50.dp))

                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                    strokeWidth = 6.dp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(50.dp))
            }
            Spacer(Modifier.width(50.dp))
        }

        LazyRow(state = lazyListState) {
            items(count = moviesNowPlaying.itemCount) { index ->
                val item = moviesNowPlaying[index]

                if (item != null) {
                    MoviesNowPlayingItem(
                        movie = item,
                        navigateToMovieDetails = { navigateToMovieDetails(item.id) })
                }
            }
        }


        if (moviesNowPlaying.loadState.append == LoadState.Loading) {
            Spacer(Modifier.width(50.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(50.dp))

                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                    strokeWidth = 6.dp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}

@Composable
fun PopularMovies(
    modifier: Modifier = Modifier,
    popularMovies: LazyPagingItems<PopularMoviesResult>,
    lazyListState: LazyListState,
    itemsAboveHeight: MutableState<Dp>,
    navigateToMovieDetails: (movieId: Int) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = lazyListState,
            contentPadding = PaddingValues(top = itemsAboveHeight.value)
        ) {
            item {
                Spacer(Modifier.height(10.dp))
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {

                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = "Popular Movies",
                        color = Color.LightGray,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )

                    Spacer(Modifier.height(10.dp))
                }
            }

            item {
                if (popularMovies.loadState.refresh == LoadState.Loading) {
                    Spacer(Modifier.height(50.dp))

                    CircularProgressIndicator(
                        modifier = Modifier.size(100.dp),
                        strokeWidth = 6.dp,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(50.dp))
                }
            }

            items(count = popularMovies.itemCount) { index ->
                val item = popularMovies[index]

                if (item != null) {
                    PopularMoviesItem(
                        movie = item,
                        navigateToMovieDetails = { navigateToMovieDetails(item.id) })
                }
            }

            item {
                if (popularMovies.loadState.append == LoadState.Loading) {
                    Spacer(Modifier.height(50.dp))

                    CircularProgressIndicator(
                        modifier = Modifier.size(100.dp),
                        strokeWidth = 6.dp,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(50.dp))
                }
            }
        }
    }
}

@Composable
fun MoviesNowPlayingItem(movie: MoviesNowPlayingResult, navigateToMovieDetails: () -> Unit) {
    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(elevation = 10.dp, modifier = Modifier.clickable(onClick = navigateToMovieDetails)) {
            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(elevation = 10.dp) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("$IMAGE_BASE_URL$POSTER_SIZE_W500${movie.posterPath}")
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
                    text = movie.title,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = movie.voteAverage.toString(),
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PopularMoviesItem(movie: PopularMoviesResult, navigateToMovieDetails: () -> Unit) {
    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(
            elevation = 10.dp, modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = navigateToMovieDetails)
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Card(elevation = 10.dp) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("$IMAGE_BASE_URL$POSTER_SIZE_W500${movie.posterPath}")
                            .crossfade(true)
                            .build(),
                        loading = {
                            CircularProgressIndicator(color = Color.Gray)
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .width(150.dp),
                        contentDescription = "Movie Poster"
                    )
                }

                Spacer(Modifier.width(20.dp))

                Column {
                    Text(
                        text = movie.title,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = movie.voteAverage.toString(),
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewPopularMoviesItem() {
    PopularMoviesItem(
        movie = PopularMoviesResult(null, "Title", 0, 5.0),
        navigateToMovieDetails = {})
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewMoviesNowPlayingItem() {
    MoviesNowPlayingItem(
        movie = MoviesNowPlayingResult(null, "Title", 0, 5.0),
        navigateToMovieDetails = {})
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewPopularMovies() {
    Column(modifier = Modifier.fillMaxSize()) {
        MoviesNowPlayingItem(
            movie = MoviesNowPlayingResult(null, "Title", 0, 5.0),
            navigateToMovieDetails = {})
        Divider(startIndent = 8.dp, thickness = 1.dp, color = Color.Black)
        PopularMoviesItem(
            movie = PopularMoviesResult(null, "Title", 0, 5.0),
            navigateToMovieDetails = {})
    }
}