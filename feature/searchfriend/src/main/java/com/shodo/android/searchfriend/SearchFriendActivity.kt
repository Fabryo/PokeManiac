package com.shodo.android.searchfriend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import com.shodo.android.coreui.theme.PokeManiacTheme
import com.shodo.android.searchfriend.di.searchFriendModule
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.GlobalContext.loadKoinModules
import androidx.activity.compose.setContent

class SearchFriendActivity : ComponentActivity() {
    init {
        loadKoinModules(searchFriendModule)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeManiacTheme {
                SearchFriendScreen(
                    modifier = Modifier,
                    viewModel = koinViewModel(),
                    onBackPressed = onBackPressedDispatcher::onBackPressed
                )
            }
        }
    }
}