package com.shodo.android.myprofile.di

import com.shodo.android.domain.usecases.GetMyActivitiesUseCase
import com.shodo.android.myprofile.MyProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myProfileModule = module {
    viewModel { MyProfileViewModel(get<GetMyActivitiesUseCase>()) }
}