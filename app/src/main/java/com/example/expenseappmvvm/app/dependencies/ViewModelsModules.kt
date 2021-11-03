package com.example.expenseappmvvm.app.dependencies

import com.example.expenseappmvvm.screen.actionScreen.ActionViewModel
import com.example.expenseappmvvm.screen.loginScreen.LoginViewModel
import com.example.expenseappmvvm.screen.mainScreen.MainViewModel
import com.example.expenseappmvvm.screen.mainScreen.budgetScreen.BudgetViewModel
import com.example.expenseappmvvm.screen.mainScreen.expenseScreen.ExpenseViewModel
import com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager.ViewPagerViewModel
import com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager.dialog.DialogViewModel
import com.example.expenseappmvvm.screen.registerScreen.RegisterViewModel
import com.example.expenseappmvvm.screen.resetScreen.ResetViewModel
import com.example.expenseappmvvm.screen.splashScreen.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val viewModelsModule: Module = module {
    viewModel { MainViewModel(get(), get(), get(), get()) }
    viewModel { SplashViewModel(get(), get(), get(), get()) }
    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel { RegisterViewModel(get(), get(), get(), get()) }
    viewModel { ResetViewModel(get(), get(), get()) }
    viewModel { ExpenseViewModel(get(), get()) }
    viewModel { BudgetViewModel(get(), get(), get(), get()) }
    viewModel { ActionViewModel(get(), get(), get(), get()) }
    viewModel { ViewPagerViewModel(get(), get(), get(), get()) }
    viewModel { DialogViewModel(get(), get(), get()) }
}