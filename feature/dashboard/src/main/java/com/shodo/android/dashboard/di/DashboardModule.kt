package com.shodo.android.dashboard.di

import com.shodo.android.coreui.navigator.DashboardNavigator
import com.shodo.android.dashboard.DashboardViewModel
import com.shodo.android.dashboard.navigator.DashboardNavigatorImpl
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dashboardModule = module {
    single<DashboardNavigator> { DashboardNavigatorImpl() }
    viewModelOf(::DashboardViewModel)
}