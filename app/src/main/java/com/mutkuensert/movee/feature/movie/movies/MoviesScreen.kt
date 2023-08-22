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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mutkuensert.movee.R
import com.mutkuensert.movee.core.LoadingIfAppend
import com.mutkuensert.movee.core.LoadingIfRefresh
import com.mutkuensert.movee.domain.movie.model.MovieNowPlaying
import com.mutkuensert.movee.domain.movie.model.PopularMovie
import kotlin.math.roundToInt

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val moviesNowPlayingLazyPagingItems = viewModel.moviesNowPlaying.collectAsLazyPagingItems()
    val popularMovies = viewModel.popularMovies.collectAsLazyPagingItems()

    val stateOfMoviesNowPlaying = rememberLazyListState()

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
            popularMovies = popularMovies,
            itemsAboveHeight = itemsAboveHeight,
            navigateToMovieDetails = viewModel::navigateToMovieDetails,
            onAddToFavorite = viewModel::addMovieToFavorites
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
                    navigateToMovieDetails = viewModel::navigateToMovieDetails,
                    lazyListState = stateOfMoviesNowPlaying,
                    onAddToFavorite = viewModel::addMovieToFavorites
                )

                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun MoviesNowPlaying(
    modifier: Modifier = Modifier,
    moviesNowPlaying: LazyPagingItems<MovieNowPlaying>,
    lazyListState: LazyListState,
    navigateToMovieDetails: (movieId: Int) -> Unit,
    onAddToFavorite: (isFavorite: Boolean, movieId: Int) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        moviesNowPlaying.loadState.LoadingIfRefresh()

        LazyRow(state = lazyListState) {
            items(count = moviesNowPlaying.itemCount) { index ->
                val item = moviesNowPlaying[index]

                if (item != null) {
                    MoviesNowPlayingItem(
                        movie = item,
                        navigateToMovieDetails = { navigateToMovieDetails(item.id) },
                        onAddToFavorite = onAddToFavorite
                    )
                }
            }
        }

        moviesNowPlaying.loadState.LoadingIfAppend()
    }
}

@Composable
private fun PopularMovies(
    modifier: Modifier = Modifier,
    popularMovies: LazyPagingItems<PopularMovie>,
    itemsAboveHeight: MutableState<Dp>,
    navigateToMovieDetails: (movieId: Int) -> Unit,
    onAddToFavorite: (isFavorite: Boolean, movieId: Int) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
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
                popularMovies.loadState.LoadingIfRefresh()
            }

            items(count = popularMovies.itemCount) { index ->
                val item = popularMovies[index]

                if (item != null) {
                    PopularMoviesItem(
                        movie = item,
                        navigateToMovieDetails = { navigateToMovieDetails(item.id) },
                        onAddToFavorite = onAddToFavorite
                    )
                }
            }

            item {
                popularMovies.loadState.LoadingIfAppend()
            }
        }
    }
}

@Composable
private fun MoviesNowPlayingItem(
    movie: MovieNowPlaying,
    navigateToMovieDetails: () -> Unit,
    onAddToFavorite: (isFavorite: Boolean, movieId: Int) -> Unit,
) {
    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(elevation = 10.dp, modifier = Modifier.clickable(onClick = navigateToMovieDetails)) {
            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(elevation = 10.dp) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(movie.imageUrl)
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

                if (movie.isFavorite != null) {
                    Spacer(Modifier.height(10.dp))

                    FavoriteButton(
                        isFavorite = movie.isFavorite,
                        movieId = movie.id,
                        onAddToFavorite = onAddToFavorite
                    )
                }
            }
        }
    }
}

@Composable
private fun PopularMoviesItem(
    movie: PopularMovie,
    navigateToMovieDetails: () -> Unit,
    onAddToFavorite: (isFavorite: Boolean, movieId: Int) -> Unit
) {
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
                            .data(movie.imageUrl)
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

                    if (movie.isFavorite != null) {
                        Spacer(Modifier.height(10.dp))

                        FavoriteButton(
                            isFavorite = movie.isFavorite,
                            movieId = movie.id,
                            onAddToFavorite = onAddToFavorite
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteButton(
    isFavorite: Boolean,
    movieId: Int,
    onAddToFavorite: (isFavorite: Boolean, movieId: Int) -> Unit
) {
    IconButton(
        onClick = {
            onAddToFavorite.invoke(!isFavorite, movieId)
        }
    ) {
        val iconId = if (isFavorite) {
            R.drawable.ic_star_filled
        } else {
            R.drawable.ic_star_unfilled
        }

        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewPopularMoviesItem() {
    PopularMoviesItem(
        movie = PopularMovie(null, "Title", 0, isFavorite = false, voteAverage = 1.0),
        navigateToMovieDetails = {},
        onAddToFavorite = { _, _ -> })
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewMoviesNowPlayingItem() {
    MoviesNowPlayingItem(
        movie = MovieNowPlaying(
            imageUrl = null,
            title = "movie",
            id = 8466,
            voteAverage = 2.3,
            isFavorite = null
        ),
        navigateToMovieDetails = {},
        onAddToFavorite = { _, _ -> })
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewPopularMovies() {
    Column(modifier = Modifier.fillMaxSize()) {
        MoviesNowPlayingItem(
            movie = MovieNowPlaying(
                imageUrl = null,
                title = "movie",
                id = 3126,
                voteAverage = 6.7,
                isFavorite = null
            ),
            navigateToMovieDetails = {},
            onAddToFavorite = { _, _ -> })
        Divider(startIndent = 8.dp, thickness = 1.dp, color = Color.Black)
        PopularMoviesItem(
            movie = PopularMovie(null, "Title", 0, isFavorite = false, voteAverage = 1.0),
            navigateToMovieDetails = {},
            onAddToFavorite = { _, _ -> })
    }
}
