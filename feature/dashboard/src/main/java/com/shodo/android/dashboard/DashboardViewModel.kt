package com.shodo.android.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shodo.android.dashboard.DashboardUiState.EmptyResult
import com.shodo.android.dashboard.DashboardUiState.Loading
import com.shodo.android.dashboard.uimodel.ImageSourceUI
import com.shodo.android.dashboard.uimodel.NewActivityTypeUI
import com.shodo.android.dashboard.uimodel.NewActivityUI
import com.shodo.android.dashboard.uimodel.PokemonCardUI
import com.shodo.android.domain.repositories.entities.ImageSource
import com.shodo.android.domain.repositories.entities.NewActivity
import com.shodo.android.domain.repositories.entities.NewActivityType
import com.shodo.android.domain.repositories.news.NewsFeedRepository
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class DashboardUiState {
    data class Data(val news: List<NewActivityUI>) : DashboardUiState()
    data object EmptyResult : DashboardUiState()
    data object Loading : DashboardUiState()
}

class DashboardViewModel(
    private val newsFeedRepository: NewsFeedRepository
) : ViewModel() {

    private val _error = MutableSharedFlow<Exception>()
    val error = _error.asSharedFlow()

    private val _uiState: MutableStateFlow<DashboardUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun start() {
        viewModelScope.launch {
            _uiState.update { Loading }
            fetchNewActivities()
        }
    }

    fun refreshNewsFeed() {
        viewModelScope.launch {
            _uiState.update { Loading }
            try {
                delay(2000) // Mock Delay to show the loading state
                fetchNewActivities()
            } catch (e: Exception) {
                _error.emit(e)
            }
        }
    }

    private suspend fun fetchNewActivities() {
        newsFeedRepository.getNewActivities().collect { result ->
            if (result.isNotEmpty()) {
                _uiState.update { DashboardUiState.Data(result.map { it.mapToUI() }) }
            } else {
                _uiState.update { EmptyResult }
            }
        }
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