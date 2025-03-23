package com.shodo.android.searchfriend.uimodel


enum class SubscriptionState {
    Subscribed, NotSubscribed, UpdatingSubscribe
}

data class SearchFriendUI(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val subscriptionState: SubscriptionState,
    val pokemonCards: List<SearchFriendPokemonCardUI>
)

data class SearchFriendPokemonCardUI(
    val pokemonId: Int,
    val totalVotes: Int,
    val hasMyVote: Boolean,
    val name: String,
    val imageUrl: String
)