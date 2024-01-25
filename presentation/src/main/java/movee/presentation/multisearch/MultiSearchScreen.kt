package movee.presentation.multisearch

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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import movee.domain.multisearch.model.SearchResult
import movee.presentation.components.LoadingIfAppend
import movee.presentation.components.LoadingIfRefresh
import movee.presentation.components.NestedVerticalScroll
import movee.presentation.core.getInsetsController
import movee.presentation.theme.appTypography

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
    val context = LocalContext.current

    LaunchedEffect(lazyListState.isScrollInProgress) {
        if (lazyListState.isScrollInProgress) {
            keyboardController?.hide()
        }
    }

    LaunchedEffect(Unit) {
        context.getInsetsController()?.isAppearanceLightStatusBars = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NestedVerticalScroll(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars),
            contentAlignment = Alignment.TopCenter,
            topContent = {
                OutlinedTextField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (it.isFocused) {
                                keyboardController?.show()
                            }
                        }
                        .background(MaterialTheme.colors.background),
                    value = searchTextField.value,
                    onValueChange = viewModel::search
                )

                LaunchedEffect(key1 = true) {
                    focusRequester.requestFocus()
                }
            },
            bottomContent = { lazyListContentPadding ->
                SearchResults(
                    modifier = Modifier.padding(top = 15.dp),
                    lazyListContentPadding = lazyListContentPadding,
                    searchResults = searchResults,
                    navigateToMovieDetails = viewModel::navigateToMovieDetails,
                    navigateToTvShowDetails = viewModel::navigateToTvShowDetails,
                    navigateToPersonDetails = viewModel::navigateToPersonDetails
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
private fun SearchResults(
    modifier: Modifier = Modifier,
    lazyListContentPadding: PaddingValues,
    searchResults: LazyPagingItems<SearchResult>,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
    navigateToPersonDetails: (id: Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = lazyListContentPadding
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
                            navigateToMovieDetails(item.id)
                        }
                    )
                }

                SearchResult.Type.TV -> {
                    SearchResultItem(
                        name = item.title,
                        imageUrl = item.imageUrl,
                        navigateToItemDetails = {
                            navigateToTvShowDetails(item.id)
                        }
                    )
                }

                SearchResult.Type.PERSON -> {
                    SearchResultItem(
                        name = item.title,
                        imageUrl = item.imageUrl,
                        navigateToItemDetails = {
                            navigateToPersonDetails(item.id)
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

@Composable
private fun SearchResultItem(
    name: String,
    imageUrl: String?,
    navigateToItemDetails: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 7.dp)
            .fillMaxWidth()
    ) {
        Card(
            elevation = 10.dp,
            modifier = Modifier
                .clickable(onClick = navigateToItemDetails)
                .fillMaxWidth()
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
                    style = MaterialTheme.appTypography.h3Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}