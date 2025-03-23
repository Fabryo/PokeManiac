package com.shodo.android.data.myfriends

import com.shodo.android.domain.repositories.entities.User

interface FriendsDataStore {
    suspend fun getFriendByName(friendName: String): User?
    suspend fun getFriendById(friendId: String): User?
    suspend fun subscribeFriend(user: User)
    suspend fun unsubscribeFriend(user: User)

    suspend fun updateFriend(user: User)

    suspend fun getSubscribedFriends(): List<User>
}