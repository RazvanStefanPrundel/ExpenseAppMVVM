package com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.data.database.entities.Expense
import com.example.expenseappmvvm.data.database.prefs.PreferencesProvider
import com.example.expenseappmvvm.data.database.repositories.ExpenseRepository
import com.example.expenseappmvvm.utils.rxUtils.AppRxSchedulers
import com.example.expenseappmvvm.utils.rxUtils.disposeBy
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.*

class ViewPagerViewModel(
    private val expenseRepository: ExpenseRepository,
    private val compositeDisposable: CompositeDisposable,
    private val rxSchedulers: AppRxSchedulers,
    private val prefs: PreferencesProvider
) : ViewModel() {
    val pieDataSet = MutableLiveData<PieDataSet>()

    val expense = MutableLiveData<Expense>().apply { value = Expense() }
    val expensesAmount = MutableLiveData<Double>().apply { value = 0.0 }
    val expensesList = MutableLiveData<List<Expense>>().apply { value = mutableListOf() }

    private val userId = prefs.getUserId()

    fun initDataForTab(from: Long, to: Long) {
        expensesAmount.value?.let {
            expenseRepository.getExpensesAmountFromTo(from, to, userId)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    expensesAmount.value = -it
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }

        expensesList.value?.let {
            expenseRepository.getActionsFromTo(from, to, userId)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    expensesList.value = it
                }, {
                    Timber.e(it.localizedMessage)
                })
        }

        initPieChartData(expensesAmount.value!!, expensesList.value!!)
    }

    //TODO
    private fun initPieChartData(totalAmount: Double, expenses: List<Expense>) {

        val pieEntries: ArrayList<PieEntry> = ArrayList()

        val categorySum = MutableList(7) { 0.0 }
        val categoryName = listOf("Food", "Car", "Clothes", "Savings", "Health", "Beauty", "Travel")

        for (i in expenses) {
            for (j in 0..6) {
                if (i.expenseCategoryName == categoryName[j]) {
                    categorySum[j] += i.expenseAmount
                }
            }
        }

        for (i in 0..6) {
            if (categorySum[i] != 0.0) {
                pieEntries.add(PieEntry(((categorySum[i] / totalAmount) * 100).toFloat(), categoryName[i]))
            }
        }

        pieDataSet.value = PieDataSet(pieEntries, "")
    }

}