package com.example.expenseappmvvm.app

import android.app.Application
import com.example.expenseappmvvm.BuildConfig
import com.example.expenseappmvvm.app.dependencies.databaseModule
import com.example.expenseappmvvm.app.dependencies.preferencesModule
import com.example.expenseappmvvm.app.dependencies.rxModules
import com.example.expenseappmvvm.app.dependencies.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class ExpenseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        app = this
        initKoin()
        if (BuildConfig.DEBUG) {
            initTimber()
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@ExpenseApp)
            modules(
                listOf(
                    databaseModule,
                    preferencesModule,
                    viewModelsModule,
                    rxModules
                )
            )
        }
    }

    companion object {
        lateinit var app: ExpenseApp
    }
}