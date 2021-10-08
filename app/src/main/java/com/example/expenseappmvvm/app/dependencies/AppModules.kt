package com.example.expenseappmvvm.app.dependencies

import com.example.expenseappmvvm.utils.rxUtils.AppRxSchedulers
import com.example.expenseappmvvm.utils.rxUtils.RxBus
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.module.Module
import org.koin.dsl.module

val rxModules: Module = module {
    single { AppRxSchedulers() }
    single { RxBus() }
    factory { CompositeDisposable() }
}

