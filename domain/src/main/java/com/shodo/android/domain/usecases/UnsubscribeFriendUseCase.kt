package com.shodo.android.domain.usecases

import com.shodo.android.domain.repositories.friends.UserRepository
import com.shodo.android.domain.repositories.entities.User

class UnsubscribeFriendUseCase(private val userRepository: UserRepository): UseCase<String, User> {

    override suspend fun defer(arg: String): User {
        return userRepository.unsubscribeUser(arg)
    }
}