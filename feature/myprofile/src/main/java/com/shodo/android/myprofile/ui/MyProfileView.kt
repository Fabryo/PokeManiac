package com.shodo.android.myprofile.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
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
import com.shodo.android.myprofile.MyProfileUiState.Data
import com.shodo.android.myprofile.MyProfileUiState.Loading
import com.shodo.android.myprofile.MyProfileViewModel
import com.shodo.android.myprofile.uimodel.MyProfilePokemonCardUI
import com.shodo.android.myprofile.uimodel.MyProfileUI
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileView(
    modifier: Modifier = Modifier,
    viewModel: MyProfileViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onBackPressed: () -> Unit,
    onPostTransaction: () -> Unit
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
                viewModel.start()
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
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PokeManiacTheme.colors.primaryText
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onPostTransaction) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            tint = PokeManiacTheme.colors.primaryText,
                            contentDescription = "Search Friends"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        when (val state = uiState) {
            is Data -> MyProfileContent(
                Modifier
                    .background(color = PokeManiacTheme.colors.backgroundApp)
                    .fillMaxSize()
                    .padding(innerPadding), state.profile
            )

            Loading -> Column(
                modifier = Modifier
                    .background(color = PokeManiacTheme.colors.backgroundApp)
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                GenericLoader()
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun MyProfileContent(modifier: Modifier = Modifier, profile: MyProfileUI) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        profile.imageUrl?.let { imareUrl ->
            AsyncImage(
                modifier = Modifier
                    .padding(top = PokeManiacTheme.dimens.xxLarge)
                    .clip(CircleShape)
                    .size(PokeManiacTheme.dimens.xxxxLarge),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imareUrl)
                    .crossfade(true)
                    .build(),
                contentScale = Crop,
                contentDescription = profile.name
            )
        } ?: run {
            Image(
                modifier = Modifier
                    .padding(top = PokeManiacTheme.dimens.large)
                    .clip(CircleShape)
                    .size(PokeManiacTheme.dimens.xxxLarge),
                painter = painterResource(id = R.drawable.pokemaniac),
                contentDescription = profile.name
            )
        }

        Text(
            modifier = Modifier.padding(
                top = PokeManiacTheme.dimens.small,
                start = PokeManiacTheme.dimens.standard,
                end = PokeManiacTheme.dimens.standard
            ),
            color = PokeManiacTheme.colors.primaryText,
            style = PokeManiacTheme.typography.t4,
            text = profile.name ?: stringResource(R.string.default_username),
        )

        Text(
            modifier = Modifier.padding(
                top = PokeManiacTheme.dimens.large,
                start = PokeManiacTheme.dimens.standard,
                end = PokeManiacTheme.dimens.standard
            ).align(Alignment.CenterHorizontally),
            color = PokeManiacTheme.colors.primaryText,
            style = PokeManiacTheme.typography.t5,
            text = profile.pokemonCards.sumOf { it.totalVotes }.takeIf { it > 0 }?.let {
                stringResource(R.string.my_profile_total_votes, it)
            } ?: stringResource(R.string.my_profile_total_votes_none),
        )

        if (profile.pokemonCards.isEmpty()) {
            Spacer(Modifier.weight(1f))
            Text(
                color = PokeManiacTheme.colors.primaryText,
                style = PokeManiacTheme.typography.t8,
                text = stringResource(R.string.my_profile_no_activity_yet),
                modifier = Modifier.padding(PokeManiacTheme.dimens.small)
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
            MyActivitiesContent(
                modifier = Modifier
                    .padding(top = PokeManiacTheme.dimens.standard)
                    .weight(1f),
                pokemonCards = profile.pokemonCards
            )
        }
    }
}

@Composable
fun MyActivitiesContent(modifier: Modifier = Modifier, pokemonCards: List<MyProfilePokemonCardUI>) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = PokeManiacTheme.dimens.minGridUserCellSize),
        contentPadding = PaddingValues(PokeManiacTheme.dimens.xSmall)
    ) {
        items(pokemonCards) { card ->
            Image(
                modifier = Modifier.clip(RectangleShape),
                painter = rememberAsyncImagePainter(card.imageUri),
                contentDescription = null
            )
        }
    }
}