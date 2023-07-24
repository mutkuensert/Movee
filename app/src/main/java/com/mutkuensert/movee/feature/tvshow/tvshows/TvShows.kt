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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mutkuensert.movee.data.tvshow.model.PopularTvShowsResult
import com.mutkuensert.movee.data.tvshow.model.TopRatedTvShowsResult
import com.mutkuensert.movee.util.IMAGE_BASE_URL
import com.mutkuensert.movee.util.POSTER_SIZE_W500
import kotlin.math.roundToInt

@Composable
fun TvShows(
    viewModel: TvShowsViewModel = hiltViewModel(),
    navigateToTvShowDetails: (tvShowId: Int) -> Unit
) {
    val popularTvShowsLazyPagingItems = viewModel.popularTvShows.collectAsLazyPagingItems()
    val topRatedTvShowsLazyPagingItems = viewModel.topRatedTvShows.collectAsLazyPagingItems()

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
            topRatedTvShowsLazyPagingItems = topRatedTvShowsLazyPagingItems,
            navigateToTvShowDetails = navigateToTvShowDetails,
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
                    navigateToTvShowDetails = navigateToTvShowDetails,
                    lazyListState = stateOfPopularTvShows
                )

                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun PopularTvShows(
    modifier: Modifier = Modifier,
    popularTvShows: LazyPagingItems<PopularTvShowsResult>,
    lazyListState: LazyListState,
    navigateToTvShowDetails: (tvShowId: Int) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (popularTvShows.loadState.refresh == LoadState.Loading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
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


        if (popularTvShows.loadState.append == LoadState.Loading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                    strokeWidth = 6.dp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun TopRatedTvShows(
    modifier: Modifier = Modifier,
    topRatedTvShowsLazyPagingItems: LazyPagingItems<TopRatedTvShowsResult>,
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
                if (topRatedTvShowsLazyPagingItems.loadState.refresh == LoadState.Loading) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(40.dp))

                        CircularProgressIndicator(
                            modifier = Modifier.size(100.dp),
                            strokeWidth = 6.dp,
                            color = Color.Gray
                        )

                        Spacer(Modifier.height(50.dp))
                    }
                }
            }


            items(topRatedTvShowsLazyPagingItems.itemCount) { index ->
                topRatedTvShowsLazyPagingItems[index]?.let { topRatedTvShowNonNull ->
                    TopRatedTvShowsItem(
                        topRatedTvShow = topRatedTvShowNonNull,
                        onClick = { navigateToTvShowDetails(topRatedTvShowNonNull.id) })
                }
            }

            item(
                span = { GridItemSpan(spanCount) }
            ) {
                if (topRatedTvShowsLazyPagingItems.loadState.append == LoadState.Loading) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(40.dp))

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
}

@Composable
fun PopularTvShowsItem(popularTvShow: PopularTvShowsResult, onClick: () -> Unit) {
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
                            .data("$IMAGE_BASE_URL$POSTER_SIZE_W500${popularTvShow.posterPath}")
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
fun TopRatedTvShowsItem(topRatedTvShow: TopRatedTvShowsResult, onClick: () -> Unit) {
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
                            .data("$IMAGE_BASE_URL$POSTER_SIZE_W500${topRatedTvShow.posterPath}")
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
fun PreviewTopRatedTveShowsItem() {
    TopRatedTvShowsItem(
        topRatedTvShow = TopRatedTvShowsResult(
            posterPath = null,
            id = 0,
            voteAverage = 0.0,
            name = "Tv Show Title"
        ),
        onClick = {}
    )
}