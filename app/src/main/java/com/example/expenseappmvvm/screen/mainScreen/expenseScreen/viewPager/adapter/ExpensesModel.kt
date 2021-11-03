package com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager.adapter

import com.example.expenseappmvvm.screen.actionScreen.adapter.CategoryModel

class ExpensesModel(
    val expenseId: Long,
    val date: Long,
    val categoryName: String,
    val categoryImg: Int,
    val amount: Double,
    val type: String,
    val rAmount: Double
)