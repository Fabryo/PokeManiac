package com.shodo.android.pokemaniac.welcome

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.shodo.android.pokemaniac.welcome.ui.WelcomeView
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel,
    onNavigationDone: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.errorMessage.collectLatest { errorMessage ->
            snackbarHostState.showSnackbar(errorMessage)
        }
    }

    WelcomeView(
        modifier = modifier,
        onSignUpClicked = viewModel::onSignUpClicked,
        onSignInClicked = {
            viewModel.navigateToDashboard(context)
            onNavigationDone()
        },
        snackbarHostState = snackbarHostState
    )
}
