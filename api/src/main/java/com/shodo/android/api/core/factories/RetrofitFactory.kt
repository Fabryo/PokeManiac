package com.shodo.android.api.core.factories

import com.shodo.android.api.core.urlprovider.BaseUrlProvider
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory : KoinComponent {

    private val baseUrlProvider = get<BaseUrlProvider>()

    fun retrofitWith(okHttpClient: OkHttpClient = OkHttpFactory.okHttpClient()): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrlProvider.get())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}