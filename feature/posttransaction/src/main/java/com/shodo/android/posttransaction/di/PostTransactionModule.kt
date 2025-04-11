package com.shodo.android.posttransaction.di

import com.shodo.android.coreui.navigator.PostTransactionNavigator
import com.shodo.android.posttransaction.navigator.PostTransactionNavigatorImpl
import com.shodo.android.posttransaction.step1.PostTransactionStep1ViewModel
import com.shodo.android.posttransaction.step2.PostTransactionStep2ViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val postTransactionModule = module {
    single<PostTransactionNavigator> { PostTransactionNavigatorImpl() }
    viewModelOf(::PostTransactionStep1ViewModel)
    viewModelOf(::PostTransactionStep2ViewModel)
}