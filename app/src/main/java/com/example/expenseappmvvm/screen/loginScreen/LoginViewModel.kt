package com.example.expenseappmvvm.screen.loginScreen

import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.data.database.entities.User
import com.example.expenseappmvvm.data.database.repositories.UserRepository
import com.example.expenseappmvvm.utils.SingleLiveEvent
import com.example.expenseappmvvm.utils.rxUtils.AppRxSchedulers
import io.reactivex.disposables.CompositeDisposable

class LoginViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val rxSchedulers: AppRxSchedulers,
    private val userRepository: UserRepository
) : ViewModel() {

    var goToRegisterScreen = SingleLiveEvent<Any>()

    fun onRegisterLinkClick() {
        goToRegisterScreen.call()
    }

    fun createUserInDB(){
        val user = User()
        user.userName = "Mark"
        user.userEmail = "mark@gmail.com"
        user.userPassword = "test123"
        userRepository.insertUser(user)
    }
}