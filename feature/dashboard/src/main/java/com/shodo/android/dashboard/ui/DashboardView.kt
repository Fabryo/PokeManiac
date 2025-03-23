package com.shodo.android.dashboard.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.PeopleAlt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.shodo.android.coreui.R
import com.shodo.android.coreui.theme.PokeManiacTheme
import com.shodo.android.coreui.ui.GenericLoader
import com.shodo.android.dashboard.DashboardUiState.Data
import com.shodo.android.dashboard.DashboardUiState.EmptyResult
import com.shodo.android.dashboard.DashboardUiState.Loading
import com.shodo.android.dashboard.DashboardViewModel
import com.shodo.android.dashboard.uimodel.ImageSourceUI
import com.shodo.android.dashboard.uimodel.NewActivityTypeUI.Purchase
import com.shodo.android.dashboard.uimodel.NewActivityTypeUI.Sale
import com.shodo.android.dashboard.uimodel.NewActivityUI
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardView(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    goToFriends: () -> Unit,
    goToProfile: () -> Unit,
    goToSearchFriends: () -> Unit,
    goToPostTransaction: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.error.collectLatest { error ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == ON_START) {
                viewModel.start()
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }


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
                        text = stringResource(R.string.dashboard_title),
                        style = PokeManiacTheme.typography.t1,
                        textAlign = TextAlign.Center,
                        color = PokeManiacTheme.colors.primaryText
                    )
                },
                actions = {
                    IconButton(onClick = goToSearchFriends) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            tint = PokeManiacTheme.colors.primaryText,
                            contentDescription = "Search Friends"
                        )
                    }
                    IconButton(onClick = goToFriends) {
                        Icon(
                            imageVector = Icons.Outlined.PeopleAlt,
                            tint = PokeManiacTheme.colors.primaryText,
                            contentDescription = "Friends"
                        )
                    }
                    IconButton(onClick = goToProfile) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            tint = PokeManiacTheme.colors.primaryText,
                            contentDescription = "Profile"
                        )
                    }
                    IconButton(onClick = goToPostTransaction) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            tint = PokeManiacTheme.colors.primaryText,
                            contentDescription = "Profile"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState is Loading,
            onRefresh = { viewModel.refreshNewsFeed() },
            modifier = modifier
        ) {
            when (val state = uiState) {
                is Data -> DashboardContent(state.news, innerPadding)
                Loading -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PokeManiacTheme.colors.backgroundApp),
                    contentAlignment = Alignment.Center
                ) {
                    GenericLoader()
                }

                EmptyResult -> Column(modifier = Modifier
                    .fillMaxSize()
                    .background(PokeManiacTheme.colors.backgroundApp)) {
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
fun DashboardContent(newActivies: List<NewActivityUI>, innerPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .background(PokeManiacTheme.colors.backgroundApp)
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(PokeManiacTheme.dimens.small),
        horizontalAlignment = CenterHorizontally
    ) {
        items(newActivies) { newActivity ->
            NewActivityCard(newActivity)
        }
        item {
            CircularProgressIndicator(color = PokeManiacTheme.colors.primaryText)
            Text(
                modifier = Modifier.padding(PokeManiacTheme.dimens.standard),
                color = PokeManiacTheme.colors.primaryText,
                style = PokeManiacTheme.typography.t5,
                text = "Here it's just to show that the Newsfeed should be loading some news"
            )
        }
    }
}

@Composable
fun NewActivityCard(newActivity: NewActivityUI) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = PokeManiacTheme.colors.backgroundCell)
    ) {
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            Row(
                modifier = Modifier.padding(
                    start = PokeManiacTheme.dimens.small,
                    top = PokeManiacTheme.dimens.small,
                    end = PokeManiacTheme.dimens.small
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(PokeManiacTheme.dimens.xLarge),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(newActivity.friendImageUrl)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = newActivity.friendName,
                )
                Column(
                    modifier = Modifier.padding(
                        start = PokeManiacTheme.dimens.small,
                        top = PokeManiacTheme.dimens.small,
                        end = PokeManiacTheme.dimens.small
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(start = PokeManiacTheme.dimens.small, end = PokeManiacTheme.dimens.small),
                        color = PokeManiacTheme.colors.primaryText,
                        style = PokeManiacTheme.typography.t4,
                        text = newActivity.friendName
                    )
                    Text(
                        modifier = Modifier.padding(
                            start = PokeManiacTheme.dimens.small,
                            top = PokeManiacTheme.dimens.xxSmall,
                            end = PokeManiacTheme.dimens.small
                        ),
                        color = PokeManiacTheme.colors.primaryText,
                        style = PokeManiacTheme.typography.t5,
                        text = newActivity.date
                    )
                }
            }

            Text(
                modifier = Modifier
                    .padding(horizontal = PokeManiacTheme.dimens.small, vertical = PokeManiacTheme.dimens.standard)
                    .align(CenterHorizontally),
                color = PokeManiacTheme.colors.primaryText,
                style = PokeManiacTheme.typography.t6,
                text = when (newActivity.activityType) {
                    Purchase -> stringResource(R.string.activity_purchase, newActivity.pokemonCard.name, newActivity.price)
                    Sale -> stringResource(R.string.activity_sale, newActivity.pokemonCard.name, newActivity.price)
                }
            )
            when (val source = newActivity.pokemonCard.imageSource) {
                is ImageSourceUI.UrlSource -> AsyncImage(
                    modifier = Modifier
                        .padding(top = PokeManiacTheme.dimens.small)
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(source.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = newActivity.pokemonCard.name,
                )

                is ImageSourceUI.FileSource -> Image(
                    modifier = Modifier
                        .padding(top = PokeManiacTheme.dimens.small)
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    painter = rememberAsyncImagePainter(source.fileUri),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
            // TODO Number of likes
            // TODO Button to like
            // TODO Comments section
        }
    }
}