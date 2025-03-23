package com.shodo.android.searchfriend

import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.shodo.android.domain.repositories.entities.User
import com.shodo.android.domain.repositories.tracking.TrackingEventClick
import com.shodo.android.domain.repositories.tracking.TrackingEventScreen
import com.shodo.android.domain.usecases.UseCase
import com.shodo.android.searchfriend.uimodel.SearchFriendUI
import com.shodo.android.searchfriend.uimodel.SubscriptionState.NotSubscribed
import com.shodo.android.searchfriend.uimodel.SubscriptionState.Subscribed
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
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class SearchFriendViewModelTest {

    private lateinit var dispatcher: TestDispatcher

    private val searchFriend = mock<UseCase<String, List<User>>> {
        onBlocking { defer("friendName") }.doReturn(
            listOf(defaultUserNotSubscribed, defaultUserNotSubscribed2)
        )
    }
    private val subscribeFriend = mock<UseCase<User, User>> {
        onBlocking { defer(defaultUserNotSubscribed) }.doReturn(defaultUserSubscribed)
    }
    private val unsubscribeFriend = mock<UseCase<String, User>> {
        onBlocking { defer("friendId") }.doReturn(defaultUserNotSubscribed)
    }
    private val trackEventScreen = mock<UseCase<TrackingEventScreen, Unit>> {
        onBlocking { defer(TrackingEventScreen("eventScreenName")) }.doReturn(Unit)
    }
    private val trackEventClick = mock<UseCase<TrackingEventClick, Unit>> {
        onBlocking { defer(TrackingEventClick("eventScreenClick")) }.doReturn(Unit)
    }

    private lateinit var searchFriendViewModel: SearchFriendViewModel

    @Before
    fun setUp() {
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
        searchFriendViewModel = SearchFriendViewModel(
            searchFriend,
            subscribeFriend,
            unsubscribeFriend,
            trackEventScreen,
            trackEventClick
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSearchFriendsWithResults() = runTest {
        // GIVEN
        searchFriendViewModel.uiState.test {
            // WHEN
            searchFriendViewModel.searchFriend("friendName")
            skipItems(1) // skip Loading state

            // THEN
            val uiStateResult = awaitItem()
            assertTrue(uiStateResult is SearchFriendUiState.Data)
            assertEquals(expected = listOf(defaultFriendUINotSubscribed, defaultFriendUINotSubscribed2), actual = uiStateResult.people)
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
        val searchFriendReturnsEmpty = mock<UseCase<String, List<User>>> {
            onBlocking { defer("friendName") }.doReturn(emptyList())
        }
        searchFriendViewModel = SearchFriendViewModel(
            searchFriendReturnsEmpty,
            subscribeFriend,
            unsubscribeFriend,
            trackEventScreen,
            trackEventClick
        )
        searchFriendViewModel.uiState.test {
            // WHEN
            searchFriendViewModel.searchFriend("friendName")
            skipItems(1) // skip Loading state

            // THEN
            val uiStateResult = awaitItem()
            assertTrue(uiStateResult is SearchFriendUiState.EmptyResult)
        }
    }

    @Test
    fun testSubcribeFriend() = runTest {
        // GIVEN
        searchFriendViewModel.uiState.test {
            // WHEN
            searchFriendViewModel.searchFriend("friendName")
            skipItems(2) // skip Loading + Data states

            searchFriendViewModel.subscribeFriend("friendId")

            // THEN
            val resultState = awaitItem()
            assertTrue(resultState is SearchFriendUiState.Data)
            assertEquals(expected = listOf(defaultFriendUISubscribed, defaultFriendUINotSubscribed2), actual = resultState.people)
        }
    }

    @Test
    fun testUnsubcribeFriend() = runTest {
        // GIVEN
        val searchFriendReturnsSubscribed = mock<UseCase<String, List<User>>> {
            onBlocking { defer("friendName") }.doReturn(
                listOf(defaultUserSubscribed, defaultUserNotSubscribed2)
            )
        }
        searchFriendViewModel = SearchFriendViewModel(
            searchFriendReturnsSubscribed,
            subscribeFriend,
            unsubscribeFriend,
            trackEventScreen,
            trackEventClick
        )
        searchFriendViewModel.uiState.test {
            // WHEN
            searchFriendViewModel.searchFriend("friendName")
            skipItems(2) // skip Loading + Data states

            searchFriendViewModel.unsubscribeFriend("friendId")

            // THEN
            val resultState = awaitItem()
            assertTrue(resultState is SearchFriendUiState.Data)
            assertEquals(expected = listOf(defaultFriendUINotSubscribed, defaultFriendUINotSubscribed2), actual = resultState.people)
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