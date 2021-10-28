package com.example.expenseappmvvm.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.screen.actionScreen.adapter.CategoryModel

@Entity(tableName = "Expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) var expenseId: Long = 0,
    @ColumnInfo(name="expenseDate") var expenseDate: Long = 0,
    @ColumnInfo(name="expenseAmount") var expenseAmount: Double = 0.0,
    @ColumnInfo(name="expenseCategoryName") var expenseCategoryName: String = "",
    @ColumnInfo(name="expenseCategoryImg") var expenseCategoryImg: Int = R.drawable.img_income,
    @ColumnInfo(name="expenseDetails") var expenseDetails: String = "",
    @ColumnInfo(name="expenseRemainedAmount") var expenseRemainedAmount: Double = 0.0,
    @ColumnInfo(name="expenseUserId") var expenseUserId: Long = 0,
    @ColumnInfo(name="expensePhoto") var expensePhoto: String = "",
)