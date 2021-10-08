package com.example.expenseappmvvm.screen.mainScreen

import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.utils.rxUtils.AppRxSchedulers
import io.reactivex.disposables.CompositeDisposable

class MainViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val rxSchedulers: AppRxSchedulers
) : ViewModel() {
}