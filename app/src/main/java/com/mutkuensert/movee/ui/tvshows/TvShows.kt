package com.mutkuensert.movee.ui.tvshows

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(10.dp))

        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "Popular Tv Shows",
            color = Color.LightGray,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )

        Spacer(Modifier.height(10.dp))

        PopularTvShowsDataObserver(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 10.dp),
            popularTvShowsLazyPagingItems = popularTvShowsLazyPagingItems,
            navigateToTvShowDetails = navigateToTvShowDetails
        )

        Spacer(Modifier.height(10.dp))

        Divider(
            modifier = Modifier.padding(horizontal = 10.dp),
            thickness = 1.dp,
            color = Color.Black
        )

        Spacer(Modifier.height(10.dp))

        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "Top Rated Tv Shows",
            color = Color.LightGray,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )

        Spacer(Modifier.height(10.dp))

        TopRatedTvShowsDataObserver(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            topRatedTvShowsLazyPagingItems = topRatedTvShowsLazyPagingItems,
            navigateToTvShowDetails = navigateToTvShowDetails
        )
    }
}

@Composable
fun PopularTvShowsDataObserver(
    modifier: Modifier = Modifier,
    popularTvShowsLazyPagingItems: LazyPagingItems<PopularTvShowsResult>,
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

        LazyRow {
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
fun TopRatedTvShowsDataObserver(
    modifier: Modifier = Modifier,
    topRatedTvShowsLazyPagingItems: LazyPagingItems<TopRatedTvShowsResult>,
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
            columns = GridCells.Fixed(2)
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
                    text = popularTvShow.originalName,
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
                            .height(250.dp),
                        contentDescription = "Movie Poster"
                    )
                }

                Spacer(Modifier.height(5.dp))

                Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
                    Text(
                        text = topRatedTvShow.originalName,
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
        originalName = "Tv Show Title"
    )) {

    }
}