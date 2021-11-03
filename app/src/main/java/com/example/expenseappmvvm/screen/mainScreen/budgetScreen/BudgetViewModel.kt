package com.example.expenseappmvvm.screen.mainScreen.budgetScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.data.database.entities.Expense
import com.example.expenseappmvvm.data.database.prefs.PreferencesProvider
import com.example.expenseappmvvm.data.database.repositories.ExpenseRepository
import com.example.expenseappmvvm.utils.CalendarUtils.Companion.getStartOfDay
import com.example.expenseappmvvm.utils.CalendarUtils.Companion.getStartOfMonth
import com.example.expenseappmvvm.utils.CalendarUtils.Companion.getStartOfWeek
import com.example.expenseappmvvm.utils.rxUtils.AppRxSchedulers
import com.example.expenseappmvvm.utils.rxUtils.disposeBy
import com.github.mikephil.charting.data.BarData
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
    val barData = MutableLiveData<BarData>().apply { value = BarData() }

    val expense = MutableLiveData<Expense>().apply { value = Expense() }

    var todayExpense = MutableLiveData<Double>().apply { value = 0.0 }
    var weekExpense = MutableLiveData<Double>().apply { value = 0.0 }
    var monthExpense = MutableLiveData<Double>().apply { value = 0.0 }

    var currentBalance = MutableLiveData<Double>().apply { value = 0.0 }

    val barDataSetP = MutableLiveData<BarDataSet>()
    val barDataSetN = MutableLiveData<BarDataSet>()

    private val userId = prefs.getUserId()

    private val cal = Calendar.getInstance()

    private val sumOfMonth = MutableList(12) { 0.0 }

    private val barsEntriesP: ArrayList<BarEntry> = ArrayList()
    private val barsEntriesN: ArrayList<BarEntry> = ArrayList()

    fun initAmounts() {
        currentBalance.value?.let {
            expenseRepository.getCurrentBalance(userId)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    currentBalance.value = it
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }

        val todayS = getStartOfDay(0, 0)
        val todayE = getStartOfDay(23, 59)

        val weekS = getStartOfWeek(cal.firstDayOfWeek)
        val weekE = getStartOfWeek(cal.getActualMaximum(Calendar.DAY_OF_WEEK))

        val monthS = getStartOfMonth(cal.get(Calendar.MONTH), 1)
        val monthE = getStartOfMonth(cal.getActualMaximum(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH))

        todayExpense.value?.let {
            expenseRepository.getExpensesAmountFromTo(todayS, todayE, userId)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    todayExpense.value = -it
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }

        weekExpense.value?.let {
            expenseRepository.getExpensesAmountFromTo(weekS, weekE, userId)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    weekExpense.value = -it
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }

        monthExpense.value?.let {
            expenseRepository.getExpensesAmountFromTo(monthS, monthE, userId)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    monthExpense.value = -it
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }
    }

    //TODO
    fun initBarChartData() {
        for (i in 0..11) {
            val firstDayOfMonth = getStartOfMonth(i, 1)
            val firstDayOfNextMonth = getStartOfMonth(i + 1, 1)
            expense.value?.let {
                expenseRepository.getExpenseFromTo(firstDayOfMonth, firstDayOfNextMonth, userId)
                    .observeOn(rxSchedulers.androidUI())
                    .subscribe({
                        sumOfMonth[i] = it
                    }, {
                        Timber.e(it.localizedMessage)
                    }).disposeBy(compositeDisposable)

                if (sumOfMonth[i] < 0.0) {
                    barsEntriesN.add(BarEntry(i.toFloat(), sumOfMonth[i].toFloat()))
                    barDataSetN.value = BarDataSet(barsEntriesN, "Negative")
                } else {
                    barsEntriesP.add(BarEntry(i.toFloat(), sumOfMonth[i].toFloat()))
                    barDataSetP.value = BarDataSet(barsEntriesP, "Positive")
                }
            }
        }

        barData.value!!.addDataSet(barDataSetP.value)
        barData.value!!.addDataSet(barDataSetN.value)
    }

}