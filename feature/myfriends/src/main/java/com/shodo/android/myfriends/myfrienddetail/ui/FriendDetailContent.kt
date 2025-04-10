package com.shodo.android.myfriends.myfrienddetail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.shodo.android.coreui.R
import com.shodo.android.coreui.theme.PokeManiacTheme
import com.shodo.android.coreui.theme.PokeManiacTheme.colors
import com.shodo.android.coreui.theme.PokeManiacTheme.dimens
import com.shodo.android.coreui.theme.PokeManiacTheme.typography
import com.shodo.android.coreui.ui.GenericEmptyScreen
import com.shodo.android.coreui.ui.SecondaryButton
import com.shodo.android.myfriends.uimodel.MyFriendPokemonCardUI
import com.shodo.android.myfriends.uimodel.MyFriendUI
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun FriendDetailContent(
    friend: MyFriendUI,
    onUnsubscribePressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = colors.backgroundApp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        FriendDetailHeader(friend.imageUrl, friend.name, onUnsubscribePressed)
        FriendDescriptions(friend.description, friend.pokemonCards)
        FriendCardsContent(friend.name, friend.pokemonCards, Modifier.weight(1f))
    }
}

@Composable
private fun ColumnScope.FriendDetailHeader(
    friendImageUrl: String,
    friendName: String,
    onUnsubscribePressed: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.standard),
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(dimens.xxxLarge),
            model = ImageRequest.Builder(LocalContext.current)
                .data(friendImageUrl)
                .crossfade(true)
                .build(),
            contentScale = Crop,
            contentDescription = friendName
        )

        SecondaryButton(
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = dimens.small),
            text = stringResource(R.string.unsubscribe),
            onClick = onUnsubscribePressed
        )
    }
}

@Composable
private fun ColumnScope.FriendDescriptions(friendDescription: String, friendPokemonCards: PersistentList<MyFriendPokemonCardUI>) {
    if (friendDescription.length > 1) { // To avoid the descriptions of 1 character
        Text(
            modifier = Modifier.padding(top = dimens.standard, start = dimens.standard, end = dimens.standard),
            color = colors.primaryText,
            style = typography.t8,
            text = friendDescription,
        )
    }
    Text(
        modifier = Modifier.padding(top = dimens.xSmall, start = dimens.standard, end = dimens.standard),
        color = colors.primaryText,
        style = typography.t5,
        text = friendPokemonCards.sumOf { it.totalVotes }.takeIf { it > 0 }?.let {
            stringResource(R.string.my_friend_detail_total_votes, it)
        } ?: stringResource(R.string.my_friend_detail_total_votes_none),
    )
}

@Composable
private fun ColumnScope.FriendCardsContent(friendName: String, friendPokemonCards: PersistentList<MyFriendPokemonCardUI>, modifier: Modifier = Modifier) {
    if (friendPokemonCards.isEmpty()) {
        GenericEmptyScreen(
            text = stringResource(R.string.my_friend_detail_no_activity_yet, friendName),
            modifier = modifier
        )
    } else {
        // Separator
        Box(
            modifier = Modifier
                .padding(top = dimens.small)
                .fillMaxWidth()
                .height(dimens.separator)
                .background(color = colors.tertiary)
        )
        FriendActivities(
            modifier = modifier.padding(top = dimens.standard),
            pokemonCards = friendPokemonCards
        )
    }
}

@Composable
private fun FriendActivities(pokemonCards: PersistentList<MyFriendPokemonCardUI>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = dimens.minGridUserCellSize),
        contentPadding = PaddingValues(dimens.xSmall)
    ) {
        items(
            items = pokemonCards,
            key = { it.id }
        ) { card ->
            AsyncImage(
                modifier = Modifier.clip(RectangleShape),
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

//region Previews

//region FriendDetailContent

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "FriendDetailContent - With Posts - LightTheme")
@Composable
private fun PreviewFriendDetailContent_WithPosts_LightTheme() {
    PreviewFriendDetailContent(
        darkTheme = false,
        pokemonCards = previewPokemonCards()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "FriendDetailContent - With Posts - DarkTheme")
@Composable
private fun PreviewFriendDetailContent_WithPosts_DarkTheme() {
    PreviewFriendDetailContent(
        darkTheme = true,
        pokemonCards = previewPokemonCards()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "FriendDetailContent - No Posts - LightTheme")
@Composable
private fun PreviewFriendDetailContent_NoPosts_LightTheme() {
    PreviewFriendDetailContent(
        darkTheme = false,
        pokemonCards = persistentListOf()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "FriendDetailContent - No Posts - DarkTheme")
@Composable
fun PreviewFriendDetailContent_NoPosts_DarkTheme() {
    PreviewFriendDetailContent(
        darkTheme = true,
        pokemonCards = persistentListOf()
    )
}

@Composable
private fun PreviewFriendDetailContent(darkTheme: Boolean, pokemonCards: PersistentList<MyFriendPokemonCardUI>) {
    PokeManiacTheme(darkTheme = darkTheme) {
        FriendDetailContent(
            friend = MyFriendUI(
                id = "friendId",
                name = "friendName",
                imageUrl = "https://www.superherodb.com/pictures2/portraits/10/100/10831.jpg",
                description = "description",
                pokemonCards = pokemonCards
            ),
            onUnsubscribePressed = {}
        )
    }
}

//endregion FriendDetailContent

//region FriendDetailHeader

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "FriendDetailHeader - LightTheme")
@Composable
fun PreviewFriendDetailHeader_LightTheme() {
    PokeManiacTheme(darkTheme = false) {
        Column(
            modifier = Modifier.background(color = colors.backgroundApp)
        ) {
            FriendDetailHeader(
                friendImageUrl = "https://www.superherodb.com/pictures2/portraits/10/100/10831.jpg",
                friendName = "friendName",
                onUnsubscribePressed = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "FriendDetailHeader - DarkTheme")
@Composable
fun PreviewFriendDetailHeader_DarkTheme() {
    PokeManiacTheme(darkTheme = true) {
        Column(
            modifier = Modifier.background(color = colors.backgroundApp)
        ) {
            FriendDetailHeader(
                friendImageUrl = "https://www.superherodb.com/pictures2/portraits/10/100/10831.jpg",
                friendName = "friendName",
                onUnsubscribePressed = {}
            )
        }
    }
}

//endregion FriendDetailHeader

//region FriendDescriptions

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "FriendDescriptions - With Description - With Votes - LightTheme")
@Composable
fun PreviewFriendDescriptions_WithDescription_WithVotes_LightTheme() {
    PreviewFriendDescriptions(
        darkTheme = false,
        description = "description",
        friendPokemonCards = previewPokemonCards()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "FriendDescriptions - With Description - With Votes - DarkTheme")
@Composable
fun PreviewFriendDescriptions_WithDescription_WithVotes_DarkTheme() {
    PreviewFriendDescriptions(
        darkTheme = true,
        description = "description",
        friendPokemonCards = previewPokemonCards()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "FriendDescriptions - No Description - No Votes - LightTheme")
@Composable
fun PreviewFriendDescriptions_NoDescription_NoVotes_LightTheme() {
    PreviewFriendDescriptions(
        darkTheme = false,
        description = "-",
        friendPokemonCards = persistentListOf()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "FriendDescriptions - No Description - No Votes - DarkTheme")
@Composable
fun PreviewFriendDescriptions_NoDescription_NoVotes_DarkTheme() {
    PreviewFriendDescriptions(
        darkTheme = true,
        description = "-",
        friendPokemonCards = persistentListOf()
    )
}

@Composable
private fun PreviewFriendDescriptions(darkTheme: Boolean, description: String, friendPokemonCards: PersistentList<MyFriendPokemonCardUI>) {
    PokeManiacTheme(darkTheme = darkTheme) {
        Column(
            modifier = Modifier.background(color = colors.backgroundApp).fillMaxWidth()
        ) {
            FriendDescriptions(
                friendDescription = description,
                friendPokemonCards = friendPokemonCards
            )
        }
    }
}

//endregion FriendDescriptions

//region FriendCardsContent

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "FriendCardsContent - No PokemonCards - LightTheme")
@Composable
fun PreviewFriendCardsContent_NoPokemonCards_LightTheme() {
    PokeManiacTheme(darkTheme = false) {
        Column(
            modifier = Modifier.background(color = colors.backgroundApp)
        ) {
            FriendCardsContent(
                friendName = "friendName",
                friendPokemonCards = persistentListOf()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "FriendCardsContent - No PokemonCards - DarkTheme")
@Composable
fun PreviewFriendCardsContent_NoPokemonCards_DarkTheme() {
    PokeManiacTheme(darkTheme = true) {
        Column(
            modifier = Modifier.background(color = colors.backgroundApp)
        ) {
            FriendCardsContent(
                friendName = "friendName",
                friendPokemonCards = persistentListOf()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "FriendCardsContent - With PokemonCards - LightTheme")
@Composable
fun PreviewFriendCardsContent_WithPokemonCards_LightTheme() {
    PokeManiacTheme(darkTheme = false) {
        Column(
            modifier = Modifier.background(color = colors.backgroundApp).fillMaxSize()
        ) {
            FriendCardsContent(
                friendName = "friendName",
                friendPokemonCards = previewPokemonCards()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "FriendCardsContent - With PokemonCards - DarkTheme")
@Composable
fun PreviewFriendCardsContent_WithPokemonCards_DarkTheme() {
    PokeManiacTheme(darkTheme = true) {
        Column(
            modifier = Modifier.background(color = colors.backgroundApp).fillMaxSize()
        ) {
            FriendCardsContent(
                friendName = "friendName",
                friendPokemonCards = previewPokemonCards()
            )
        }
    }
}

//endregion FriendCardsContent

private fun previewPokemonCards() = persistentListOf(
    MyFriendPokemonCardUI(
        id = "Ivysaur" + "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png",
        pokemonId = 2,
        totalVotes = 19,
        hasMyVote = false,
        name = "Ivysaur",
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png",
    ),
    MyFriendPokemonCardUI(
        id = "Squirtle" + "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/7.png",
        pokemonId = 7,
        totalVotes = 4,
        hasMyVote = false,
        name = "Squirtle",
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/7.png"
    )
)
//endregion Previews