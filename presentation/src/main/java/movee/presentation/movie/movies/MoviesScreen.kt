package movee.presentation.movie.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import movee.domain.movie.model.MovieNowPlaying
import movee.domain.movie.model.PopularMovie
import movee.presentation.components.FavoriteButton
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
                        .background(MaterialTheme.colors.background)
                        .padding(vertical = 10.dp),
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
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
            text = "Movies in Theaters",
            style = MaterialTheme.appTypography.h3Bold
        )

        if (moviesNowPlaying.loadState.refresh == LoadState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            items(count = moviesNowPlaying.itemCount) { index ->
                val item = moviesNowPlaying[index]

                if (item != null) {
                    NowPlayingMovie(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .width(150.dp),
                        movie = item,
                        navigateToMovieDetails = { navigateToMovieDetails(item.id) },
                        onAddToFavorite = onAddToFavorite
                    )
                }
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
            if (popularMovies.loadState.refresh == LoadState.Loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
        }

        items(count = popularMovies.itemCount) { index ->
            val item = popularMovies[index]

            if (item != null) {
                PopularMovie(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    movie = item,
                    navigateToMovieDetails = { navigateToMovieDetails(item.id) },
                    onAddToFavorite = onAddToFavorite
                )
            }
        }
    }
}

@Composable
private fun NowPlayingMovie(
    modifier: Modifier = Modifier,
    movie: MovieNowPlaying,
    navigateToMovieDetails: () -> Unit,
    onAddToFavorite: (isFavorite: Boolean, movieId: Int) -> Unit,
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = navigateToMovieDetails),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Poster(modifier = Modifier.padding(5.dp), posterUrl = movie.imageUrl)

        Spacer(Modifier.height(10.dp))

        Text(
            text = movie.title,
            style = MaterialTheme.appTypography.bodyLBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
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
                onClick = { onAddToFavorite.invoke(!movie.isFavorite!!, movie.id) }
            )
        }
    }
}

@Composable
private fun PopularMovie(
    modifier: Modifier = Modifier,
    movie: PopularMovie,
    navigateToMovieDetails: () -> Unit,
    onAddToFavorite: (isFavorite: Boolean, movieId: Int) -> Unit,
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = navigateToMovieDetails),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Poster(modifier = Modifier.padding(5.dp), posterUrl = movie.imageUrl)

        Spacer(Modifier.width(20.dp))

        Column {
            Text(
                text = movie.title,
                style = MaterialTheme.appTypography.bodyLBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
                    onClick = {
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

@Composable
private fun Poster(
    modifier: Modifier = Modifier,
    posterUrl: String?,
    posterHeight: Dp = 150.dp,
    posterElevation: Dp = 3.dp
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(posterUrl)
            .crossfade(true)
            .build(),
        loading = {
            Box(
                modifier = Modifier
                    .height(posterHeight)
                    .width(100.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Gray)
            }
        },
        modifier = modifier
            .height(posterHeight)
            .shadow(elevation = posterElevation, shape = MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium),
        contentDescription = "Movie Poster"
    )
}

@Preview(widthDp = 350)
@Composable
private fun PreviewPopularMoviesItem() {
    PopularMovie(
        movie = PopularMovie(
            imageUrl = null,
            title = "Title",
            id = 0,
            isFavorite = false,
            voteAverage = 1.0
        ),
        navigateToMovieDetails = {},
        onAddToFavorite = { _, _ -> })
}

@Preview(widthDp = 150)
@Composable
private fun PreviewNowPlayingItem() {
    NowPlayingMovie(
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
