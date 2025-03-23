package com.shodo.android.data.di


import com.shodo.android.data.myfriends.UserRepositoryImpl
import com.shodo.android.data.myprofile.MyProfileRepositoryImpl
import com.shodo.android.data.newsfeed.NewsFeedRepositoryImpl
import com.shodo.android.domain.repositories.friends.UserRepository
import com.shodo.android.domain.repositories.myprofile.MyProfileRepository
import com.shodo.android.domain.repositories.news.NewsFeedRepository
import org.koin.dsl.module

val dataModule = module {

    // repositories
    factory<UserRepository> { UserRepositoryImpl(get(), get()) }
    factory<NewsFeedRepository> { NewsFeedRepositoryImpl(get(), get()) }
    factory<MyProfileRepository> { MyProfileRepositoryImpl(get()) }
}