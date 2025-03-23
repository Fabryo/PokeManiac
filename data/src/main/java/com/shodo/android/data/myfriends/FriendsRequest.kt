package com.shodo.android.data.myfriends

import com.shodo.android.domain.repositories.entities.User

interface FriendsRequest {
    suspend fun searchFriend(friendName: String): List<User>?
}