package com.mutkuensert.movee.ui.multisearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mutkuensert.movee.data.model.remote.search.MovieResultItemModel
import com.mutkuensert.movee.data.model.remote.search.PersonResulItemModel
import com.mutkuensert.movee.data.model.remote.search.TvResultItemModel
import com.mutkuensert.movee.util.IMAGE_BASE_URL
import com.mutkuensert.movee.util.SIZE_ORIGINAL

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MultiSearch(
    viewModel: MultiSearchViewModel = hiltViewModel(),
    navigateToMovieDetails: (movieId: Int) -> Unit,
    navigateToTvDetails: (tvId: Int) -> Unit,
    navigateToPersonDetails: (personId: Int) -> Unit) {

    val searchResultsLazyPagingItems = viewModel.multiSearchResults.collectAsLazyPagingItems()
    val searchTextField = viewModel.searchTextField.collectAsStateWithLifecycle()
    val focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current
    val lazyListState = rememberLazyListState()

    if (lazyListState.isScrollInProgress) keyboardController?.hide()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        keyboardController?.show()
                    }
                },
            value = searchTextField.value,
            onValueChange = { viewModel.multiSearch(it) })

        LaunchedEffect(key1 = true){
            focusRequester.requestFocus()
        }

        Spacer(Modifier.height(15.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, state = lazyListState){

            item {
                if (searchResultsLazyPagingItems.loadState.refresh == LoadState.Loading) {
                    Spacer(Modifier.height(50.dp))

                    CircularProgressIndicator(
                        modifier = Modifier.size(100.dp),
                        strokeWidth = 6.dp,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(50.dp))
                }
            }

            items(searchResultsLazyPagingItems){ searchResultItem ->
                when(searchResultItem){

                    is MovieResultItemModel -> {
                        (searchResultItem as MovieResultItemModel).let { movieResultItemModel ->
                            SearchResultItem(
                                name = movieResultItemModel.title,
                                picturePath = movieResultItemModel.posterPath,
                                navigateToItemDetails = { navigateToMovieDetails(movieResultItemModel.id) }
                            )
                        }
                    }

                    is TvResultItemModel -> {
                        (searchResultItem as TvResultItemModel).let { tvResultItemModel ->
                            SearchResultItem(
                                name = tvResultItemModel.name,
                                picturePath = tvResultItemModel.posterPath,
                                navigateToItemDetails = { navigateToTvDetails(tvResultItemModel.id) }
                            )
                        }
                    }

                    is PersonResulItemModel -> {
                        (searchResultItem as PersonResulItemModel).let { personResulItemModel ->
                            SearchResultItem(
                                name = personResulItemModel.name,
                                picturePath = personResulItemModel.profilePath,
                                navigateToItemDetails = { navigateToPersonDetails(personResulItemModel.id) }
                            )
                        }
                    }
                }

            }

            item {
                if (searchResultsLazyPagingItems.loadState.append == LoadState.Loading) {
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
fun SearchResultItem(
    name: String,
    picturePath: String?,
    navigateToItemDetails: () -> Unit){

    Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(elevation = 10.dp, modifier = Modifier
            .clickable { navigateToItemDetails() }
        ) {

            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Card(elevation = 10.dp) {
                    picturePath?.let { picturePathNonNull ->
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("$IMAGE_BASE_URL$SIZE_ORIGINAL${picturePathNonNull}")
                                .crossfade(true)
                                .build(),
                            loading = {
                                CircularProgressIndicator(color = Color.Gray)
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))
                                .height(200.dp),
                            contentDescription = "Item Poster"
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = name,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )

            }
        }

    }
}