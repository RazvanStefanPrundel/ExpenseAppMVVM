package com.example.expenseappmvvm.data.database.repositories

import com.example.expenseappmvvm.data.database.AppDatabase
import com.example.expenseappmvvm.data.database.entities.Expense
import io.reactivex.Observable
import io.reactivex.Single

class ExpenseRepository(private val appDatabase: AppDatabase) {
    fun insertExpense(expense: Expense): Single<Long> {
        return appDatabase.expenseDao().insertExpense(expense)
    }

    fun updateExpense(expense: Expense): Single<Int> {
        return appDatabase.expenseDao().updateExpense(expense)
    }

    fun deleteExpense(expense: Expense): Single<Int> {
        return appDatabase.expenseDao().deleteExpense(expense)
    }

    fun getExpense(idExpense: Long): Single<Expense> {
        return appDatabase.expenseDao().getExpense(idExpense)
    }

    fun getActionsFromTo(expensesFrom: Long, expensesTo: Long, idUser: Long): Observable<List<Expense>> {
        return appDatabase.expenseDao().getActionsFromTo(expensesFrom, expensesTo, idUser)
    }

    fun getExpensesAmountFromTo(expensesFrom: Long, expensesTo: Long, idUser: Long): Observable<Double> {
        return appDatabase.expenseDao().getExpensesAmountFromTo(expensesFrom, expensesTo, idUser)
    }

    fun getCurrentBalance(idUser: Long): Observable<Double> {
        return appDatabase.expenseDao().getCurrentBalance(idUser)
    }

    fun getExpenseFromTo(fromDate: Long, toDate: Long, idUser: Long): Observable<Double> {
        return appDatabase.expenseDao().getActionFromTo(fromDate, toDate, idUser)
    }
}