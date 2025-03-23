package com.shodo.android.myprofile

import androidx.lifecycle.ViewModel
import com.shodo.android.coreui.viewmodel.launchStoreCall
import com.shodo.android.domain.repositories.entities.ImageSource
import com.shodo.android.domain.repositories.entities.NewActivity
import com.shodo.android.domain.repositories.entities.UserPokemonCard
import com.shodo.android.domain.usecases.UseCase
import com.shodo.android.domain.usecases.UseCase.Companion.await
import com.shodo.android.myprofile.MyProfileUiState.Loading
import com.shodo.android.myprofile.uimodel.MyProfilePokemonCardUI
import com.shodo.android.myprofile.uimodel.MyProfileUI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class MyProfileUiState {
    data object Loading: MyProfileUiState()
    data class Data(val profile: MyProfileUI): MyProfileUiState()
}

class MyProfileViewModel(
    private val getMyActivities: UseCase<Nothing?, List<NewActivity>>
): ViewModel() {

    private val _uiState: MutableStateFlow<MyProfileUiState> = MutableStateFlow(Loading)
    val uiState = _uiState.asStateFlow()

    private val _error = MutableSharedFlow<Exception>()
    val error = _error.asSharedFlow()

    fun start() {
        launchStoreCall(
            onLoading = { _uiState.update { Loading } },
            load = {
                _uiState.update { MyProfileUiState.Data(profile = MyProfileUI(
                    name = null,
                    imageUrl = null,
                    pokemonCards = getMyActivities.await().map { it.pokemonCard.mapToUI() }
                )) }
            },
            onError = { _error.emit(it) }
        )
    }
}

private fun UserPokemonCard.mapToUI() = MyProfilePokemonCardUI(
    totalVotes = totalVotes,
    name = name,
    imageUri = (imageSource as ImageSource.FileSource).fileUri
)
