package com.shodo.android.myfriends

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Right
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.shodo.android.coreui.theme.PokeManiacTheme
import com.shodo.android.myfriends.Routes.MyFriendDetail
import com.shodo.android.myfriends.Routes.MyFriendList
import com.shodo.android.myfriends.di.myFriendsModule
import com.shodo.android.myfriends.myfrienddetail.ui.MyFriendDetailView
import com.shodo.android.myfriends.myfriendlist.ui.MyFriendListView
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.GlobalContext.loadKoinModules
import kotlinx.serialization.Serializable

class MyFriendsActivity : ComponentActivity() {
    init {
        loadKoinModules(myFriendsModule)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeManiacTheme {
                MyFriendsFeature(onBackPressed = onBackPressedDispatcher::onBackPressed) {
                    val intent = Intent().setClassName(packageName, "com.shodo.android.searchfriend.SearchFriendActivity")
                    startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun MyFriendsFeature(onBackPressed: () -> Unit, navigateToSearchFriend: () -> Unit) {
    val navController = rememberNavController()
    MyFriendsNavHost(navController = navController, navigateToSearchFriend = navigateToSearchFriend, onBackPressed = onBackPressed)
}

sealed class Routes {
    @Serializable
    data object MyFriendList: Routes()

    @Serializable
    data class MyFriendDetail(val id: String, val name: String): Routes()
}

@Composable
fun MyFriendsNavHost(modifier: Modifier = Modifier, navController: NavHostController, navigateToSearchFriend: () -> Unit, onBackPressed: () -> Unit) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MyFriendList
    ) {
        composable<MyFriendList>(
            enterTransition = {
                when (initialState.destination.route) {
                    MyFriendDetail::class.java.canonicalName -> slideIntoContainer(Left, animationSpec = tween(200))
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    MyFriendDetail::class.java.canonicalName -> slideOutOfContainer(Left, animationSpec = tween(200))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    MyFriendDetail::class.java.canonicalName -> slideIntoContainer(Right, animationSpec = tween(200))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    MyFriendDetail::class.java.canonicalName -> slideOutOfContainer(Right, animationSpec = tween(200))
                    else -> null
                }
            }
        ) {
            MyFriendListView(
                modifier = Modifier,
                viewModel = koinViewModel(),
                onFriendClicked = { friend ->
                    navController.navigate(MyFriendDetail(friend.id, friend.name))
                },
                goToSearchFriends = navigateToSearchFriend,
                onBackPressed = onBackPressed
            )
        }

        composable<MyFriendDetail>(
            enterTransition = {
                when (initialState.destination.route) {
                    MyFriendList::class.java.canonicalName -> slideIntoContainer(Left, animationSpec = tween(200))
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    MyFriendList::class.java.canonicalName -> slideOutOfContainer(Left, animationSpec = tween(200))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    MyFriendList::class.java.canonicalName -> slideIntoContainer(Right, animationSpec = tween(200))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    MyFriendList::class.java.canonicalName -> slideOutOfContainer(Right, animationSpec = tween(200))
                    else -> null
                }
            }
        ) { backStackEntry ->
            val myFriendDetail: MyFriendDetail = backStackEntry.toRoute()
            MyFriendDetailView(
                friendId = myFriendDetail.id,
                friendName = myFriendDetail.name,
                viewModel = koinViewModel(),
                onBackPressed = onBackPressed
            )
        }
    }
}