package com.shodo.android.data.myfriends

import com.shodo.android.domain.repositories.entities.User
import com.shodo.android.domain.repositories.friends.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip

class UserRepositoryImpl(
    private val friendsRequest: FriendsRequest,
    private val friendsDataStore: FriendsDataStore
) : UserRepository {

    override fun getSubscribedUsers(): Flow<List<User>> {
        return friendsDataStore.getSubscribedFriends()
    }

    override fun getSubscribedUser(userId: String): Flow<User?> {
        return friendsDataStore.getFriendById(userId)
    }

    override suspend fun searchUsers(userName: String): Flow<List<User>> {
        val subscribedFriends = friendsDataStore.getSubscribedFriends()
        val searchFriends = flow { emit(friendsRequest.searchUsers(userName)) }
        return searchFriends.zip(subscribedFriends) { searchResults, subscribedUsers ->
            searchResults.map { searchResult ->
                subscribedUsers.firstOrNull { it.id == searchResult.id } ?: searchResult
            }
        }
    }

    override suspend fun subscribeUser(user: User) {
        friendsDataStore.subscribeFriend(user.copy(isSubscribed = true))
    }

    override suspend fun unsubscribeUser(userId: String) {
        return friendsDataStore.unsubscribeFriend(userId)
    }
}