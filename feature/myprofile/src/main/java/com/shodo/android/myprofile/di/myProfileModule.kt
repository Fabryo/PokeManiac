package com.shodo.android.myprofile.di

import com.shodo.android.coreui.navigator.MyProfileNavigator
import com.shodo.android.myprofile.MyProfileViewModel
import com.shodo.android.myprofile.navigator.MyProfileNavigatorImpl
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val myProfileModule = module {
    single<MyProfileNavigator> { MyProfileNavigatorImpl() }
    viewModelOf(::MyProfileViewModel)
}