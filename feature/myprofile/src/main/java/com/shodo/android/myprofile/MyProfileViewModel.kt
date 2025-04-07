package com.shodo.android.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shodo.android.domain.repositories.entities.ImageSource
import com.shodo.android.domain.repositories.entities.UserPokemonCard
import com.shodo.android.domain.repositories.myprofile.MyProfileRepository
import com.shodo.android.myprofile.MyProfileUiState.Loading
import com.shodo.android.myprofile.uimodel.MyProfilePokemonCardUI
import com.shodo.android.myprofile.uimodel.MyProfileUI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class MyProfileUiState {
    data object Loading: MyProfileUiState()
    data class Data(val profile: MyProfileUI): MyProfileUiState()
}

class MyProfileViewModel(
    private val myProfileRepository: MyProfileRepository
): ViewModel() {

    private val _uiState: MutableStateFlow<MyProfileUiState> = MutableStateFlow(Loading)
    val uiState = _uiState.asStateFlow()

    private val _error = MutableSharedFlow<Exception>()
    val error = _error.asSharedFlow()

    fun start() {
        viewModelScope.launch {
            _uiState.update { Loading }
            try {
                myProfileRepository.getMyActivities().collect { myActivies ->
                    _uiState.update {
                        MyProfileUiState.Data(
                            profile = MyProfileUI(
                                name = null,
                                imageUrl = null,
                                pokemonCards = myActivies.sortedByDescending { it.date }.map { it.pokemonCard.mapToUI() }
                            ))
                    }
                }
            } catch (e: Exception) {
                _error.emit(e)
            }
        }
    }
}

private fun UserPokemonCard.mapToUI() = MyProfilePokemonCardUI(
    totalVotes = totalVotes,
    name = name,
    imageUri = (imageSource as ImageSource.FileSource).fileUri
)
