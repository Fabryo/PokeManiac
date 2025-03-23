package com.shodo.android.myfriends.myfrienddetail.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.shodo.android.coreui.R
import com.shodo.android.coreui.theme.PokeManiacTheme
import com.shodo.android.coreui.ui.GenericLoader
import com.shodo.android.coreui.ui.SecondaryButton
import com.shodo.android.myfriends.myfrienddetail.MyFriendDetailUiState.Data
import com.shodo.android.myfriends.myfrienddetail.MyFriendDetailUiState.Loading
import com.shodo.android.myfriends.myfrienddetail.MyFriendDetailViewModel
import com.shodo.android.myfriends.uimodel.MyFriendPokemonCardUI
import com.shodo.android.myfriends.uimodel.MyFriendUI
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFriendDetailView(
    friendId: String,
    friendName: String,
    viewModel: MyFriendDetailViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.error.collectLatest { error ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.unsubscribed.collectLatest {
            onBackPressed()
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == ON_START) {
                viewModel.start(friendId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    Scaffold(
        modifier = Modifier
            .background(color = PokeManiacTheme.colors.backgroundApp)
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PokeManiacTheme.colors.backgroundApp
                ),
                title = {
                    Text(
                        text = friendName,
                        style = PokeManiacTheme.typography.t1,
                        textAlign = Center,
                        color = PokeManiacTheme.colors.primaryText
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
        when (val state = uiState) {
            is Data -> FriendDetailContent(
                friend = state.friend,
                onUnsubscribe = {
                    viewModel.unsubscribeFriend(state.friend.id)
                },
                innerPadding = innerPadding
            )

            Loading -> Column(
                modifier = Modifier
                    .background(color = PokeManiacTheme.colors.backgroundApp)
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Spacer(modifier = Modifier.weight(1f))
                GenericLoader()
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun FriendDetailContent(friend: MyFriendUI, innerPadding: PaddingValues, onUnsubscribe: () -> Unit) {
    Column(
        Modifier
            .background(color = PokeManiacTheme.colors.backgroundApp)
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PokeManiacTheme.dimens.standard),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(PokeManiacTheme.dimens.xxxLarge),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(friend.imageUrl)
                    .crossfade(true)
                    .build(),
                contentScale = Crop,
                contentDescription = friend.name
            )

            SecondaryButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(vertical = PokeManiacTheme.dimens.small),
                text = stringResource(R.string.unsubscribe),
                onClick = onUnsubscribe
            )
        }
        if (friend.description.length > 1) { // To avoid the descriptions of 1 character
            Text(
                modifier = Modifier.padding(top = PokeManiacTheme.dimens.standard, start = PokeManiacTheme.dimens.standard, end = PokeManiacTheme.dimens.standard),
                color = PokeManiacTheme.colors.primaryText,
                style = PokeManiacTheme.typography.t8,
                text = friend.description,
            )
        }
        Text(
            modifier = Modifier.padding(top = PokeManiacTheme.dimens.xSmall, start = PokeManiacTheme.dimens.standard, end = PokeManiacTheme.dimens.standard),
            color = PokeManiacTheme.colors.primaryText,
            style = PokeManiacTheme.typography.t5,
            text = friend.pokemonCards.sumOf { it.totalVotes }.takeIf { it > 0 }?.let {
                stringResource(R.string.my_friend_detail_total_votes, it)
            } ?: stringResource(R.string.my_friend_detail_total_votes_none),
        )

        if (friend.pokemonCards.isEmpty()) {
            Spacer(Modifier.weight(1f))
            Text(
                color = PokeManiacTheme.colors.primaryText,
                style = PokeManiacTheme.typography.t3,
                text = stringResource(R.string.my_friend_detail_no_activity_yet, friend.name),
                modifier = Modifier
                    .padding(PokeManiacTheme.dimens.small)
                    .align(CenterHorizontally)
            )
            Spacer(Modifier.weight(1f))
        } else {
            Box(
                modifier = Modifier
                    .padding(top = PokeManiacTheme.dimens.small)
                    .fillMaxWidth()
                    .height(PokeManiacTheme.dimens.separator)
                    .background(color = PokeManiacTheme.colors.tertiary)
            )
            FriendActivitiesContent(
                modifier = Modifier
                    .padding(top = PokeManiacTheme.dimens.standard)
                    .weight(1f),
                pokemonCards = friend.pokemonCards
            )
        }
    }
}

@Composable
fun FriendActivitiesContent(modifier: Modifier = Modifier, pokemonCards: List<MyFriendPokemonCardUI>) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = PokeManiacTheme.dimens.minGridUserCellSize),
        contentPadding = PaddingValues(PokeManiacTheme.dimens.xSmall)
    ) {
        items(pokemonCards) { card ->
            AsyncImage(
                modifier = Modifier
                    .clip(RectangleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(card.imageUrl)
                    .crossfade(true)
                    .build(),
                contentScale = Crop,
                contentDescription = card.name
            )
        }
    }
}

@Preview
@Composable
fun CharacterDetailViewPreview() {
}