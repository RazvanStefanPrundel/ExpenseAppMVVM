package com.example.expenseappmvvm.screen.mainScreen.budgetScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.data.database.entities.Expense
import com.example.expenseappmvvm.data.database.prefs.PreferencesProvider
import com.example.expenseappmvvm.data.database.repositories.ExpenseRepository
import com.example.expenseappmvvm.utils.rxUtils.AppRxSchedulers
import com.example.expenseappmvvm.utils.rxUtils.disposeBy
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.*

class BudgetViewModel(
    private val expenseRepository: ExpenseRepository,
    private val compositeDisposable: CompositeDisposable,
    private val rxSchedulers: AppRxSchedulers,
    private val prefs: PreferencesProvider
) : ViewModel() {
    val barsEntriesP: ArrayList<BarEntry> = ArrayList()
    val barsEntriesN: ArrayList<BarEntry> = ArrayList()

    val expense = MutableLiveData<Expense>().apply { value = Expense() }

    var todayExpense = MutableLiveData<Double>().apply { value = 0.0 }
    var weekExpense = MutableLiveData<Double>().apply { value = 0.0 }
    var monthExpense = MutableLiveData<Double>().apply { value = 0.0 }

    var currentBalance = MutableLiveData<Double>().apply { value = 0.0 }

    private val userId = prefs.getUserId()!!

    private val cal = Calendar.getInstance()

    private val sumOfMonth = MutableList(12) { 0.0 }

    private var sumOfMonthAmount = MutableLiveData<Double>().apply { value = 0.0 }

    private val barDataSetP = MutableLiveData<BarDataSet>().apply { value = BarDataSet(barsEntriesP, "") }
    private val barDataSetN = MutableLiveData<BarDataSet>().apply { value = BarDataSet(barsEntriesN, "") }

    fun initAmounts() {
        expense.value?.let {
            expenseRepository.getCurrentBalance(userId)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    currentBalance.value = it
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }

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
                    todayExpense.value = it
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }

        expense.value?.let {
            expenseRepository.getExpensesAmount(week, userId)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    weekExpense.value = it
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }

        expense.value?.let {
            expenseRepository.getExpensesAmount(month, userId)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    monthExpense.value = it
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }
    }

    fun initBarChartData() {
        for (i in 0..11) {
            val firstDayOfMonth = getFirstDayOfMonth(i)
            val firstDayOfNextMonth = getFirstDayOfMonth(i + 1)
            sumOfMonthAmount.value?.let {
                expenseRepository.getExpenseFromTo(firstDayOfMonth, firstDayOfNextMonth, userId)
                    .observeOn(rxSchedulers.androidUI())
                    .subscribe({
                        sumOfMonth[i] = it
                    }, {
                        Timber.e(it.localizedMessage)
                    }).disposeBy(compositeDisposable)
                sumOfMonth[i]

                if (sumOfMonth[i] < 0.0) {
                    barsEntriesN.add(BarEntry(i.toFloat(), sumOfMonth[i].toFloat()))
                    barDataSetN.value = BarDataSet(barsEntriesN, "Negative")
                } else {
                    barsEntriesP.add(BarEntry(i.toFloat(), sumOfMonth[i].toFloat()))
                    barDataSetP.value = BarDataSet(barsEntriesP, "Positive")
                }
            }
        }
    }

    private fun getFirstDayOfMonth(monthOfYear: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        return calendar.timeInMillis
    }

}