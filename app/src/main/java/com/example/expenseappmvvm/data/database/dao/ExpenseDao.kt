package com.example.expenseappmvvm.data.database.dao

import androidx.room.*
import com.example.expenseappmvvm.data.database.entities.Expense
import com.example.expenseappmvvm.data.database.entities.User
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpense(expense: Expense): Single<Long>

    @Update
    fun updateExpense(expense: Expense): Single<Int>

    @Delete
    fun deleteExpense(expense: Expense): Single<Int>

    @Query("SELECT * FROM Expenses WHERE expenseId=:idExpense")
    fun getExpense(idExpense: Long): Single<Expense>

    @Query("SELECT * FROM Expenses WHERE expenseDate>=:expensesFrom AND expenseDate<=:expensesTo AND expenseUserId=:idUser")
    fun getActionsFromTo(expensesFrom: Long, expensesTo: Long, idUser: Long): Observable<List<Expense>>

    @Query("SELECT sum(expenseAmount) FROM Expenses WHERE expenseDate>=:expensesFrom AND expenseDate<=:expensesTo AND expenseAmount < 0.0 AND expenseUserId=:idUser")
    fun getExpensesAmountFromTo(expensesFrom: Long, expensesTo: Long, idUser: Long): Observable<Double>

    @Query("SELECT sum(expenseAmount) FROM Expenses WHERE expenseUserId=:idUser")
    fun getCurrentBalance(idUser: Long): Observable<Double>

    @Query("SELECT sum(expenseAmount) FROM Expenses WHERE expenseDate>=:fromDate AND expenseDate<:toDate AND expenseUserId=:idUser")
    fun getActionFromTo(fromDate: Long, toDate: Long, idUser: Long): Observable<Double>
}