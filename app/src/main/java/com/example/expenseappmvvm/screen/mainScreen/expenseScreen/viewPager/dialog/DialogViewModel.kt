package com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager.dialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.data.database.entities.Expense
import com.example.expenseappmvvm.data.database.repositories.ExpenseRepository
import com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager.ViewPagerFragment
import com.example.expenseappmvvm.utils.ImageStorageManager
import com.example.expenseappmvvm.utils.SingleLiveEvent
import com.example.expenseappmvvm.utils.rxUtils.AppRxSchedulers
import com.example.expenseappmvvm.utils.rxUtils.disposeBy
import io.reactivex.disposables.CompositeDisposable
import okhttp3.internal.notify
import timber.log.Timber

class DialogViewModel(
    private val expenseRepository: ExpenseRepository,
    private val compositeDisposable: CompositeDisposable,
    private val rxSchedulers: AppRxSchedulers
) : ViewModel()  {
    val redirectToEdit = SingleLiveEvent<Any>()
    val deleteExpense = SingleLiveEvent<String>()
    val expenseForDialog = MutableLiveData<Expense>().apply { value = Expense() }

    fun getExpenseForDialog(expenseId: Long) {
        expenseForDialog.value?.let {
            expenseRepository.getExpense(expenseId)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    expenseForDialog.value = it
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }
    }

    fun deleteExpense(){
        deleteExpense.value.let {
            expenseRepository.deleteExpense(expenseForDialog.value!!)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    deleteExpense.value = expenseForDialog.value!!.expensePhoto
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }

    }

    fun redirectToEditExpense(){
        redirectToEdit.call()
    }
}