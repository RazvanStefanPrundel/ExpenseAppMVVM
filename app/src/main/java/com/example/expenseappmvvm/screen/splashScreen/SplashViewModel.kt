package com.example.expenseappmvvm.screen.splashScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.data.database.entities.User
import com.example.expenseappmvvm.data.database.prefs.PreferencesProvider
import com.example.expenseappmvvm.data.database.repositories.UserRepository
import com.example.expenseappmvvm.utils.SingleLiveEvent
import com.example.expenseappmvvm.utils.rxUtils.AppRxSchedulers
import com.example.expenseappmvvm.utils.rxUtils.disposeBy
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class SplashViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val rxSchedulers: AppRxSchedulers,
    private val userRepository: UserRepository,
    private val prefs: PreferencesProvider
) : ViewModel() {
    val autoLogin = SingleLiveEvent<Any>()
    val user = MutableLiveData<User>().apply { value = User() }

    fun verifyAutoLogin() {

        if (prefs.getUserId() != 0L) {
            user.value?.let {
                userRepository.getUserById(prefs.getUserId()!!)
                    .subscribeOn(rxSchedulers.background())
                    .observeOn(rxSchedulers.androidUI())
                    .subscribe({
                        autoLogin.call()
                    }, {
                        Timber.e(it.localizedMessage)
                    })
                    .disposeBy(compositeDisposable)
            }
        }
    }

}