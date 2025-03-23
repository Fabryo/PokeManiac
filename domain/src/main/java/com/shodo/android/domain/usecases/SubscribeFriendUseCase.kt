package com.shodo.android.domain.usecases

import com.shodo.android.domain.repositories.friends.UserRepository
import com.shodo.android.domain.repositories.entities.User

class SubscribeFriendUseCase(private val userRepository: UserRepository): UseCase<User, User> {

    override suspend fun defer(arg: User): User {
        return userRepository.subscribeUser(arg)
    }
}