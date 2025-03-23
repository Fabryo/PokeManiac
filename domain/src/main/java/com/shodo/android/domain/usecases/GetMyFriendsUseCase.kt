package com.shodo.android.domain.usecases

import com.shodo.android.domain.repositories.friends.UserRepository
import com.shodo.android.domain.repositories.entities.User

class GetMyFriendsUseCase(private val userRepository: UserRepository): UseCase<Nothing?, List<User>> {

    override suspend fun defer(arg: Nothing?): List<User> {
        return userRepository.getSubscribedUsers()
    }
}