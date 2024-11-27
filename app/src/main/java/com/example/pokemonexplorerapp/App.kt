package com.example.pokemonexplorerapp

import android.app.Application
import com.example.data.di.koinDependenciesData
import com.example.domain.di.koinDependenciesDomain
import com.example.pokemonexplorerapp.di.koinDependenciesApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        if (GlobalContext.getKoinApplicationOrNull() == null) {
            startKoin {
                androidLogger()
                androidContext(this@App)
                modules(
                    listOf(
                        koinDependenciesApp,
                        koinDependenciesData,
                        koinDependenciesDomain
                    )
                )
            }
        }
    }
}