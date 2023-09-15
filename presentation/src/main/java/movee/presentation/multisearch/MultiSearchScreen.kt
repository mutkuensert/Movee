package movee.presentation.multisearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import movee.domain.multisearch.model.SearchResult
import movee.presentation.core.LoadingIfAppend
import movee.presentation.core.LoadingIfRefresh

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MultiSearchScreen(
    viewModel: MultiSearchViewModel = hiltViewModel()
) {
    val searchResults = viewModel.multiSearchResults.collectAsLazyPagingItems()
    val searchTextField = viewModel.searchTextField.collectAsStateWithLifecycle()
    val focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current
    val lazyListState = rememberLazyListState()

    if (lazyListState.isScrollInProgress) keyboardController?.hide()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        LaunchedEffect(key1 = true) {
            focusRequester.requestFocus()
        }

        Spacer(Modifier.height(15.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = lazyListState
        ) {
            item {
                searchResults.loadState.LoadingIfRefresh()
            }

            items(count = searchResults.itemCount) { index ->
                val item = searchResults[index]

                when (item?.type) {
                    SearchResult.Type.MOVIE -> {
                        SearchResultItem(
                            name = item.title,
                            imageUrl = item.imageUrl,
                            navigateToItemDetails = {
                                viewModel.navigateToMovieDetails(item.id)
                            }
                        )
                    }

                    SearchResult.Type.TV -> {
                        SearchResultItem(
                            name = item.title,
                            imageUrl = item.imageUrl,
                            navigateToItemDetails = {
                                viewModel.navigateToTvShowDetails(item.id)
                            }
                        )
                    }

                    SearchResult.Type.PERSON -> {
                        SearchResultItem(
                            name = item.title,
                            imageUrl = item.imageUrl,
                            navigateToItemDetails = {
                                viewModel.navigateToPersonDetails(item.id)
                            }
                        )
                    }

                    else -> {}
                }
            }

            item {
                searchResults.loadState.LoadingIfAppend()
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    name: String,
    imageUrl: String?,
    navigateToItemDetails: () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 7.dp)) {
        Card(elevation = 10.dp, modifier = Modifier
            .clickable { navigateToItemDetails() }
        ) {
            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(elevation = 10.dp) {
                    if (imageUrl != null) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
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