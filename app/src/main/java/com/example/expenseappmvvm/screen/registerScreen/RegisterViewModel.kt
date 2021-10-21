package com.example.expenseappmvvm.screen.registerScreen

import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.data.database.repositories.UserRepository
import com.example.expenseappmvvm.utils.SingleLiveEvent
import com.example.expenseappmvvm.utils.rxUtils.AppRxSchedulers
import io.reactivex.disposables.CompositeDisposable

class RegisterViewModel(
    private val userRepository: UserRepository,
    private val compositeDisposable: CompositeDisposable,
    private val rxSchedulers: AppRxSchedulers
) : ViewModel() {

    var goToLoginScreen = SingleLiveEvent<Any>()

    fun onRegisterLinkClick() {
        goToLoginScreen.call()
    }
}