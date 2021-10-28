package com.example.expenseappmvvm.screen.mainScreen.budgetScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.data.database.entities.Expense
import com.example.expenseappmvvm.data.database.prefs.PreferencesProvider
import com.example.expenseappmvvm.data.database.repositories.ExpenseRepository
import com.example.expenseappmvvm.utils.rxUtils.AppRxSchedulers
import com.example.expenseappmvvm.utils.rxUtils.disposeBy
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.*

class BudgetViewModel(
    private val expenseRepository: ExpenseRepository,
    private val compositeDisposable: CompositeDisposable,
    private val rxSchedulers: AppRxSchedulers,
    private val prefs: PreferencesProvider
) : ViewModel() {

    val expense = MutableLiveData<Expense>().apply { value = Expense() }

    var currentBalance = MutableLiveData<String>().apply { value = "0.0" }

    var todayExpense = MutableLiveData<String>().apply { value = "0.0" }
    var weekExpense = MutableLiveData<String>().apply { value = "0.0" }
    var monthExpense = MutableLiveData<String>().apply { value = "0.0" }

    private val cal = Calendar.getInstance()
    private val userId = prefs.getUserId()!!

    fun initAmounts() {
        expense.value?.let {
            expenseRepository.getCurrentBalance(userId)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    currentBalance.value = it.toString()
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }.toString()

        cal.set(Calendar.HOUR_OF_DAY, 1)
        val today = cal.timeInMillis
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        val week = cal.timeInMillis
        cal.set(Calendar.DAY_OF_MONTH, 1)
        val month = cal.timeInMillis

        expense.value?.let {
            expenseRepository.getExpensesAmount(today, userId)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    todayExpense.value = it.toString()
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }.toString()

        expense.value?.let {
            expenseRepository.getExpensesAmount(week, userId)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    weekExpense.value = it.toString()
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }.toString()

        expense.value?.let {
            expenseRepository.getExpensesAmount(month, userId)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    monthExpense.value = it.toString()
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }.toString()

    }

}