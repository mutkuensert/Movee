package com.mutkuensert.movee.ui.tvshows

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mutkuensert.movee.data.model.remote.tvshows.PopularTvShowsResult
import com.mutkuensert.movee.data.model.remote.tvshows.TopRatedTvShowsResult
import com.mutkuensert.movee.util.IMAGE_BASE_URL
import com.mutkuensert.movee.util.POSTER_SIZE_W500

private const val TAG = "TvShows Composable"

@Composable
fun TvShows(
    modifier: Modifier = Modifier,
    viewModel: TvShowsViewModel = hiltViewModel(),
    navigateToTvShowDetails: (tvShowId: Int) -> Unit
) {
    val popularTvShowsLazyPagingItems = viewModel.popularTvShows.collectAsLazyPagingItems()
    val topRatedTvShowsLazyPagingItems = viewModel.topRatedTvShows.collectAsLazyPagingItems()

    val stateOfPopularTvShows = rememberLazyListState()

    var previousFirstVisibleItemIndexOfTopRatedTvShows by remember { mutableStateOf(0) }
    val stateOfTopRatedTvShows = rememberLazyGridState()
    val visibilityOfItemsAbove = remember {
        derivedStateOf {
            if(stateOfTopRatedTvShows.firstVisibleItemIndex > previousFirstVisibleItemIndexOfTopRatedTvShows){
                false
            }else{
                true
            }.also { previousFirstVisibleItemIndexOfTopRatedTvShows = stateOfTopRatedTvShows.firstVisibleItemIndex }
        }
    }

    Column() {
        AnimatedVisibility(
            visible = visibilityOfItemsAbove.value,
            enter = slideInVertically(),
            exit = slideOutVertically()) {
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
                    popularTvShowsLazyPagingItems = popularTvShowsLazyPagingItems,
                    navigateToTvShowDetails = navigateToTvShowDetails,
                    lazyListState = stateOfPopularTvShows
                )

                Spacer(Modifier.height(10.dp))

                Divider(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    thickness = 1.dp,
                    color = Color.Black
                )

                Spacer(Modifier.height(10.dp))

            }
        }

        Column{
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = "Top Rated Tv Shows",
                color = Color.LightGray,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )

            Spacer(Modifier.height(10.dp))

            TopRatedTvShows(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                topRatedTvShowsLazyPagingItems = topRatedTvShowsLazyPagingItems,
                navigateToTvShowDetails = navigateToTvShowDetails,
                lazyGridState = stateOfTopRatedTvShows
            )
        }
    }

}

@Composable
fun PopularTvShows(
    modifier: Modifier = Modifier,
    popularTvShowsLazyPagingItems: LazyPagingItems<PopularTvShowsResult>,
    lazyListState: LazyListState,
    navigateToTvShowDetails: (tvShowId: Int) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (popularTvShowsLazyPagingItems.loadState.refresh == LoadState.Loading || popularTvShowsLazyPagingItems.loadState.append == LoadState.Loading) {
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

        LazyRow(state = lazyListState) {
            items(popularTvShowsLazyPagingItems) { item ->
                item?.let { itemNonNull ->
                    PopularTvShowsItem(
                        popularTvShow = itemNonNull,
                        onClick = { navigateToTvShowDetails(itemNonNull.id) }
                    )
                }
            }
        }

    }

}

@Composable
fun TopRatedTvShows(
    modifier: Modifier = Modifier,
    topRatedTvShowsLazyPagingItems: LazyPagingItems<TopRatedTvShowsResult>,
    lazyGridState: LazyGridState,
    navigateToTvShowDetails: (tvShowId: Int) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (topRatedTvShowsLazyPagingItems.loadState.refresh == LoadState.Loading || topRatedTvShowsLazyPagingItems.loadState.append == LoadState.Loading) {
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

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = lazyGridState
        ) {
            items(topRatedTvShowsLazyPagingItems.itemCount){ index ->
                topRatedTvShowsLazyPagingItems.get(index)?.let { topRatedTvShowNonNull ->
                    TopRatedTvShowsItem(topRatedTvShow = topRatedTvShowNonNull, onClick = { navigateToTvShowDetails(topRatedTvShowNonNull.id) })
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

                Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
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
fun PreviewTopRatedTveShowsItem(){
    TopRatedTvShowsItem(topRatedTvShow = TopRatedTvShowsResult(
        posterPath = null,
        id = 0,
        voteAverage = 0.0,
        name = "Tv Show Title"
    )) {

    }
}