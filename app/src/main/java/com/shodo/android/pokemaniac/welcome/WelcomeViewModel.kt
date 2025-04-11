package com.shodo.android.pokemaniac.welcome

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shodo.android.coreui.navigator.DashboardNavigator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val dashboardNavigator: DashboardNavigator
): ViewModel() {

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    fun onSignUpClicked() {
        viewModelScope.launch {
            _errorMessage.emit("Nothing implemented yet")
        }
    }
    fun navigateToDashboard(context: Context) {
        dashboardNavigator.navigate(context)
    }
}