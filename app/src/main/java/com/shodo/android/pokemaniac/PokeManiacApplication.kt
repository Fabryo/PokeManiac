package com.shodo.android.pokemaniac

import android.app.Application
import com.shodo.android.api.di.apiModule
import com.shodo.android.data.di.dataModule
import com.shodo.android.database.di.databaseModule
import com.shodo.android.di.trackingModule
import com.shodo.android.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PokeManiacApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@PokeManiacApplication)
            modules(
                trackingModule,
                databaseModule,
                apiModule,
                dataModule,
                domainModule
            )
        }
    }
}