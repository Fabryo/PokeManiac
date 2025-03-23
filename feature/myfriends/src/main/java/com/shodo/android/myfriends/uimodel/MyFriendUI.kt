package com.shodo.android.myfriends.uimodel

import com.shodo.android.domain.repositories.entities.ImageSource
import com.shodo.android.domain.repositories.entities.User
import com.shodo.android.domain.repositories.entities.UserPokemonCard

data class MyFriendUI(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val pokemonCards: List<MyFriendPokemonCardUI>
)

data class MyFriendPokemonCardUI(
    val pokemonId: Int,
    val totalVotes: Int,
    val hasMyVote: Boolean,
    val name: String,
    val imageUrl: String
)

fun User.mapToUI() = MyFriendUI(
    id = id,
    name = name,
    imageUrl = imageUrl,
    description = description,
    pokemonCards = pokemonCards.map { it.mapToUI() }
)

private fun UserPokemonCard.mapToUI() = MyFriendPokemonCardUI(
    pokemonId = pokemonId,
    totalVotes = totalVotes,
    hasMyVote = hasMyVote,
    name = name,
    imageUrl = (imageSource as ImageSource.UrlSource).imageUrl
)
