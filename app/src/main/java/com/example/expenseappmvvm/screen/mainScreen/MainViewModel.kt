package com.example.expenseappmvvm.screen.mainScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.data.database.prefs.PreferencesProvider
import com.example.expenseappmvvm.data.database.repositories.UserRepository
import com.example.expenseappmvvm.utils.rxUtils.AppRxSchedulers
import com.example.expenseappmvvm.utils.rxUtils.disposeBy
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MainViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val rxSchedulers: AppRxSchedulers,
    private val userRepository: UserRepository,
    private val prefs: PreferencesProvider
) : ViewModel() {
    val userName = MutableLiveData<String>().apply { value = "" }

    fun getUserName() {
        userName.value?.let {
            userRepository.getUserName(prefs.getUserId())
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    userName.value = it
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable).toString()
        }
    }

    //TODO
    fun logoutUserOnClick() {
        prefs.discardUserId()
    }
}