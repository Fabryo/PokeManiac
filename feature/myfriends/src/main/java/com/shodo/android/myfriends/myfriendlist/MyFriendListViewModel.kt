package com.shodo.android.myfriends.myfriendlist

import androidx.lifecycle.ViewModel
import com.shodo.android.coreui.viewmodel.launchStoreCall
import com.shodo.android.domain.repositories.entities.User
import com.shodo.android.domain.usecases.UseCase
import com.shodo.android.domain.usecases.UseCase.Companion.await
import com.shodo.android.myfriends.myfriendlist.MyFriendListUiState.Data
import com.shodo.android.myfriends.myfriendlist.MyFriendListUiState.Empty
import com.shodo.android.myfriends.myfriendlist.MyFriendListUiState.Loading
import com.shodo.android.myfriends.uimodel.MyFriendUI
import com.shodo.android.myfriends.uimodel.mapToUI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class MyFriendListUiState {
    data object Loading: MyFriendListUiState()
    data class Data(val friends: List<MyFriendUI>): MyFriendListUiState()
    data object Empty: MyFriendListUiState()
}

class MyFriendListViewModel(
    private val getMyFriends: UseCase<Nothing?, List<User>>,
    private val unsubscribeFriend: UseCase<String, User>
): ViewModel() {

    private val _uiState: MutableStateFlow<MyFriendListUiState> = MutableStateFlow(Loading)
    val uiState = _uiState.asStateFlow()

    private val _error = MutableSharedFlow<Exception>()
    val error = _error.asSharedFlow()

    fun fetchMyFriends() {
        launchStoreCall(
            onLoading = { _uiState.update { Loading } },
            load = { updateMyFriends() },
            onError = { _error.emit(it) }
        )
    }

    fun unsubscribeFriend(friendId: String) {
        launchStoreCall(
            onLoading = { _uiState.update { Loading } },
            load = {
                unsubscribeFriend.defer(friendId)
                updateMyFriends()
            },
            onError = { _error.emit(it) }
        )
    }

    private suspend fun updateMyFriends() {
        val result = getMyFriends.await()
        if (result.isNotEmpty()) {
            _uiState.update { Data(friends = result.map { it.mapToUI() }) }
        } else {
            _uiState.update { Empty }
        }
    }
}
