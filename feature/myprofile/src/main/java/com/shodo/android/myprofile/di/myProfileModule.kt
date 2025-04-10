package com.shodo.android.myprofile.di

import com.shodo.android.myprofile.MyProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val myProfileModule = module {
    viewModelOf(::MyProfileViewModel)
}