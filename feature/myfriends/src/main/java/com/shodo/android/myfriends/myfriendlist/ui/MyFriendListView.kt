package com.shodo.android.myfriends.myfriendlist.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
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
import com.shodo.android.myfriends.myfriendlist.MyFriendListUiState
import com.shodo.android.myfriends.myfriendlist.MyFriendListUiState.Data
import com.shodo.android.myfriends.myfriendlist.MyFriendListUiState.Loading
import com.shodo.android.myfriends.myfriendlist.MyFriendListViewModel
import com.shodo.android.myfriends.uimodel.MyFriendUI
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFriendListView(
    modifier: Modifier = Modifier,
    viewModel: MyFriendListViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onFriendClicked: (MyFriendUI) -> Unit,
    goToSearchFriends: () -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.error.collectLatest { error ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == ON_RESUME) {
                viewModel.fetchMyFriends()
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
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
                    Text(
                        text = stringResource(R.string.my_friends_title),
                        style = PokeManiacTheme.typography.t1,
                        textAlign = TextAlign.Center,
                        color = PokeManiacTheme.colors.primaryText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PokeManiacTheme.colors.primaryText
                        )
                    }
                },
                actions = {
                    IconButton(onClick = goToSearchFriends) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            tint = PokeManiacTheme.colors.primaryText,
                            contentDescription = "Search Friends"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = PokeManiacTheme.colors.backgroundApp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(PokeManiacTheme.dimens.separator)
                    .background(color = PokeManiacTheme.colors.tertiary)
                    .padding(top = PokeManiacTheme.dimens.small)
            )

            when (val state = uiState) {
                Loading -> {
                    Spacer(Modifier.weight(1f))
                    GenericLoader(Modifier.align(CenterHorizontally))
                    Spacer(Modifier.weight(1f))
                }

                is Data -> {
                    MyFriendsListContent(
                        modifier = Modifier.padding(top = PokeManiacTheme.dimens.small),
                        friends = state.friends,
                        onFriendClicked = onFriendClicked,
                        onUnsubscribeFriend = viewModel::unsubscribeFriend
                    )
                }

                MyFriendListUiState.Empty -> {
                    Spacer(Modifier.weight(1f))
                    Text(
                        color = PokeManiacTheme.colors.primaryText,
                        style = PokeManiacTheme.typography.t3,
                        text = stringResource(R.string.no_friends_yet),
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
fun MyFriendsListContent(
    modifier: Modifier = Modifier,
    friends: List<MyFriendUI>,
    onFriendClicked: (MyFriendUI) -> Unit,
    onUnsubscribeFriend: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(PokeManiacTheme.dimens.xSmall)
    ) {
        items(friends) { friend ->
            FriendCard(friend, onFriendClicked, onUnsubscribeFriend)
        }
    }
}

@Composable
fun FriendCard(
    friend: MyFriendUI,
    onFriendClicked: (MyFriendUI) -> Unit,
    onUnsubscribeFriend: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PokeManiacTheme.dimens.xSmall),
        shape = RoundedCornerShape(PokeManiacTheme.dimens.xSmall),
        colors = CardDefaults.cardColors(containerColor = PokeManiacTheme.colors.backgroundCell),
        onClick = { onFriendClicked(friend) }
    ) {
        Row(
            modifier = Modifier
                .height(PokeManiacTheme.dimens.xxxLarge)
                .padding(horizontal = PokeManiacTheme.dimens.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(PokeManiacTheme.dimens.xxLarge),
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
                modifier = Modifier
                    .padding(PokeManiacTheme.dimens.small)
                    .weight(1f)
            )

            SecondaryButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = PokeManiacTheme.dimens.small, vertical = PokeManiacTheme.dimens.small),
                text = stringResource(R.string.unsubscribe)
            ) { onUnsubscribeFriend(friend.id) }
        }
    }
}