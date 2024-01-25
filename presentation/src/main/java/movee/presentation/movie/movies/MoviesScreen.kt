package movee.presentation.movie.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import movee.domain.movie.model.MovieNowPlaying
import movee.domain.movie.model.PopularMovie
import movee.presentation.components.FavoriteButton
import movee.presentation.components.LoadingIfAppend
import movee.presentation.components.LoadingIfRefresh
import movee.presentation.components.NestedVerticalScroll
import movee.presentation.core.getInsetsController
import movee.presentation.theme.appTypography

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val moviesNowPlayingLazyPagingItems = viewModel.moviesNowPlaying.collectAsLazyPagingItems()
    val popularMovies = viewModel.popularMovies.collectAsLazyPagingItems()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        context.getInsetsController()?.isAppearanceLightStatusBars = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NestedVerticalScroll(
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
            topContent = {
                MoviesNowPlaying(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.background),
                    moviesNowPlaying = moviesNowPlayingLazyPagingItems,
                    navigateToMovieDetails = viewModel::navigateToMovieDetails,
                    onAddToFavorite = viewModel::addMovieToFavorites
                )
            },
            bottomContent = { lazyListContentPadding ->
                PopularMovies(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    lazyListContentPadding = lazyListContentPadding,
                    popularMovies = popularMovies,
                    navigateToMovieDetails = viewModel::navigateToMovieDetails,
                    onAddToFavorite = viewModel::addMovieToFavorites
                )
            })

        Spacer(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun MoviesNowPlaying(
    modifier: Modifier = Modifier,
    moviesNowPlaying: LazyPagingItems<MovieNowPlaying>,
    navigateToMovieDetails: (movieId: Int) -> Unit,
    onAddToFavorite: (isFavorite: Boolean, movieId: Int) -> Unit,
) {
    Column(modifier = modifier.padding(vertical = 10.dp)) {
        Text(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
            text = "Movies in Theaters",
            style = MaterialTheme.appTypography.h3Bold
        )

        moviesNowPlaying.loadState.LoadingIfRefresh()

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
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

            item {
                moviesNowPlaying.loadState.LoadingIfAppend()
            }
        }
    }
}

@Composable
private fun PopularMovies(
    modifier: Modifier = Modifier,
    lazyListContentPadding: PaddingValues,
    popularMovies: LazyPagingItems<PopularMovie>,
    navigateToMovieDetails: (movieId: Int) -> Unit,
    onAddToFavorite: (isFavorite: Boolean, movieId: Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = lazyListContentPadding
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
                    style = MaterialTheme.appTypography.h3Bold
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
                    style = MaterialTheme.appTypography.h3Bold
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = movie.voteAverage.toString(),
                    style = MaterialTheme.appTypography.bodyLRegular
                )

                if (movie.isFavorite != null) {
                    Spacer(Modifier.height(10.dp))

                    FavoriteButton(
                        isFavorite = movie.isFavorite!!,
                        onAddToFavorite = { onAddToFavorite.invoke(!movie.isFavorite!!, movie.id) }
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
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color.Gray)
                            }
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
                        style = MaterialTheme.appTypography.h3Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = movie.voteAverage.toString(),
                        style = MaterialTheme.appTypography.bodyLRegular
                    )

                    if (movie.isFavorite != null) {
                        Spacer(Modifier.height(10.dp))

                        FavoriteButton(
                            isFavorite = movie.isFavorite!!,
                            onAddToFavorite = {
                                onAddToFavorite.invoke(
                                    !movie.isFavorite!!,
                                    movie.id
                                )
                            }
                        )
                    }
                }
            }
        }
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
