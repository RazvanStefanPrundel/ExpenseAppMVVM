package com.example.expenseappmvvm.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expenseappmvvm.R

@Entity(tableName = "Expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) var expenseId: Long = 0,
    var expenseDate: Long = 0,
    var expenseAmount: Double = 0.0,
    var expenseCategoryName: String = "",
    var expenseCategoryImg: Int = R.drawable.img_income,
    var expenseDetails: String = "",
    var expenseRemainedAmount: Double = 0.0,
    var expenseUserId: Long = 0,
    var expensePhoto: String = "",
)