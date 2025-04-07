package com.shodo.android.posttransaction.di

import com.shodo.android.posttransaction.step1.PostTransactionStep1ViewModel
import com.shodo.android.posttransaction.step2.PostTransactionStep2ViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val postTransactionModule = module {
    viewModel { PostTransactionStep1ViewModel() }
    viewModel { PostTransactionStep2ViewModel(get()) }
}