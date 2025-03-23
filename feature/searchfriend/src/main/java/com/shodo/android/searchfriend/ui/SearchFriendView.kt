package com.shodo.android.searchfriend.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.shodo.android.coreui.R
import com.shodo.android.coreui.theme.PokeManiacTheme
import com.shodo.android.coreui.ui.GenericLoader
import com.shodo.android.coreui.ui.PrimaryButton
import com.shodo.android.coreui.ui.SecondaryButton
import com.shodo.android.searchfriend.SearchFriendUiState.Data
import com.shodo.android.searchfriend.SearchFriendUiState.EmptyResult
import com.shodo.android.searchfriend.SearchFriendUiState.EmptySearch
import com.shodo.android.searchfriend.SearchFriendUiState.Loading
import com.shodo.android.searchfriend.SearchFriendViewModel
import com.shodo.android.searchfriend.uimodel.SearchFriendUI
import com.shodo.android.searchfriend.uimodel.SubscriptionState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFriendView(
    modifier: Modifier = Modifier,
    viewModel: SearchFriendViewModel,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.error.collectLatest { error ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
    }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color = PokeManiacTheme.colors.backgroundApp),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PokeManiacTheme.colors.backgroundApp
                ),
                title = {
                    DebouncedSearchTextField(
                        placeholderText = stringResource(R.string.search_friends),
                        onDebouncedQueryChange = { query -> viewModel.searchFriend(query) }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PokeManiacTheme.colors.primaryText
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .background(color = PokeManiacTheme.colors.backgroundApp)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(PokeManiacTheme.dimens.separator)
                    .background(color = PokeManiacTheme.colors.tertiary)
                    .padding(top = PokeManiacTheme.dimens.small)
            )

            when (val state = uiState) {
                is Data -> PeopleData(
                    Modifier.padding(top = PokeManiacTheme.dimens.small),
                    state.people,
                    viewModel::subscribeCharacter,
                    viewModel::unsubscribeCharacter
                )

                Loading -> {
                    Spacer(Modifier.weight(1f))
                    GenericLoader(Modifier.align(CenterHorizontally))
                    Spacer(Modifier.weight(1f))
                }

                is EmptyResult -> {
                    Spacer(Modifier.weight(1f))
                    Text(
                        color = PokeManiacTheme.colors.primaryText,
                        style = PokeManiacTheme.typography.t3,
                        text = stringResource(R.string.search_friends_no_result, state.query),
                        modifier = Modifier
                            .padding(PokeManiacTheme.dimens.small)
                            .align(CenterHorizontally)
                    )
                    Spacer(Modifier.weight(1f))
                }

                EmptySearch -> {
                    Spacer(Modifier.weight(1f))
                    Text(
                        color = PokeManiacTheme.colors.primaryText,
                        style = PokeManiacTheme.typography.t3,
                        text = stringResource(R.string.search_friends_description),
                        modifier = Modifier
                            .padding(PokeManiacTheme.dimens.small)
                            .align(CenterHorizontally)
                    )
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun PeopleData(
    modifier: Modifier = Modifier,
    characters: List<SearchFriendUI>,
    onSubscribeCharacter: (String) -> Unit,
    onUnsubscribeCharacter: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = PokeManiacTheme.dimens.minGridSearchCellSize),
        modifier = modifier.padding(PokeManiacTheme.dimens.xSmall),
        contentPadding = PaddingValues(PokeManiacTheme.dimens.xSmall),
        horizontalArrangement = Arrangement.spacedBy(PokeManiacTheme.dimens.xSmall),
        verticalArrangement = Arrangement.spacedBy(PokeManiacTheme.dimens.small),

        ) {
        items(characters) { friend ->
            PersonCard(
                friend = friend,
                onSubscribeFriend = onSubscribeCharacter,
                onUnsubscribeFriend = onUnsubscribeCharacter
            )
        }
    }
}


@Composable
fun PersonCard(
    friend: SearchFriendUI,
    onSubscribeFriend: (String) -> Unit,
    onUnsubscribeFriend: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PokeManiacTheme.dimens.xxSmall),
        shape = RoundedCornerShape(PokeManiacTheme.dimens.small),
        colors = CardDefaults.cardColors(containerColor = PokeManiacTheme.colors.backgroundCell),
    ) {
        Column(
            modifier = Modifier.wrapContentHeight(),
            horizontalAlignment = CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(top = PokeManiacTheme.dimens.standard)
                    .clip(CircleShape)
                    .wrapContentSize()
                    .size(PokeManiacTheme.dimens.xxxLarge),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(friend.imageUrl)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = friend.name,
            )

            Text(
                color = PokeManiacTheme.colors.primaryText,
                style = PokeManiacTheme.typography.t3,
                text = friend.name,
                modifier = Modifier.padding(
                    top = PokeManiacTheme.dimens.small,
                    start = PokeManiacTheme.dimens.small,
                    end = PokeManiacTheme.dimens.small
                )
            )

            when (friend.subscriptionState) {
                SubscriptionState.Subscribed -> SecondaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PokeManiacTheme.dimens.small, vertical = PokeManiacTheme.dimens.small),
                    text = stringResource(R.string.unsubscribe)
                ) { onUnsubscribeFriend(friend.id) }

                SubscriptionState.NotSubscribed -> PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PokeManiacTheme.dimens.small, vertical = PokeManiacTheme.dimens.small),
                    text = stringResource(R.string.subscribe),
                    onClick = { onSubscribeFriend(friend.id) }
                )

                SubscriptionState.UpdatingSubscribe -> CircularProgressIndicator(
                    modifier = Modifier.align(CenterHorizontally),
                    color = PokeManiacTheme.colors.primaryText
                )
            }
        }
    }
}


@OptIn(FlowPreview::class)
@Composable
fun DebouncedSearchTextField(
    placeholderText: String,
    onDebouncedQueryChange: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        snapshotFlow { searchQuery }
            .debounce(500)
            .collect { debouncedText ->
                onDebouncedQueryChange(debouncedText)
            }
    }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = PokeManiacTheme.colors.primaryText,
        backgroundColor = PokeManiacTheme.colors.fourthText
    )
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(placeholderText) },
            singleLine = true,
            textStyle = PokeManiacTheme.typography.t7.copy(color = PokeManiacTheme.colors.primaryText),
            modifier = Modifier.fillMaxSize(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = PokeManiacTheme.colors.backgroundCell,
                unfocusedContainerColor = PokeManiacTheme.colors.backgroundCell,
                cursorColor = PokeManiacTheme.colors.backgroundCell,
                focusedLabelColor = Color.Transparent,
                unfocusedPlaceholderColor = PokeManiacTheme.colors.fourthText,
                focusedPlaceholderColor = PokeManiacTheme.colors.thirdText,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

// TODO Fabrice
//@Preview(showBackground = true)
//@Composable
//fun CharacterListCharactersPreview() {
//    PokeManiacTheme {
//        CharactersData()
//    }
//}