package com.example.expenseappmvvm.app.dependencies

import com.example.expenseappmvvm.screen.loginScreen.LoginViewModel
import com.example.expenseappmvvm.screen.mainScreen.MainViewModel
import com.example.expenseappmvvm.screen.registerScreen.RegisterViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelsModule: Module = module {

    viewModel { MainViewModel(get(), get()) }
    //viewModel { SplashViewModel() }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { RegisterViewModel(get(), get(), get()) }
}