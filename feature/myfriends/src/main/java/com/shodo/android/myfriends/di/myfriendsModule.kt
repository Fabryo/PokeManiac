package com.shodo.android.myfriends.di

import com.shodo.android.myfriends.myfrienddetail.MyFriendDetailViewModel
import com.shodo.android.myfriends.myfriendlist.MyFriendListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val myFriendsModule = module {
    viewModelOf(::MyFriendListViewModel)
    viewModelOf(::MyFriendDetailViewModel)
}