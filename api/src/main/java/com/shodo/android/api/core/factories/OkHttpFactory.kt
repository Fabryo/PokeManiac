package com.shodo.android.api.core.factories

import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.util.concurrent.TimeUnit.MINUTES

object OkHttpFactory : KoinComponent {

    fun okHttpClient(): OkHttpClient {
        return clientBuilder()
            .build()
    }

    private fun clientBuilder(): Builder {
        return get<Builder>()
            .connectTimeout(2, MINUTES)
            .readTimeout(2, MINUTES)
            .writeTimeout(2, MINUTES)
            .addMarvelApiInterceptor()
    }

    private fun Builder.addMarvelApiInterceptor(): Builder =
        addInterceptor { chain ->
            val httpUrl = chain.request().url.newBuilder()
                .build()

            val requestBuilder = chain.request().newBuilder().url(httpUrl)

            chain.proceed(requestBuilder.build())
        }

}