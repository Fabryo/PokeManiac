package com.shodo.android.data

import com.shodo.android.data.myfriends.FriendsDataStore
import com.shodo.android.data.myfriends.FriendsRequest
import com.shodo.android.data.myfriends.UserRepositoryImpl
import com.shodo.android.domain.repositories.entities.User
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    private lateinit var dispatcher: TestDispatcher

    @Mock
    private lateinit var friendsRequest: FriendsRequest
//    = FriendsRequest {
//        onBlocking { searchUsers("friendName") }.doReturn(flow { emit(listOf(defaultUserSubscribed, defaultUser2NotSubscribed)) } )
//    }
    @Mock
    private lateinit var friendsDataStore: FriendsDataStore
//    = mock<FriendsDataStore> {
//        onBlocking { getFriendById("friendId") }.doReturn(flow { emit(defaultUserSubscribed) })
//        onBlocking { updateFriend(defaultUserSubscribed) }.doReturn(Unit)
//        onBlocking { getSubscribedFriends() }.doReturn(flow { emit(listOf(defaultUserSubscribed)) })
//    }
    private lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
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
        `when`(friendsDataStore.getSubscribedFriends()).thenReturn(
            flow { emit(listOf(defaultUserSubscribed)) }
        )

        // WHEN
        val result = userRepository.getSubscribedUsers().first()

        // THEN
        assertEquals(expected = listOf(defaultUserSubscribed), actual = result)
    }

    @Test
    fun testGetSubscribedUser() = runTest {
        // GIVEN
        `when`(friendsDataStore.getFriendById("friendId")).thenReturn(
            flow { emit(defaultUserSubscribed) }
        )

        // WHEN
        val result = userRepository.getSubscribedUser("friendId").first()

        // THEN
        assertEquals(expected = defaultUserSubscribed, actual = result)
    }

    @Test
    fun testSearchUser() = runTest {
        // GIVEN
        `when`(friendsDataStore.getSubscribedFriends()).thenReturn(
            flow { emit(listOf(defaultUserSubscribed)) }
        )
        `when`(friendsRequest.searchUsers("friendName")).thenReturn(
            listOf(defaultUserSubscribed, defaultUser2NotSubscribed)
        )

        // WHEN
        val result = userRepository.searchUsers("friendName").first()

        // THEN
        assertEquals(expected = listOf(defaultUserSubscribed, defaultUser2NotSubscribed), actual = result)
    }

    companion object {
        private val defaultUser2NotSubscribed = User(
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