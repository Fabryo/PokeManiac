package com.shodo.android.myfriends

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.shodo.android.coreui.navigation.enterTransition
import com.shodo.android.coreui.navigation.exitTransition
import com.shodo.android.coreui.navigation.popEnterTransition
import com.shodo.android.coreui.navigation.popExitTransition
import com.shodo.android.coreui.theme.PokeManiacTheme
import com.shodo.android.myfriends.Routes.MyFriendDetail
import com.shodo.android.myfriends.Routes.MyFriendList
import com.shodo.android.myfriends.di.myFriendsModule
import com.shodo.android.myfriends.myfrienddetail.MyFriendDetailScreen
import com.shodo.android.myfriends.myfriendlist.MyFriendListScreen
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
            enterTransition = enterTransition(MyFriendDetail::class.java.canonicalName),
            exitTransition = exitTransition(MyFriendDetail::class.java.canonicalName),
            popEnterTransition = popEnterTransition(MyFriendDetail::class.java.canonicalName),
            popExitTransition = popExitTransition(MyFriendDetail::class.java.canonicalName)
        ) {
            MyFriendListScreen(
                modifier = Modifier,
                viewModel = koinViewModel(),
                onFriendPressed = { friend -> navController.navigate(MyFriendDetail(friend.id, friend.name)) },
                onSearchFriendsPressed = navigateToSearchFriend,
                onBackPressed = onBackPressed
            )
        }

        composable<MyFriendDetail>(
            enterTransition = enterTransition(MyFriendList::class.java.canonicalName),
            exitTransition = exitTransition(MyFriendList::class.java.canonicalName),
            popEnterTransition = popEnterTransition(MyFriendList::class.java.canonicalName),
            popExitTransition = popExitTransition(MyFriendList::class.java.canonicalName)
        ) { backStackEntry ->
            val myFriendDetail: MyFriendDetail = backStackEntry.toRoute()
            MyFriendDetailScreen(
                friendId = myFriendDetail.id,
                friendName = myFriendDetail.name,
                viewModel = koinViewModel(),
                onBackPressed = onBackPressed
            )
        }
    }
}