package com.shodo.android.data

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.shodo.android.data.myfriends.FriendsDataStore
import com.shodo.android.data.myfriends.FriendsRequest
import com.shodo.android.data.myfriends.UserRepositoryImpl
import com.shodo.android.domain.repositories.entities.User
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

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    private lateinit var dispatcher: TestDispatcher

    private val friendsRequest = mock<FriendsRequest> {
        onBlocking { searchFriend("friendName") }.doReturn(listOf(defaultUserSubscribed, defaultUserNotSubscribed2))
    }
    private val friendsDataStore = mock<FriendsDataStore> {
        onBlocking { getFriendById("friendId") }.doReturn(defaultUserSubscribed)
        onBlocking { getFriendByName("friendName") }.doReturn(defaultUserSubscribed)
        onBlocking { subscribeFriend(defaultUserNotSubscribed) }.doReturn(Unit)
        onBlocking { unsubscribeFriend(defaultUserSubscribed) }.doReturn(Unit)
        onBlocking { updateFriend(defaultUserSubscribed) }.doReturn(Unit)
        onBlocking { getSubscribedFriends() }.doReturn(listOf(defaultUserSubscribed))
    }
    private lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setUp() {
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
        userRepository = UserRepositoryImpl(friendsRequest, friendsDataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetSubscribedUsers() = runTest {
        // GIVEN

        // WHEN
        val result = userRepository.getSubscribedUsers()

        // THEN
        kotlin.test.assertEquals(expected = listOf(defaultUserSubscribed), actual = result)
    }

    @Test
    fun testGetSubscribedUser() = runTest {
        // GIVEN

        // WHEN
        val result = userRepository.getSubscribedUser("friendId")

        // THEN
        kotlin.test.assertEquals(expected = defaultUserSubscribed, actual = result)
    }

    @Test
    fun testSearchUser() = runTest {
        // GIVEN

        // WHEN
        val result = userRepository.searchUser("friendName")

        // THEN
        kotlin.test.assertEquals(expected = listOf(defaultUserSubscribed, defaultUserNotSubscribed2), actual = result)
    }

    @Test
    fun testSubscribeUser() = runTest {
        // GIVEN

        // WHEN
        val result = userRepository.subscribeUser(defaultUserNotSubscribed)

        // THEN
        kotlin.test.assertEquals(expected = defaultUserSubscribed, actual = result)
    }

    @Test
    fun testUnsubscribeUser() = runTest {
        // GIVEN

        // WHEN
        val result = userRepository.unsubscribeUser("friendId")

        // THEN
        kotlin.test.assertEquals(expected = defaultUserNotSubscribed, actual = result)
    }

    companion object {
        private val defaultUserNotSubscribed = User(
            id = "friendId",
            name = "friendName",
            imageUrl = "friendImageUrl",
            description = "friendDescription",
            isSubscribed = false,
            pokemonCards = listOf()
        )
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