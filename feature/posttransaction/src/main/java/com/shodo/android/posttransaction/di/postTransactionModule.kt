package com.shodo.android.posttransaction.di

import com.shodo.android.posttransaction.step1.PostTransactionStep1ViewModel
import com.shodo.android.posttransaction.step2.PostTransactionStep2ViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val postTransactionModule = module {
    viewModelOf(::PostTransactionStep1ViewModel)
    viewModelOf(::PostTransactionStep2ViewModel)
}