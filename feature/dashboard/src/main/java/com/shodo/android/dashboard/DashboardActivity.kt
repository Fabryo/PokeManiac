package com.shodo.android.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shodo.android.coreui.theme.PokeManiacTheme
import com.shodo.android.dashboard.di.dashboardModule
import com.shodo.android.dashboard.ui.DashboardView
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
                DashboardView(
                    viewModel = koinViewModel(),
                    goToFriends = {
                        val intent = Intent().setClassName(packageName, "com.shodo.android.myfriends.MyFriendsActivity")
                        startActivity(intent)
                    },
                    goToSearchFriends = {
                        val intent = Intent().setClassName(packageName, "com.shodo.android.searchfriend.SearchFriendActivity")
                        startActivity(intent)
                    },
                    goToProfile = {
                        val intent = Intent().setClassName(packageName, "com.shodo.android.myprofile.MyProfileActivity")
                        startActivity(intent)
                    },
                    goToPostTransaction = {
                        val intent = Intent().setClassName(packageName, "com.shodo.android.posttransaction.PostTransactionActivity")
                        startActivity(intent)
                    }
                )
            }
        }
    }
}