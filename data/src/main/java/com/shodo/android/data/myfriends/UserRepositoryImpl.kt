package com.shodo.android.data.myfriends

import com.shodo.android.domain.repositories.entities.User
import com.shodo.android.domain.repositories.friends.UserRepository
import com.shodo.android.domain.repositories.entities.NoUserFoundException
import com.shodo.android.domain.repositories.entities.UserPokemonCard

class UserRepositoryImpl(
    private val friendsRequest: FriendsRequest,
    private val friendsDataStore: FriendsDataStore
) : UserRepository {

    override suspend fun getSubscribedUsers(): List<User> {
        return friendsDataStore.getSubscribedFriends()
    }

    override suspend fun getSubscribedUser(userId: String): User? {
        return friendsDataStore.getFriendById(userId)
    }

    override suspend fun searchUser(userName: String): List<User> {
        val subscribedCharacters = getSubscribedUsers()
        return friendsRequest.searchFriend(userName)?.map { searchCharacter ->
            subscribedCharacters.firstOrNull { it.id == searchCharacter.id} ?: searchCharacter
        } ?: emptyList()
    }

    override suspend fun subscribeUser(user: User): User {
        return friendsDataStore.getFriendById(user.id)?.let { subscribedFriend ->
            val updatedFriend = subscribedFriend.copy(isSubscribed = true)
            friendsDataStore.subscribeFriend(updatedFriend)
            updatedFriend
        } ?: run {
            val updatedFriend = user.copy(isSubscribed = true)
            friendsDataStore.subscribeFriend(user.copy(isSubscribed = true))
            updatedFriend
        }
    }

    override suspend fun unsubscribeUser(userId: String): User {
        friendsDataStore.getFriendById(userId)?.let { friend ->
            val updatedFriend = friend.copy(isSubscribed = false)
            friendsDataStore.unsubscribeFriend(updatedFriend)
            return updatedFriend
        } ?: throw NoUserFoundException(userId)
    }

    override suspend fun upvotePokemonCard(userId: String, cardId: Int): User {
        return friendsDataStore.getFriendById(userId)?.apply {
            val newPokemonCards: List<UserPokemonCard> = pokemonCards.toMutableList().voteForCard(cardId, true).toList()
            friendsDataStore.updateFriend(copy(pokemonCards = newPokemonCards))
        } ?: throw NoUserFoundException(userId)
    }

    override suspend fun downvotePokemonCard(userId: String, cardId: Int): User {
        return friendsDataStore.getFriendById(userId)?.apply {
            val newPokemonCards: List<UserPokemonCard> = pokemonCards.toMutableList().voteForCard(cardId, false).toList()
            friendsDataStore.updateFriend(copy(pokemonCards = newPokemonCards))
        } ?: throw NoUserFoundException(userId)
    }

    private fun MutableList<UserPokemonCard>.voteForCard(cardId: Int, upvote: Boolean): MutableList<UserPokemonCard> {
        val index = indexOfFirst { it.pokemonId == cardId }
        if (index != -1) {
            val updatedCard = this[index].copy(hasMyVote = upvote)
            this[index] = updatedCard
        }
        return this
    }

//    // Probably better way to do this, to be improved later
//    private fun SuperStats.upvoteStat(stat: SuperStat): SuperStats = when (stat) {
//        is Combat -> copy(combat = combat.copy(level = combat.level + 1))
//        is Durability -> copy(durability = durability.copy(level = durability.level + 1))
//        is Intelligence -> copy(intelligence = intelligence.copy(level = intelligence.level + 1))
//        is Power -> copy(power = power.copy(level = power.level + 1))
//        is Speed -> copy(speed = speed.copy(level = speed.level + 1))
//        is Strength -> copy(strength = strength.copy(level = strength.level + 1))
//    }
//
//    // Probably better way to do this, to be improved later
//    private fun SuperStats.downvoteStat(stat: SuperStat): SuperStats = when (stat) {
//        is Combat -> copy(combat = combat.copy(level = combat.level - 1))
//        is Durability -> copy(durability = durability.copy(level = durability.level - 1))
//        is Intelligence -> copy(intelligence = intelligence.copy(level = intelligence.level - 1))
//        is Power -> copy(power = power.copy(level = power.level - 1))
//        is Speed -> copy(speed = speed.copy(level = speed.level - 1))
//        is Strength -> copy(strength = strength.copy(level = strength.level - 1))
//    }
}