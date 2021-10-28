package com.adiph.expensesapp.data.converters

import androidx.room.TypeConverter
import com.example.expenseappmvvm.screen.actionScreen.adapter.CategoryModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CategoryConverter {
    @TypeConverter
    fun fromArrayList(earningsByYear: CategoryModel): String {
        return Gson().toJson(earningsByYear)
    }

    @TypeConverter
    fun fromString(earningsByYear: String): CategoryModel {
        val type = object : TypeToken<CategoryModel>() {}.type
        return Gson().fromJson(earningsByYear, type)
    }
}