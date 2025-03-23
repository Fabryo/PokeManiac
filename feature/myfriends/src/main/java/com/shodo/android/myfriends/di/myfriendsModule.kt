package com.shodo.android.myfriends.di

import com.shodo.android.domain.usecases.GetUserUseCase
import com.shodo.android.domain.usecases.GetMyFriendsUseCase
import com.shodo.android.domain.usecases.UnsubscribeFriendUseCase
import com.shodo.android.myfriends.myfrienddetail.MyFriendDetailViewModel
import com.shodo.android.myfriends.myfriendlist.MyFriendListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myFriendsModule = module {
    viewModel { MyFriendListViewModel(get<GetMyFriendsUseCase>(), get<UnsubscribeFriendUseCase>()) }
    viewModel { MyFriendDetailViewModel(get<GetUserUseCase>(), get<UnsubscribeFriendUseCase>()) }
}