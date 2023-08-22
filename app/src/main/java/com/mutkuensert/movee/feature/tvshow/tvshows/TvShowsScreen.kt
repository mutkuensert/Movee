package com.mutkuensert.movee.feature.tvshow.tvshows

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mutkuensert.movee.core.LoadingIfAppend
import com.mutkuensert.movee.core.LoadingIfRefresh
import com.mutkuensert.movee.domain.tvshow.model.PopularTvShow
import com.mutkuensert.movee.domain.tvshow.model.TopRatedTvShow
import kotlin.math.roundToInt

@Composable
fun TvShowsScreen(
    viewModel: TvShowsViewModel = hiltViewModel()
) {
    val popularTvShowsLazyPagingItems = viewModel.popularTvShows.collectAsLazyPagingItems()
    val topRatedTvShows = viewModel.topRatedTvShows.collectAsLazyPagingItems()

    val stateOfPopularTvShows = rememberLazyListState()
    val stateOfTopRatedTvShows = rememberLazyGridState()

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
        TopRatedTvShows(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            topRatedTvShows = topRatedTvShows,
            navigateToTvShowDetails = viewModel::navigateToTvShowDetails,
            itemsAboveHeight = itemsAboveHeight,
            lazyGridState = stateOfTopRatedTvShows
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
                    text = "Popular Tv Shows",
                    color = Color.LightGray,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(10.dp))

                PopularTvShows(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 10.dp),
                    popularTvShows = popularTvShowsLazyPagingItems,
                    navigateToTvShowDetails = viewModel::navigateToTvShowDetails,
                    lazyListState = stateOfPopularTvShows
                )

                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun PopularTvShows(
    modifier: Modifier = Modifier,
    popularTvShows: LazyPagingItems<PopularTvShow>,
    lazyListState: LazyListState,
    navigateToTvShowDetails: (tvShowId: Int) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        popularTvShows.loadState.LoadingIfRefresh()

        LazyRow(state = lazyListState) {
            items(count = popularTvShows.itemCount) { index ->
                val item = popularTvShows[index]

                if (item != null) {
                    PopularTvShowsItem(
                        popularTvShow = item,
                        onClick = { navigateToTvShowDetails(item.id) }
                    )
                }
            }
        }

        popularTvShows.loadState.LoadingIfAppend()
    }
}

@Composable
private fun TopRatedTvShows(
    modifier: Modifier = Modifier,
    topRatedTvShows: LazyPagingItems<TopRatedTvShow>,
    itemsAboveHeight: MutableState<Dp>,
    lazyGridState: LazyGridState,
    navigateToTvShowDetails: (tvShowId: Int) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val spanCount = 2

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = lazyGridState,
            contentPadding = PaddingValues(top = itemsAboveHeight.value)
        ) {
            item(
                span = { GridItemSpan(spanCount) }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(Modifier.height(10.dp))

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth(),
                        text = "Top Rated Tv Shows",
                        color = Color.LightGray,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        maxLines = 1
                    )

                    Spacer(Modifier.height(10.dp))
                }
            }

            item(
                span = { GridItemSpan(spanCount) }
            ) {
                topRatedTvShows.loadState.LoadingIfRefresh()
            }


            items(topRatedTvShows.itemCount) { index ->
                val tvShow = topRatedTvShows[index]

                if (tvShow != null) {
                    TopRatedTvShowsItem(
                        topRatedTvShow = tvShow,
                        onClick = { navigateToTvShowDetails(tvShow.id) })
                }
            }

            item(
                span = { GridItemSpan(spanCount) }
            ) {
                topRatedTvShows.loadState.LoadingIfAppend()
            }
        }
    }
}

@Composable
private fun PopularTvShowsItem(popularTvShow: PopularTvShow, onClick: () -> Unit) {
    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(elevation = 10.dp, modifier = Modifier
            .clickable { onClick() }) {

            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(elevation = 10.dp) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(popularTvShow.imageUrl)
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
                    text = popularTvShow.name,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = popularTvShow.voteAverage.toString(),
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TopRatedTvShowsItem(
    topRatedTvShow: TopRatedTvShow,
    onClick: () -> Unit
) {
    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(elevation = 10.dp, modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .clickable { onClick() }) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(elevation = 10.dp) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(topRatedTvShow.imageUrl)
                            .crossfade(true)
                            .build(),
                        loading = {
                            CircularProgressIndicator(color = Color.Gray)
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .fillMaxWidth(),
                        contentDescription = "Movie Poster",
                        contentScale = ContentScale.FillWidth
                    )
                }

                Spacer(Modifier.height(5.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = topRatedTvShow.name,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = topRatedTvShow.voteAverage.toString(),
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
private fun PreviewTopRatedTveShowsItem() {
    TopRatedTvShowsItem(
        topRatedTvShow = TopRatedTvShow(
            imageUrl = null,
            id = 0,
            voteAverage = 0.0,
            name = "Tv Show Title"
        ),
        onClick = {}
    )
}