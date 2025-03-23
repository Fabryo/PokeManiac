package com.shodo.android.myfriends.myfrienddetail

import androidx.lifecycle.ViewModel
import com.shodo.android.coreui.viewmodel.launchStoreCall
import com.shodo.android.domain.repositories.entities.User
import com.shodo.android.domain.usecases.UseCase
import com.shodo.android.myfriends.myfrienddetail.MyFriendDetailUiState.Data
import com.shodo.android.myfriends.myfrienddetail.MyFriendDetailUiState.Loading
import com.shodo.android.myfriends.myfriendlist.MyFriendListUiState
import com.shodo.android.myfriends.uimodel.MyFriendUI
import com.shodo.android.myfriends.uimodel.mapToUI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class MyFriendDetailUiState {
    data class Data(val friend: MyFriendUI) : MyFriendDetailUiState()
    data object Loading : MyFriendDetailUiState()
}

class MyFriendDetailViewModel(
    private val getUserUseCase: UseCase<String, User>,
    private val unsubscribeFriend: UseCase<String, User>
) : ViewModel() {

    private val _error = MutableSharedFlow<Exception>()
    val error = _error.asSharedFlow()

    private val _unsubscribed = MutableSharedFlow<Boolean>()
    val unsubscribed = _unsubscribed.asSharedFlow()

    private val _uiState: MutableStateFlow<MyFriendDetailUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<MyFriendDetailUiState> = _uiState.asStateFlow()

    fun start(id: String) {
        launchStoreCall(
            onLoading = { _uiState.update { Loading } },
            load = { _uiState.update { Data(getUserUseCase.defer(id).mapToUI()) } },
            onError = { _error.emit(it) }
        )
    }

    fun unsubscribeFriend(friendId: String) {
        launchStoreCall(
            onLoading = { _uiState.update { Loading } },
            load = {
                unsubscribeFriend.defer(friendId)
                _unsubscribed.emit(true)
            },
            onError = { _error.emit(it) }
        )
    }
}