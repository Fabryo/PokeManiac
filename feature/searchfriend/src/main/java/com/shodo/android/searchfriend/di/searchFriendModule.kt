package com.shodo.android.searchfriend.di

import com.shodo.android.searchfriend.SearchFriendViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchFriendModule = module {
    viewModel { SearchFriendViewModel(get(), get()) }
}