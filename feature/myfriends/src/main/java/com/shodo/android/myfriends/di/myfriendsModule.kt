package com.shodo.android.myfriends.di

import com.shodo.android.myfriends.myfrienddetail.MyFriendDetailViewModel
import com.shodo.android.myfriends.myfriendlist.MyFriendListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myFriendsModule = module {
    viewModel { MyFriendListViewModel(get()) }
    viewModel { MyFriendDetailViewModel(get()) }
}