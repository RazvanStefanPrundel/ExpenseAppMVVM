package com.example.expenseappmvvm.app.dependencies

import com.example.expenseappmvvm.screen.mainScreen.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelsModule: Module = module {

    viewModel { MainViewModel(get(), get()) }
    //viewModel { SplashViewModel() }
    //viewModel { LoginViewModel() }
}