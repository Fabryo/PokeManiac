package com.shodo.android.domain

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.shodo.android.domain.repositories.entities.User
import com.shodo.android.domain.repositories.friends.UserRepository
import com.shodo.android.domain.usecases.SearchFriendUseCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SearchFriendUseCaseTest {

    private lateinit var dispatcher: TestDispatcher

    private val userRepository = mock<UserRepository> {
        onBlocking { searchUser("friendName") }.doReturn(listOf(defaultUserSubscribed, defaultUserNotSubscribed2))
    }

    private lateinit var searchFriendUseCase: SearchFriendUseCase

    @Before
    fun setUp() {
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)

        searchFriendUseCase = SearchFriendUseCase(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetSubscribedUsers() = runTest {
        // GIVEN

        // WHEN
        val result = searchFriendUseCase.defer("friendName")

        // THEN
        assertEquals(expected = listOf(defaultUserSubscribed, defaultUserNotSubscribed2), actual = result)
    }

    companion object {
        private val defaultUserNotSubscribed2 = User(
            id = "friendId2",
            name = "friendName",
            imageUrl = "friendImageUrl2",
            description = "friendDescription2",
            isSubscribed = false,
            pokemonCards = listOf()
        )
        private val defaultUserSubscribed = User(
            id = "friendId",
            name = "friendName",
            imageUrl = "friendImageUrl",
            description = "friendDescription",
            isSubscribed = true,
            pokemonCards = listOf()
        )
    }
}