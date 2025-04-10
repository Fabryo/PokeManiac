package com.shodo.android.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shodo.android.coreui.theme.PokeManiacTheme
import com.shodo.android.dashboard.di.dashboardModule
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.GlobalContext.loadKoinModules

class DashboardActivity : ComponentActivity() {
    init {
        loadKoinModules(dashboardModule)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeManiacTheme {
                DashboardScreen(
                    viewModel = koinViewModel(),
                    onFriendsPressed = {
                        val intent = Intent().setClassName(packageName, "com.shodo.android.myfriends.MyFriendsActivity")
                        startActivity(intent)
                    },
                    onSearchFriendsPressed = {
                        val intent = Intent().setClassName(packageName, "com.shodo.android.searchfriend.SearchFriendActivity")
                        startActivity(intent)
                    },
                    onProfilePressed = {
                        val intent = Intent().setClassName(packageName, "com.shodo.android.myprofile.MyProfileActivity")
                        startActivity(intent)
                    },
                    onPostTransactionPressed = {
                        val intent = Intent().setClassName(packageName, "com.shodo.android.posttransaction.PostTransactionActivity")
                        startActivity(intent)
                    }
                )
            }
        }
    }
}