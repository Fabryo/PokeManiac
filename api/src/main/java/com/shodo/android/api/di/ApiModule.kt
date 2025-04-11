package com.shodo.android.api.di


import com.shodo.android.api.core.factories.RetrofitFactory.retrofitWith
import com.shodo.android.api.core.remote.SuperHerosApiService
import com.shodo.android.api.core.urlprovider.BaseUrlProvider
import com.shodo.android.api.request.FriendsRequestImpl
import com.shodo.android.data.myfriends.FriendsRequest
import okhttp3.OkHttpClient
import org.koin.dsl.module

val apiModule = module {

    // network
    factory { BaseUrlProvider() }
    factory { OkHttpClient.Builder() }
    factory { retrofitWith().create(SuperHerosApiService::class.java) }

    // request
    factory<FriendsRequest> { FriendsRequestImpl(get()) }
}