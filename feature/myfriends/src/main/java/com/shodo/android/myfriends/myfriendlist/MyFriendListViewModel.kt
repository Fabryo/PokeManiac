package com.shodo.android.myfriends.myfriendlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shodo.android.domain.repositories.friends.UserRepository
import com.shodo.android.myfriends.myfriendlist.MyFriendListUiState.Data
import com.shodo.android.myfriends.myfriendlist.MyFriendListUiState.Empty
import com.shodo.android.myfriends.myfriendlist.MyFriendListUiState.Loading
import com.shodo.android.myfriends.uimodel.MyFriendUI
import com.shodo.android.myfriends.uimodel.mapToUI
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class MyFriendListUiState {
    data object Loading: MyFriendListUiState()
    data class Data(val friends: PersistentList<MyFriendUI>): MyFriendListUiState()
    data object Empty: MyFriendListUiState()
}

class MyFriendListViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    private val _uiState: MutableStateFlow<MyFriendListUiState> = MutableStateFlow(Loading)
    val uiState = _uiState.asStateFlow()

    private val _error = MutableSharedFlow<Exception>()
    val error = _error.asSharedFlow()

    fun fetchMyFriends() {
        viewModelScope.launch {
            _uiState.update { Loading }
            try {
                userRepository.getSubscribedUsers().collect { friends ->
                    if (friends.isNotEmpty()) {
                        _uiState.update { Data(friends = friends.map { it.mapToUI() }.toPersistentList()) }
                    } else {
                        _uiState.update { Empty }
                    }
                }
            } catch (e: Exception) {
                _error.emit(e)
            }
        }
    }

    fun unsubscribeFriend(friendId: String) {
        viewModelScope.launch {
            try {
                userRepository.unsubscribeUser(friendId)
            } catch (e: Exception) {
                _error.emit(e)
            }
        }
    }
}
