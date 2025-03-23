package com.shodo.android.searchfriend.di

import com.shodo.android.domain.usecases.SearchFriendUseCase
import com.shodo.android.domain.usecases.SendTrackingEventClickUseCase
import com.shodo.android.domain.usecases.SendTrackingEventScreenUseCase
import com.shodo.android.domain.usecases.SubscribeFriendUseCase
import com.shodo.android.domain.usecases.UnsubscribeFriendUseCase
import com.shodo.android.searchfriend.SearchFriendViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchFriendModule = module {
    viewModel { SearchFriendViewModel(
        get<SearchFriendUseCase>(),
        get<SubscribeFriendUseCase>(),
        get<UnsubscribeFriendUseCase>(),
        get<SendTrackingEventScreenUseCase>(),
        get<SendTrackingEventClickUseCase>(),
    ) }
}