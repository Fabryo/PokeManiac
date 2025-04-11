package com.shodo.android.pokemaniac.welcome.di

import com.shodo.android.pokemaniac.welcome.WelcomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val welcomeModule = module {
    viewModelOf(::WelcomeViewModel)
}