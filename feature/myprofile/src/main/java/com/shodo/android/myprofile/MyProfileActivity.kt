package com.shodo.android.myprofile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import com.shodo.android.coreui.theme.PokeManiacTheme
import com.shodo.android.myprofile.di.myProfileModule
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.GlobalContext.loadKoinModules

class MyProfileActivity : ComponentActivity() {
    init {
        loadKoinModules(myProfileModule)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeManiacTheme {
                MyProfileScreen(
                    modifier = Modifier,
                    viewModel = koinViewModel(),
                    onBackPressed = onBackPressedDispatcher::onBackPressed,
                    onPostTransactionPressed = {
                        val intent = Intent().setClassName(packageName, "com.shodo.android.posttransaction.PostTransactionActivity")
                        startActivity(intent)
                    }
                )
            }
        }
    }
}