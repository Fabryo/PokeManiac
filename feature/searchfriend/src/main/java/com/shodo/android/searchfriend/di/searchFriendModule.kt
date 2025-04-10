package com.shodo.android.searchfriend.di

import com.shodo.android.searchfriend.SearchFriendViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchFriendModule = module {
    viewModelOf(::SearchFriendViewModel)
}