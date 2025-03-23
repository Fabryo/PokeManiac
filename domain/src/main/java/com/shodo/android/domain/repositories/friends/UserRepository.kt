package com.shodo.android.domain.repositories.friends

import com.shodo.android.domain.repositories.entities.User

interface UserRepository {
    suspend fun getSubscribedUsers(): List<User>
    suspend fun getSubscribedUser(userId: String): User?

    suspend fun searchUser(userName: String): List<User>

    suspend fun subscribeUser(user: User): User
    suspend fun unsubscribeUser(userId: String): User

    suspend fun upvotePokemonCard(userId: String, cardId: Int): User
    suspend fun downvotePokemonCard(userId: String, cardId: Int): User
}