package com.shodo.android.pokemaniac

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shodo.android.pokemaniac.ui.LogInView
import com.shodo.android.coreui.theme.PokeManiacTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeManiacTheme {
                LogInView(
                    onSignInClicked = {
                        val intent = Intent().setClassName(packageName, "com.shodo.android.dashboard.DashboardActivity")
                        startActivity(intent)
                        finish()
                    },
                    onSignUpClicked = {
                      // TODO go to subscription screen
                    }
                )
            }
        }
    }
}
