package com.shodo.android.database.friends

import com.shodo.android.data.myfriends.FriendsDataStore
import com.shodo.android.database.PokeManiacDatabase
import com.shodo.android.database.friends.FriendBase.PokemonCardBase
import com.shodo.android.domain.repositories.entities.ImageSource
import com.shodo.android.domain.repositories.entities.User
import com.shodo.android.domain.repositories.entities.UserPokemonCard
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FriendsDataStoreImpl(private val database: PokeManiacDatabase) : FriendsDataStore {
    override fun getFriendById(friendId: String): Flow<User?> {
        return database.localFriendsDao().getFriendById(friendId).map { it?.mapToModel() }
    }

    override suspend fun subscribeFriend(user: User) {
        database.localFriendsDao().insert(user.mapToBase())
    }

    override suspend fun unsubscribeFriend(userId: String) {
        database.localFriendsDao().deleteFriend(userId)
    }

    override suspend fun updateFriend(user: User) {
        database.localFriendsDao().insert(user.mapToBase())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getSubscribedFriends(): Flow<List<User>> {
        return database.localFriendsDao().getAllFriends().map { friends -> friends.map { it.mapToModel() } }
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