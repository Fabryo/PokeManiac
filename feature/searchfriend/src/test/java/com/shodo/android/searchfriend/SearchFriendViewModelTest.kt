package com.shodo.android.searchfriend

import app.cash.turbine.test
import com.shodo.android.domain.repositories.entities.User
import com.shodo.android.domain.repositories.friends.UserRepository
import com.shodo.android.domain.repositories.tracking.TrackingRepository
import com.shodo.android.searchfriend.uimodel.SearchFriendUI
import com.shodo.android.searchfriend.uimodel.SubscriptionState.NotSubscribed
import com.shodo.android.searchfriend.uimodel.SubscriptionState.Subscribed
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class SearchFriendViewModelTest {

    private lateinit var dispatcher: TestDispatcher

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var trackingRepository: TrackingRepository

    private lateinit var searchFriendViewModel: SearchFriendViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
        searchFriendViewModel = SearchFriendViewModel(
            userRepository,
            trackingRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSearchFriendsWithResults() = runTest {
        // GIVEN
        `when`(userRepository.searchUsers("friendName")).thenReturn(
            flow { emit(listOf(defaultUserNotSubscribed, defaultUserNotSubscribed2)) }
        )

        searchFriendViewModel.uiState.test {
            // WHEN
            searchFriendViewModel.searchFriend("friendName")
            skipItems(2) // skip initial Empty Search & Loading states

            // THEN
            val uiStateResult = awaitItem()
            assertTrue(uiStateResult is SearchFriendUiState.Data)
            assertEquals(
                expected = listOf(defaultFriendUINotSubscribed, defaultFriendUINotSubscribed2),
                actual = uiStateResult.people
            )
        }
    }

    @Test
    fun testSearchFriendsWithEmptySearch() = runTest {
        // GIVEN
        searchFriendViewModel.uiState.test {
            // WHEN
            searchFriendViewModel.searchFriend("")

            // THEN
            val uiStateResult = awaitItem()
            assertTrue(uiStateResult is SearchFriendUiState.EmptySearch)
        }
    }

    @Test
    fun testSearchFriendsWithEmptyResult() = runTest {
        // GIVEN
        `when`(userRepository.searchUsers("friendName")).thenReturn(
            flow { emit(emptyList()) }
        )

        searchFriendViewModel = SearchFriendViewModel(
            userRepository,
            trackingRepository
        )
        searchFriendViewModel.uiState.test {
            // WHEN
            searchFriendViewModel.searchFriend("friendName")
            skipItems(2) // skip initial Empty Search & Loading states

            // THEN
            val uiStateResult = awaitItem()
            assertTrue(uiStateResult is SearchFriendUiState.EmptyResult)
        }
    }

    @Test
    fun testSubcribeFriend() = runTest {
        // GIVEN
        `when`(userRepository.searchUsers("friendName")).thenReturn(
            flow { emit(listOf(defaultUserNotSubscribed, defaultUserNotSubscribed2)) }
        )
        searchFriendViewModel.uiState.test {
            // WHEN
            searchFriendViewModel.searchFriend("friendName")
            skipItems(3) // skip initial Empty Search & Loading & Data states

            searchFriendViewModel.subscribeFriend("friendId")

            // THEN
            val resultState = awaitItem()
            assertTrue(resultState is SearchFriendUiState.Data)
            assertEquals(expected = listOf(defaultFriendUISubscribed, defaultFriendUINotSubscribed2), actual = resultState.people)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun testUnsubcribeFriend() = runTest {
        // GIVEN
        `when`(userRepository.searchUsers("friendName")).thenReturn(
            flow { emit(listOf(defaultUserSubscribed, defaultUserNotSubscribed2)) }
        )

        searchFriendViewModel = SearchFriendViewModel(
            userRepository,
            trackingRepository
        )

        searchFriendViewModel = SearchFriendViewModel(
            userRepository,
            trackingRepository
        )
        searchFriendViewModel.uiState.test {
            // WHEN
            searchFriendViewModel.searchFriend("friendName")
            skipItems(3) // skip initial Empty Search & Loading & Data states

            searchFriendViewModel.unsubscribeFriend("friendId")

            // THEN
            val resultState = awaitItem()
            assertTrue(resultState is SearchFriendUiState.Data)
            assertEquals(
                expected = listOf(defaultFriendUINotSubscribed, defaultFriendUINotSubscribed2),
                actual = resultState.people
            )
        }
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

        private val defaultFriendUINotSubscribed = SearchFriendUI(
            id = "friendId",
            name = "friendName",
            imageUrl = "friendImageUrl",
            description = "friendDescription",
            subscriptionState = NotSubscribed,
            pokemonCards = listOf(),
        )
        private val defaultFriendUINotSubscribed2 = SearchFriendUI(
            id = "friendId2",
            name = "friendName",
            imageUrl = "friendImageUrl2",
            description = "friendDescription2",
            subscriptionState = NotSubscribed,
            pokemonCards = listOf()
        )
        private val defaultFriendUISubscribed = SearchFriendUI(
            id = "friendId",
            name = "friendName",
            imageUrl = "friendImageUrl",
            description = "friendDescription",
            subscriptionState = Subscribed,
            pokemonCards = listOf()
        )
    }
}