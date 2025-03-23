package com.shodo.android.domain.usecases

import com.shodo.android.domain.repositories.friends.UserRepository
import com.shodo.android.domain.repositories.entities.User
import com.shodo.android.domain.repositories.entities.NoUserFoundException

class GetUserUseCase(private val userRepository: UserRepository): UseCase<String, User> {

    override suspend fun defer(arg: String): User {
        return userRepository.getSubscribedUser(arg) ?: throw NoUserFoundException(arg)
    }
}