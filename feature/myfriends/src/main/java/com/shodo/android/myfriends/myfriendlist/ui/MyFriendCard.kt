package com.shodo.android.myfriends.myfriendlist.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.shodo.android.coreui.ui.SecondaryButton
import com.shodo.android.myfriends.uimodel.MyFriendUI
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MyFriendCard(
    friend: MyFriendUI,
    onFriendPressed: (MyFriendUI) -> Unit,
    onUnsubscribePressed: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.xSmall),
        shape = RoundedCornerShape(dimens.xSmall),
        colors = CardDefaults.cardColors(containerColor = colors.backgroundCell),
        onClick = { onFriendPressed(friend) }
    ) {
        Row(
            modifier = Modifier
                .height(dimens.xxxLarge)
                .padding(horizontal = dimens.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(dimens.xxLarge),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(friend.imageUrl)
                    .crossfade(true)
                    .build(),
                contentScale = Crop,
                contentDescription = friend.name,
            )

            Text(
                color = colors.primaryText,
                style = typography.t3,
                text = friend.name,
                modifier = Modifier
                    .padding(dimens.small)
                    .weight(1f)
            )

            SecondaryButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = dimens.small, vertical = dimens.small),
                text = stringResource(R.string.unsubscribe)
            ) { onUnsubscribePressed(friend.id) }
        }
    }
}

//region Previews

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "MyFriendCard - LightTheme")
@Composable
fun PreviewMyFriendCard_LightTheme() {
    PokeManiacTheme(darkTheme = false) {
        MyFriendCard(
            friend = MyFriendUI(
                id = "friendId",
                name = "friendName",
                imageUrl = "https://www.superherodb.com/pictures2/portraits/10/100/10831.jpg",
                description = "description",
                pokemonCards = persistentListOf()
            ),
            onFriendPressed = {},
            onUnsubscribePressed = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "MyFriendCard - DarkTheme")
@Composable
fun PreviewMyFriendCard_DarkTheme() {
    PokeManiacTheme(darkTheme = true) {
        MyFriendCard(
            friend = MyFriendUI(
                id = "friendId",
                name = "friendName",
                imageUrl = "https://www.superherodb.com/pictures2/portraits/10/100/10831.jpg",
                description = "description",
                pokemonCards = persistentListOf()
            ),
            onFriendPressed = {},
            onUnsubscribePressed = {}
        )
    }
}

//endregion Previews