package com.shodo.android.database.friends

import com.shodo.android.data.myfriends.FriendsDataStore
import com.shodo.android.database.PokeManiacDatabase
import com.shodo.android.database.friends.FriendBase.PokemonCardBase
import com.shodo.android.domain.repositories.entities.ImageSource
import com.shodo.android.domain.repositories.entities.User
import com.shodo.android.domain.repositories.entities.UserPokemonCard

class FriendsDataStoreImpl(private val database: PokeManiacDatabase) : FriendsDataStore {
    override suspend fun getFriendByName(friendName: String): User? {
        return database.localFriendsDao().getFriendByName(friendName)?.mapToModel()
    }

    override suspend fun getFriendById(friendId: String): User? {
        return database.localFriendsDao().getFriendById(friendId)?.mapToModel()
    }

    override suspend fun subscribeFriend(user: User) {
        database.localFriendsDao().insert(user.mapToBase())
    }

    override suspend fun unsubscribeFriend(user: User) {
        database.localFriendsDao().deleteFriend(user.id)
    }

    override suspend fun updateFriend(user: User) {
        database.localFriendsDao().insert(user.mapToBase())
    }

    override suspend fun getSubscribedFriends(): List<User> {
        return database.localFriendsDao().getAllFriends()?.map { it.mapToModel() } ?: emptyList()
    }
}

private fun FriendBase.mapToModel() = User(
    id = id,
    name = name,
    imageUrl = imageUrl,
    description = description,
    isSubscribed = isSubscribed,
    pokemonCards = pokemonCards.map { it.mapToModel() }
)

private fun PokemonCardBase.mapToModel() = UserPokemonCard(
    pokemonId = pokemonId,
    totalVotes = totalVotes,
    hasMyVote = hasMyVote,
    name = name,
    imageSource = ImageSource.UrlSource(imageUrl)
)

private fun User.mapToBase() = FriendBase(
    id = id,
    name = name,
    imageUrl = imageUrl,
    description = description,
    isSubscribed = isSubscribed,
    pokemonCards = pokemonCards.map { it.mapToBase() }
)

private fun UserPokemonCard.mapToBase() = PokemonCardBase(
    pokemonId = pokemonId,
    totalVotes = totalVotes,
    hasMyVote = hasMyVote,
    name = name,
    imageUrl = (imageSource as ImageSource.UrlSource).imageUrl
)