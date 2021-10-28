package com.example.expenseappmvvm.data.database.repositories

import com.example.expenseappmvvm.data.database.AppDatabase
import com.example.expenseappmvvm.data.database.entities.Expense
import io.reactivex.Observable
import io.reactivex.Single

class ExpenseRepository(private val appDatabase: AppDatabase) {
    fun insertExpense(expenses: Expense): Single<Long> {
        return appDatabase.expenseDao().insertExpense(expenses)
    }

    fun updateExpense(expenses: Expense){
        appDatabase.expenseDao().updateExpense(expenses)
    }

    fun deleteExpense(expenses: Expense){
        appDatabase.expenseDao().deleteExpense(expenses)
    }

    fun getExpense(idExpense: Long): Expense{
        return appDatabase.expenseDao().getExpense(idExpense)
    }

    fun getActionsFrom(expensesFrom: Long, idUser: Long): Observable<List<Expense>> {
        return appDatabase.expenseDao().getActionsFrom(expensesFrom, idUser)
    }

    fun getExpensesAmount(expensesFrom: Long, idUser: Long): Observable<Double> {
        return appDatabase.expenseDao().getExpensesAmount(expensesFrom, idUser)
    }

    fun getCurrentBalance(idUser: Long): Observable<Double> {
        return appDatabase.expenseDao().getCurrentBalance(idUser)
    }

    fun getExpenseFromTo(fromDate: Long, toDate: Long, idUser: Long): Observable<Double> {
        return appDatabase.expenseDao().getExpenseFromTo(fromDate, toDate, idUser)
    }
}