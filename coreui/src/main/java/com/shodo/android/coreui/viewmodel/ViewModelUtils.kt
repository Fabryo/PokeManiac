package com.shodo.android.coreui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun ViewModel.launchStoreCall(
    onLoading: suspend () -> Unit,
    load: suspend () -> Unit,
    onError: suspend (Exception) -> Unit
): Job {
    return viewModelScope.launch {
        try {
            onLoading()
            load()
        } catch (exception: Exception) {
            onError(exception)
        }
    }
}