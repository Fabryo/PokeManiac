package com.shodo.android.searchfriend.di

import com.shodo.android.coreui.navigator.SearchFriendNavigator
import com.shodo.android.searchfriend.SearchFriendViewModel
import com.shodo.android.searchfriend.navigator.SearchFriendNavigatorImpl
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchFriendModule = module {
    single<SearchFriendNavigator> { SearchFriendNavigatorImpl() }
    viewModelOf(::SearchFriendViewModel)
}