package com.shodo.android.posttransaction

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Right
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.shodo.android.coreui.theme.PokeManiacTheme
import com.shodo.android.posttransaction.Routes.Step1
import com.shodo.android.posttransaction.Routes.Step2
import com.shodo.android.posttransaction.di.postTransactionModule
import com.shodo.android.posttransaction.step1.ui.PostTransactionStep1View
import com.shodo.android.posttransaction.step2.ui.PostTransactionStep2View
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.GlobalContext.loadKoinModules
import kotlinx.serialization.Serializable

class PostTransactionActivity : ComponentActivity() {
    init {
        loadKoinModules(postTransactionModule)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeManiacTheme {
                PostTransactionFeature(onBackPressed = onBackPressedDispatcher::onBackPressed) { finish() }
            }
        }
    }
}

@Composable
fun PostTransactionFeature(onBackPressed: () -> Unit, onActivitySaved: () -> Unit) {
    val navController = rememberNavController()
    PostTransactionNavHost(navController = navController, onBackPressed = onBackPressed, onActivitySaved = onActivitySaved)
}

sealed class Routes {
    @Serializable
    data object Step1: Routes()

    @Serializable
    data class Step2(val uri: String): Routes()
}


@Composable
fun PostTransactionNavHost(modifier: Modifier = Modifier, navController: NavHostController, onBackPressed: () -> Unit, onActivitySaved: () -> Unit) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Step1
    ) {
        composable<Step1>(
            enterTransition = {
                when (initialState.destination.route) {
                    Step2::class.java.canonicalName -> slideIntoContainer(Left, animationSpec = tween(200))
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Step2::class.java.canonicalName -> slideOutOfContainer(Left, animationSpec = tween(200))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Step2::class.java.canonicalName -> slideIntoContainer(Right, animationSpec = tween(200))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    Step2::class.java.canonicalName -> slideOutOfContainer(Right, animationSpec = tween(200))
                    else -> null
                }
            }
        ) {
            PostTransactionStep1View(
                viewModel = koinViewModel(),
                onNextStep = { imageUri ->
                    navController.navigate(Step2(imageUri.toString()))
                },
                onBackPressed = onBackPressed
            )
        }

        composable<Step2>(
            enterTransition = {
                when (initialState.destination.route) {
                    Step1::class.java.canonicalName -> slideIntoContainer(Left, animationSpec = tween(200))
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Step1::class.java.canonicalName -> slideOutOfContainer(Left, animationSpec = tween(200))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Step1::class.java.canonicalName -> slideIntoContainer(Right, animationSpec = tween(200))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    Step1::class.java.canonicalName -> slideOutOfContainer(Right, animationSpec = tween(200))
                    else -> null
                }
            }
        ) { backStackEntry ->
            val step2Arg: Step2 = backStackEntry.toRoute()
            PostTransactionStep2View(
                viewModel = koinViewModel(),
                imageUri = step2Arg.uri.toUri(),
                onBackPressed = onBackPressed,
                onActivitySaved = onActivitySaved
            )
        }
    }
}