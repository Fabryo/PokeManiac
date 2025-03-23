package com.shodo.android.dashboard.di

import com.shodo.android.dashboard.DashboardViewModel
import com.shodo.android.domain.usecases.GetNewActivitiesUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dashboardModule = module {
    viewModel { DashboardViewModel(get<GetNewActivitiesUseCase>()) }
}