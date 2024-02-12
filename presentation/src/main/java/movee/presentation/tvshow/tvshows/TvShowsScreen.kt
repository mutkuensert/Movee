package movee.presentation.tvshow.tvshows

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import movee.domain.tvshow.model.PopularTvShow
import movee.domain.tvshow.model.TopRatedTvShow
import movee.presentation.components.FavoriteButton
import movee.presentation.components.NestedVerticalScroll
import movee.presentation.components.Poster
import movee.presentation.core.getInsetsController
import movee.presentation.theme.appTypography

@Composable
fun TvShowsScreen(
    viewModel: TvShowsViewModel = hiltViewModel()
) {
    val popularTvShowsLazyPagingItems = viewModel.popularTvShows.collectAsLazyPagingItems()
    val topRatedTvShows = viewModel.topRatedTvShows.collectAsLazyPagingItems()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        context.getInsetsController()?.isAppearanceLightStatusBars = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NestedVerticalScroll(
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
            topContent = {
                PopularTvShows(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.background)
                        .padding(vertical = 10.dp),
                    popularTvShows = popularTvShowsLazyPagingItems,
                    navigateToTvShowDetails = viewModel::navigateToTvShowDetails,
                    onAddToFavorite = viewModel::addTvShowToFavorites
                )
            },
            bottomContent = { lazyListContentPadding ->
                TopRatedTvShows(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    topRatedTvShows = topRatedTvShows,
                    navigateToTvShowDetails = viewModel::navigateToTvShowDetails,
                    lazyListContentPadding = lazyListContentPadding,
                    onAddToFavorite = viewModel::addTvShowToFavorites
                )
            }
        )

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
private fun PopularTvShows(
    modifier: Modifier = Modifier,
    popularTvShows: LazyPagingItems<PopularTvShow>,
    navigateToTvShowDetails: (tvShowId: Int) -> Unit,
    onAddToFavorite: (isFavorite: Boolean, tvShowId: Int) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
            text = "Popular Tv Shows",
            style = MaterialTheme.appTypography.h3Bold
        )

        if (popularTvShows.loadState.refresh == LoadState.Loading) {
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
            items(count = popularTvShows.itemCount) { index ->
                val item = popularTvShows[index]

                if (item != null) {
                    PopularShow(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .width(150.dp),
                        id = item.id,
                        name = item.name,
                        imageUrl = item.imageUrl,
                        voteAverage = item.voteAverage,
                        isFavorite = item.isFavorite,
                        onClick = { navigateToTvShowDetails(item.id) },
                        onAddToFavorite = onAddToFavorite
                    )
                }
            }
        }
    }
}

@Composable
private fun TopRatedTvShows(
    modifier: Modifier = Modifier,
    topRatedTvShows: LazyPagingItems<TopRatedTvShow>,
    lazyListContentPadding: PaddingValues,
    navigateToTvShowDetails: (tvShowId: Int) -> Unit,
    onAddToFavorite: (isFavorite: Boolean, tvShowId: Int) -> Unit,
) {
    val spanCount = 2

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = lazyListContentPadding
    ) {
        item(
            span = { GridItemSpan(spanCount) }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = "Top Rated Tv Shows",
                    style = MaterialTheme.appTypography.h3Bold
                )

                Spacer(Modifier.height(10.dp))
            }
        }

        item(
            span = { GridItemSpan(spanCount) }
        ) {
            if (topRatedTvShows.loadState.refresh == LoadState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
        }


        items(topRatedTvShows.itemCount) { index ->
            val tvShow = topRatedTvShows[index]

            if (tvShow != null) {
                TopRatedShow(
                    modifier = Modifier
                        .width(150.dp)
                        .padding(10.dp),
                    id = tvShow.id,
                    name = tvShow.name,
                    imageUrl = tvShow.imageUrl,
                    voteAverage = tvShow.voteAverage,
                    isFavorite = tvShow.isFavorite,
                    onClick = { navigateToTvShowDetails(tvShow.id) },
                    onAddToFavorite = onAddToFavorite
                )
            }
        }
    }
}

@Composable
private fun PopularShow(
    modifier: Modifier = Modifier,
    id: Int,
    name: String,
    imageUrl: String?,
    voteAverage: Double,
    isFavorite: Boolean?,
    onClick: () -> Unit,
    onAddToFavorite: (isFavorite: Boolean, tvShowId: Int) -> Unit,
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Poster(modifier = Modifier.padding(5.dp), posterUrl = imageUrl)

        Spacer(Modifier.height(10.dp))

        Text(
            text = name,
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
private fun TopRatedShow(
    modifier: Modifier = Modifier,
    id: Int,
    name: String,
    imageUrl: String?,
    voteAverage: Double,
    isFavorite: Boolean?,
    onClick: () -> Unit,
    onAddToFavorite: (isFavorite: Boolean, tvShowId: Int) -> Unit,
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Poster(
            modifier = Modifier.padding(5.dp),
            posterUrl = imageUrl
        )

        Spacer(Modifier.width(20.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = name,
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewTopRatedTvShow() {
    TopRatedShow(
        id = 1364,
        name = "Ada Stein",
        imageUrl = null,
        voteAverage = 4.5,
        isFavorite = null,
        onClick = {},
        onAddToFavorite = { _, _ -> }
    )
}