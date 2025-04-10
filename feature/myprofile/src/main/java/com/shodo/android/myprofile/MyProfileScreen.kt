package com.shodo.android.myprofile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.shodo.android.myprofile.ui.MyProfileView
import kotlinx.coroutines.flow.collectLatest

/**
 * MyProfileScreen is a container composable responsible for:
 * - Collecting the UI state from the MyProfileViewModel.
 * - Handling lifecycle events to trigger ViewModel actions.
 * - Displaying error messages using a Snackbar.
 * - Delegating the UI rendering to the stateless MyProfileView composable.
 *
 * @param modifier            Modifier to apply to the root of the screen.
 * @param viewModel           The ViewModel handling the logic and state for My Profile.
 * @param lifecycleOwner      The lifecycle owner to observe for automatic data refresh (default: current).
 * @param onPostTransactionPressed   Callback for navigating to the Post Transaction screen.
 * @param onBackPressed       Callback triggered when the back button is pressed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: MyProfileViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onBackPressed: () -> Unit,
    onPostTransactionPressed: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.error.collectLatest { error ->
            snackbarHostState.showSnackbar(error.message.toString())
        }
    }

    val uiState by viewModel.uiState.collectAsState()

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

    MyProfileView(
        modifier = modifier,
        uiState = uiState,
        onBackPressed = onBackPressed,
        onPostTransactionPressed = onPostTransactionPressed,
        snackbarHostState = snackbarHostState
    )
}