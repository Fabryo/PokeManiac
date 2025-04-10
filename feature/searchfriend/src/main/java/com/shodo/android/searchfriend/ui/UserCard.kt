package com.shodo.android.searchfriend.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.shodo.android.coreui.R
import com.shodo.android.coreui.theme.PokeManiacTheme.colors
import com.shodo.android.coreui.theme.PokeManiacTheme.dimens
import com.shodo.android.coreui.theme.PokeManiacTheme.typography
import com.shodo.android.coreui.ui.PrimaryButton
import com.shodo.android.coreui.ui.SecondaryButton
import com.shodo.android.searchfriend.uimodel.SearchFriendUI
import com.shodo.android.searchfriend.uimodel.SubscriptionState
import com.shodo.android.searchfriend.uimodel.SubscriptionState.NotSubscribed
import com.shodo.android.searchfriend.uimodel.SubscriptionState.Subscribed
import com.shodo.android.searchfriend.uimodel.SubscriptionState.UpdatingSubscribe

@Composable
fun UserCard(
    user: SearchFriendUI,
    onSubscribeUserPressed: (String) -> Unit,
    onUnsubscribeUserPressed: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.xxSmall),
        shape = RoundedCornerShape(dimens.small),
        colors = CardDefaults.cardColors(containerColor = colors.backgroundCell),
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalAlignment = CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(top = dimens.standard)
                    .clip(CircleShape)
                    .wrapContentSize()
                    .size(dimens.xxxLarge),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.imageUrl)
                    .crossfade(true)
                    .build(),
                contentScale = Crop,
                contentDescription = user.name,
            )

            Text(
                color = colors.primaryText,
                style = typography.t3,
                text = user.name,
                modifier = Modifier.padding(
                    top = dimens.small,
                    start = dimens.small,
                    end = dimens.small
                )
            )

            SubscriptionStateContent(
                subscriptionState = user.subscriptionState,
                onUnsubscribeUserPressed =  { onUnsubscribeUserPressed(user.id) },
                onSubscribeUserPressed = { onSubscribeUserPressed(user.id) }
            )
        }
    }
}

@Composable
private fun ColumnScope.SubscriptionStateContent(
    subscriptionState: SubscriptionState,
    onUnsubscribeUserPressed: () -> Unit,
    onSubscribeUserPressed: () -> Unit
) {
    when (subscriptionState) {
        Subscribed -> SecondaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.small, vertical = dimens.small),
            text = stringResource(R.string.unsubscribe)
        ) { onUnsubscribeUserPressed() }

        NotSubscribed -> PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.small, vertical = dimens.small),
            text = stringResource(R.string.subscribe),
            onClick = { onSubscribeUserPressed() }
        )

        UpdatingSubscribe -> CircularProgressIndicator(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(vertical = dimens.small),
            color = colors.primaryText
        )
    }
}
