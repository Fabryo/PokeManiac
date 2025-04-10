package com.shodo.android.dashboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.shodo.android.coreui.theme.PokeManiacTheme
import com.shodo.android.coreui.theme.PokeManiacTheme.colors
import com.shodo.android.coreui.theme.PokeManiacTheme.dimens
import com.shodo.android.coreui.theme.PokeManiacTheme.typography
import com.shodo.android.dashboard.uimodel.ImageSourceUI
import com.shodo.android.dashboard.uimodel.NewActivityTypeUI
import com.shodo.android.dashboard.uimodel.NewActivityUI
import com.shodo.android.dashboard.uimodel.PokemonCardUI
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun DashboardContent(newActivies: PersistentList<NewActivityUI>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colors.backgroundApp),
        verticalArrangement = Arrangement.spacedBy(dimens.small),
        horizontalAlignment = CenterHorizontally
    ) {
        items(
            items = newActivies,
            key = { it.id }
        ) { newActivity ->
            NewActivityCard(newActivity)
        }
        item(key = "LOAD_MORE_KEY") {
            CircularProgressIndicator(
                modifier = Modifier.padding(top = dimens.standard),
                color = colors.primaryText
            )
            Text(
                modifier = Modifier.padding(dimens.standard),
                color = colors.primaryText,
                style = typography.t5,
                text = "Here it's just to show that the Newsfeed should be loading some news"
            )
        }
    }
}

//region Previews

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "DashboardContent - LightTheme")
@Composable
fun PreviewDashboardContent_LightTheme() {
    PokeManiacTheme(darkTheme = false) {
        DashboardContent(
            newActivies = previewNewActivities(),
            modifier = Modifier
                .fillMaxSize()
                .background(colors.backgroundApp)
                .padding()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "DashboardContent - DarkTheme")
@Composable
fun PreviewDashboardContent_DarkTheme() {
    PokeManiacTheme(darkTheme = true) {
        DashboardContent(
            newActivies = previewNewActivities(),
            modifier = Modifier
                .fillMaxSize()
                .background(colors.backgroundApp)
                .padding()
        )
    }
}

private fun previewNewActivities() = persistentListOf(
    NewActivityUI(
        id = "friendName" + "01/06/2025 18:30",
        friendName = "friendName",
        friendImageUrl = null,
        date = "01/06/2025 18:30",
        activityType = NewActivityTypeUI.Sale,
        pokemonCard = PokemonCardUI(
            name = "pokemonName",
            imageSource = ImageSourceUI.UrlSource("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/7.png")
        ),
        price = 30
    ),
    NewActivityUI(
        id = "friendName2" + "01/06/2025 12:30",
        friendName = "friendName2",
        friendImageUrl = null,
        date = "01/06/2025 12:30",
        activityType = NewActivityTypeUI.Purchase,
        pokemonCard = PokemonCardUI(
            name = "pokemonName",
            imageSource = ImageSourceUI.UrlSource("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png")
        ),
        price = 30
    )
)
//endregion Previews