package com.shodo.android.dashboard

import androidx.lifecycle.ViewModel
import com.shodo.android.coreui.viewmodel.launchStoreCall
import com.shodo.android.dashboard.DashboardUiState.EmptyResult
import com.shodo.android.dashboard.DashboardUiState.Loading
import com.shodo.android.dashboard.uimodel.ImageSourceUI
import com.shodo.android.dashboard.uimodel.NewActivityTypeUI
import com.shodo.android.dashboard.uimodel.NewActivityUI
import com.shodo.android.dashboard.uimodel.PokemonCardUI
import com.shodo.android.domain.repositories.entities.ImageSource
import com.shodo.android.domain.repositories.entities.NewActivity
import com.shodo.android.domain.repositories.entities.NewActivityType
import com.shodo.android.domain.usecases.UseCase
import com.shodo.android.domain.usecases.UseCase.Companion.await
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


sealed class DashboardUiState {
    data class Data(val news: List<NewActivityUI>) : DashboardUiState()
    data object EmptyResult : DashboardUiState()
    data object Loading : DashboardUiState()
}

class DashboardViewModel(
    private val getNews: UseCase<Nothing?, List<NewActivity>>
) : ViewModel() {

    private val _error = MutableSharedFlow<Exception>()
    val error = _error.asSharedFlow()

    private val _uiState: MutableStateFlow<DashboardUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun start() {
        launchStoreCall(
            onLoading = { _uiState.update { Loading } },
            load = {
                val result = getNews.await()
                if (result.isNotEmpty()) {
                    _uiState.update { DashboardUiState.Data(getNews.await().map { it.mapToUI() }) }
                } else {
                    _uiState.update { EmptyResult }
                }
            },
            onError = { _error.emit(it) }
        )
    }

    fun refreshNewsFeed() {
        launchStoreCall(
            onLoading = { _uiState.update { Loading } },
            load = {
                delay(2000) // Mock Delay to show the loading state
                val result = getNews.await()
                if (result.isNotEmpty()) {
                    _uiState.update { DashboardUiState.Data(getNews.await().map { it.mapToUI() }) }
                } else {
                    _uiState.update { EmptyResult }
                }
            },
            onError = { _error.emit(it) }
        )
    }
}

private fun NewActivity.mapToUI() = NewActivityUI(
    friendName = userName,
    friendImageUrl = userImageUrl,
    date = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
    activityType = activityType.mapToUI(),
    pokemonCard = PokemonCardUI(
        name = pokemonCard.name,
        imageSource = when (val source = pokemonCard.imageSource) {
            is ImageSource.UrlSource -> ImageSourceUI.UrlSource(source.imageUrl)
            is ImageSource.FileSource -> ImageSourceUI.FileSource(source.fileUri)
        }
    ),
    price = price
)

private fun NewActivityType.mapToUI() = when (this) {
    NewActivityType.Purchase -> NewActivityTypeUI.Purchase
    NewActivityType.Sale -> NewActivityTypeUI.Sale
}