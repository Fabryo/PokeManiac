package com.shodo.android.myfriends.myfriendlist

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.shodo.android.myfriends.myfriendlist.ui.MyFriendListView
import com.shodo.android.myfriends.uimodel.MyFriendUI
import kotlinx.coroutines.flow.collectLatest

/**
 * MyFriendListScreen is a container composable responsible for:
 * - Collecting the UI state from the MyFriendListViewModel.
 * - Handling lifecycle events to trigger ViewModel actions.
 * - Displaying error messages using a Snackbar.
 * - Delegating the UI rendering to the stateless MyFriendListView composable.
 *
 * @param modifier            Modifier to apply to the root of the screen.
 * @param viewModel           The ViewModel handling the logic and state for the friend list.
 * @param lifecycleOwner      The lifecycle owner to observe for automatic data refresh (default: current).
 * @param onFriendPressed     Callback triggered when a friend item is clicked.
 * @param onBackPressed       Callback triggered when the back button is pressed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFriendListScreen(
    modifier: Modifier = Modifier,
    viewModel: MyFriendListViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onFriendPressed: (MyFriendUI) -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.error.collectLatest { error ->
            snackbarHostState.showSnackbar(error.message.toString())
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == ON_START) {
                viewModel.fetchMyFriends()
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    MyFriendListView(
        modifier = modifier,
        uiState = uiState,
        onFriendPressed = onFriendPressed,
        onSearchFriendsPressed = { viewModel.navigateToSearchFriend(context) },
        onUnsubscribePressed = viewModel::unsubscribeFriend,
        onBackPressed = onBackPressed,
        snackbarHostState = snackbarHostState
    )
}