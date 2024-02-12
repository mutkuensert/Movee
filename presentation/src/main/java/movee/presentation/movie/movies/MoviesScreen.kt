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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import movee.domain.movie.model.MovieNowPlaying
import movee.domain.movie.model.PopularMovie
import movee.presentation.components.FavoriteButton
import movee.presentation.components.NestedVerticalScroll
import movee.presentation.components.Poster
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
                val movie = moviesNowPlaying[index]

                if (movie != null) {
                    NowPlayingMovie(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .width(150.dp),
                        id = movie.id,
                        title = movie.title,
                        imageUrl = movie.imageUrl,
                        isFavorite = movie.isFavorite,
                        voteAverage = movie.voteAverage,
                        navigateToMovieDetails = { navigateToMovieDetails(movie.id) },
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
            val movie = popularMovies[index]

            if (movie != null) {
                PopularMovie(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    id = movie.id,
                    title = movie.title,
                    imageUrl = movie.imageUrl,
                    isFavorite = movie.isFavorite,
                    voteAverage = movie.voteAverage,
                    navigateToMovieDetails = { navigateToMovieDetails(movie.id) },
                    onAddToFavorite = onAddToFavorite
                )
            }
        }
    }
}

@Composable
private fun NowPlayingMovie(
    modifier: Modifier = Modifier,
    id: Int,
    title: String,
    imageUrl: String?,
    isFavorite: Boolean?,
    voteAverage: Double,
    navigateToMovieDetails: () -> Unit,
    onAddToFavorite: (isFavorite: Boolean, movieId: Int) -> Unit,
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = navigateToMovieDetails),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Poster(modifier = Modifier.padding(5.dp), posterUrl = imageUrl)

        Spacer(Modifier.height(10.dp))

        Text(
            text = title,
            style = MaterialTheme.appTypography.bodyLBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = voteAverage.toString(),
            style = MaterialTheme.appTypography.bodyLRegular
        )

        if (isFavorite != null) {
            Spacer(Modifier.height(10.dp))

            FavoriteButton(
                isFavorite = isFavorite,
                onClick = { onAddToFavorite.invoke(!isFavorite, id) }
            )
        }
    }
}

@Composable
private fun PopularMovie(
    modifier: Modifier = Modifier,
    id: Int,
    title: String,
    imageUrl: String?,
    isFavorite: Boolean?,
    voteAverage: Double,
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
        Poster(modifier = Modifier.padding(5.dp), posterUrl = imageUrl)

        Spacer(Modifier.width(20.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.appTypography.bodyLBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = voteAverage.toString(),
                style = MaterialTheme.appTypography.bodyLRegular
            )

            if (isFavorite != null) {
                Spacer(Modifier.height(10.dp))

                FavoriteButton(
                    isFavorite = isFavorite,
                    onClick = {
                        onAddToFavorite.invoke(!isFavorite, id)
                    }
                )
            }
        }
    }
}

@Preview(widthDp = 350)
@Composable
private fun PreviewPopularMoviesItem() {
    PopularMovie(
        id = 5363,
        title = "quis",
        imageUrl = null,
        isFavorite = null,
        voteAverage = 8.9,
        navigateToMovieDetails = {},
        onAddToFavorite = { _, _ -> }
    )
}

@Preview(widthDp = 150)
@Composable
private fun PreviewNowPlayingItem() {
    NowPlayingMovie(
        id = 5363,
        title = "quis",
        imageUrl = null,
        isFavorite = null,
        voteAverage = 8.9,
        navigateToMovieDetails = {},
        onAddToFavorite = { _, _ -> })
}
