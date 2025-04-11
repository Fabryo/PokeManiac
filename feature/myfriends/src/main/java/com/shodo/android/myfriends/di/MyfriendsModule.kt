package com.shodo.android.myfriends.di

import com.shodo.android.coreui.navigator.MyFriendsNavigator
import com.shodo.android.myfriends.myfrienddetail.MyFriendDetailViewModel
import com.shodo.android.myfriends.myfriendlist.MyFriendListViewModel
import com.shodo.android.myfriends.navigator.MyFriendsNavigatorImpl
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val myFriendsModule = module {
    single<MyFriendsNavigator> { MyFriendsNavigatorImpl() }
    viewModelOf(::MyFriendListViewModel)
    viewModelOf(::MyFriendDetailViewModel)
}