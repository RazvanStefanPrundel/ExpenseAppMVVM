package com.example.expenseappmvvm.data.database.dao

import androidx.room.*
import com.example.expenseappmvvm.data.database.entities.Expense
import com.example.expenseappmvvm.data.database.entities.User
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpense(expenses: Expense): Single<Long>

    @Update
    fun updateExpense(expenses: Expense)

    @Delete
    fun deleteExpense(expenses: Expense)

    @Query("SELECT * FROM Expenses WHERE expenseId=:idExpense")
    fun getExpense(idExpense: Long): Expense

    @Query("SELECT * FROM Expenses WHERE expenseDate>:expensesFrom AND expenseUserId=:idUser")
    fun getActionsFrom(expensesFrom: Long, idUser: Long): Observable<List<Expense>>

    @Query("SELECT sum(expenseAmount) FROM Expenses WHERE expenseDate>:expensesFrom AND expenseCategoryName NOT LiKE 'Income' AND expenseUserId=:idUser")
    fun getExpensesAmount(expensesFrom: Long, idUser: Long): Observable<Double>

    @Query("SELECT sum(expenseAmount) FROM Expenses WHERE expenseUserId=:idUser")
    fun getCurrentBalance(idUser: Long): Observable<Double>

    @Query("SELECT sum(expenseAmount) FROM Expenses WHERE expenseDate>=:fromDate AND expenseDate<:toDate AND expenseUserId=:idUser")
    fun getExpenseFromTo(fromDate: Long, toDate: Long, idUser: Long): Observable<Double>
}