package com.droidcon.droidflix.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.droidcon.droidflix.R
import com.droidcon.droidflix.data.model.Flix

@Composable
fun FlixListScreen(
    viewModel: FlixViewModel,
    onFlixClicked: (Flix) -> Unit,
) {
    InfiniteFlixList(
        viewModel = viewModel,
        onFlixClicked = onFlixClicked,
        onSearch = { viewModel.getFlix(input = it, page = 1) },
    )
}

@Composable
fun InfiniteFlixList(
    viewModel: FlixViewModel,
    onFlixClicked: (Flix) -> Unit,
    onSearch: (String) -> Unit,
) {
    val flix by viewModel.flixList.collectAsState()
    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    var searchText by remember { mutableStateOf("") }

    val loadMoreItems = {
        viewModel.getFlix(searchText, page = viewModel.currentPage + 1)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START && searchText.isNotBlank()) {
                viewModel.getFlix(searchText, page = 1)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (search, list, loader, info) = createRefs()
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .zIndex(1f)
                    .constrainAs(loader) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .testTag("loader")
            )
        } else if (
            searchText.isBlank() ||
            error?.isNotBlank() == true ||
            flix.isEmpty()
        ) {
            Text(
                text = when {
                    searchText.isBlank() -> stringResource(id = R.string.search_for_movies)
                    error?.isNotBlank() == true -> error!!
                    else -> stringResource(id = R.string.empty)
                },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .constrainAs(info) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .testTag("info")
            )
        }

        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                onSearch(it)
            },
            label = { Text(text = stringResource(id = R.string.search)) },
            modifier = Modifier
                .constrainAs(search) {
                    top.linkTo(parent.top, margin = 8.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                }
                .testTag("search")
        )

        InfiniteScrollList(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(list) {
                    top.linkTo(search.bottom, margin = 16.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .testTag("list"),
            itemCount = flix.size,
            loadMoreItems = {
                loadMoreItems.invoke()
            }
        ) {
            FlixItem(flix[it], onFlixClicked)
        }
    }
}

@Composable
fun FlixItem(flix: Flix, onFlixClicked: (Flix) -> Unit) {
    Card(
        modifier = Modifier
            .height(300.dp)
            .padding(4.dp)
            .clickable {
                onFlixClicked(flix)
            }
            .testTag("flixItem")
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (image, name) = createRefs()

            AsyncImage(
                model = flix.poster,
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    .testTag("image"),
            )
            Text(
                text = flix.title,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .constrainAs(name) {
                        top.linkTo(image.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .testTag("name")
            )
        }
    }
}

@Composable
fun InfiniteScrollList(
    modifier: Modifier,
    itemCount: Int,
    loadMoreItems: () -> Unit,
    content: @Composable (Int) -> Unit
) {
    val listState = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        state = listState
    ) {
        items(itemCount) { index ->
            content(index)
            if (index == itemCount - 1) {
                loadMoreItems()
            }
        }
    }
}