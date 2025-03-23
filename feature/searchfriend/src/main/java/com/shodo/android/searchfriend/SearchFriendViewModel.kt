package com.shodo.android.searchfriend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shodo.android.coreui.viewmodel.launchStoreCall
import com.shodo.android.domain.repositories.entities.ImageSource
import com.shodo.android.domain.repositories.entities.User
import com.shodo.android.domain.repositories.entities.UserPokemonCard
import com.shodo.android.domain.repositories.tracking.TrackingEventClick
import com.shodo.android.domain.repositories.tracking.TrackingEventScreen
import com.shodo.android.domain.usecases.UseCase
import com.shodo.android.searchfriend.SearchFriendUiState.Data
import com.shodo.android.searchfriend.SearchFriendUiState.EmptyResult
import com.shodo.android.searchfriend.SearchFriendUiState.EmptySearch
import com.shodo.android.searchfriend.SearchFriendUiState.Loading
import com.shodo.android.searchfriend.uimodel.SearchFriendPokemonCardUI
import com.shodo.android.searchfriend.uimodel.SearchFriendUI
import com.shodo.android.searchfriend.uimodel.SubscriptionState.NotSubscribed
import com.shodo.android.searchfriend.uimodel.SubscriptionState.Subscribed
import com.shodo.android.searchfriend.uimodel.SubscriptionState.UpdatingSubscribe
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class SearchFriendUiState {
    data object EmptySearch: SearchFriendUiState()
    data object Loading : SearchFriendUiState()
    data class Data(val people: List<SearchFriendUI>) : SearchFriendUiState()
    data class EmptyResult(val query: String): SearchFriendUiState()
}

class SearchFriendViewModel(
    private val searchFriend: UseCase<String, List<User>>,
    private val subscribeFriend: UseCase<User, User>,
    private val unsubscribeFriend: UseCase<String, User>,
    private val trackEventScreen: UseCase<TrackingEventScreen, Unit>,
    private val trackEventClick: UseCase<TrackingEventClick, Unit>
) : ViewModel() {

    private val _uiState: MutableStateFlow<SearchFriendUiState> = MutableStateFlow(EmptySearch)
    val uiState = _uiState.asStateFlow()

    private val _error = MutableSharedFlow<Exception>()
    val error = _error.asSharedFlow()

    init {
        viewModelScope.launch {
            trackEventScreen.defer(TrackingEventScreen(TRACKING_SEARCH_SCREEN))
        }
    }

    fun searchFriend(friendName: String) {
        if (friendName.isEmpty() || friendName.isBlank()) {
            viewModelScope.launch {
                _uiState.update { EmptySearch }
            }
        } else {
            launchStoreCall(
                onLoading = { _uiState.update { Loading } },
                load = {
                    val result = searchFriend.defer(friendName)
                    if (result.isNotEmpty()) {
                        _uiState.update { Data(people = searchFriend.defer(friendName).map { it.mapToUI() }) }
                    } else {
                        _uiState.update { EmptyResult(friendName) }
                    }},
                onError = { _error.emit(it) }
            )
        }
    }

    fun subscribeCharacter(friendId: String) = updateCharacterSubription(friendId = friendId, subscribe = true)
    fun unsubscribeCharacter(friendId: String) = updateCharacterSubription(friendId = friendId, subscribe = false)

    private fun updateCharacterSubription(friendId: String, subscribe: Boolean) {
        (_uiState.value as? Data)?.let { data ->
            launchStoreCall(
                onLoading = {
                    _uiState.update {
                        Data(
                            people = data.people.map { friend ->
                                if (friend.id == friendId) {
                                    friend.copy(subscriptionState = UpdatingSubscribe)
                                } else friend
                            }
                        )
                    }

                },
                load = {
                    _uiState.update {
                        Data(
                            people = data.people.map { friend ->
                                if (friend.id == friendId) {
                                    if (subscribe) {
                                        trackEventClick.defer(TrackingEventClick(TRACKING_SUBSCRIBE_CLICK))
                                        subscribeFriend.defer(friend.mapToModel()).mapToUI()
                                    } else {
                                        trackEventClick.defer(TrackingEventClick(TRACKING_UNSUBSCRIBE_CLICK))
                                        unsubscribeFriend.defer(friend.id).mapToUI()
                                    }
                                } else friend
                            }
                        )
                    }
                },
                onError = { _error.emit(it) }
            )
        }
    }

    companion object {
        private const val TRACKING_SEARCH_SCREEN = "TRACKING_SEARCH_SCREEN"
        private const val TRACKING_SUBSCRIBE_CLICK = "TRACKING_SUBSCRIBE_CLICK"
        private const val TRACKING_UNSUBSCRIBE_CLICK = "TRACKING_UNSUBSCRIBE_CLICK"
    }
}

private fun User.mapToUI() = SearchFriendUI(
    id = id,
    name = name,
    imageUrl = imageUrl,
    description = description,
    subscriptionState = if (isSubscribed) Subscribed else NotSubscribed,
    pokemonCards = pokemonCards.map { it.maptoUI() }
)

private fun UserPokemonCard.maptoUI() = SearchFriendPokemonCardUI(
    pokemonId = pokemonId,
    totalVotes = totalVotes,
    hasMyVote = hasMyVote,
    name = name,
    imageUrl = (imageSource as ImageSource.UrlSource).imageUrl
)

private fun SearchFriendUI.mapToModel() = User(
    id = id,
    name = name,
    imageUrl = imageUrl,
    description = description,
    isSubscribed = when (subscriptionState) {
        Subscribed -> true
        else -> false
    },
    pokemonCards = pokemonCards.map { it.mapToModel() }
)

private fun SearchFriendPokemonCardUI.mapToModel() = UserPokemonCard(
    pokemonId = pokemonId,
    totalVotes = totalVotes,
    hasMyVote = hasMyVote,
    name = name,
    imageSource = ImageSource.UrlSource(imageUrl)
)