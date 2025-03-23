package com.shodo.android.domain.usecases

import com.shodo.android.domain.repositories.friends.UserRepository
import com.shodo.android.domain.repositories.entities.User

class SearchFriendUseCase(private val userRepository: UserRepository): UseCase<String, List<User>> {

    override suspend fun defer(arg: String): List<User> {
        return userRepository.searchUser(arg)
    }
}