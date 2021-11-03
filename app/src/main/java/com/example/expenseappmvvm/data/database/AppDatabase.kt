package com.example.expenseappmvvm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.adiph.expensesapp.data.converters.CategoryConverter
import com.adiph.expensesapp.data.converters.DateConverter
import com.example.expenseappmvvm.data.database.dao.ExpenseDao
import com.example.expenseappmvvm.data.database.dao.UserDao
import com.example.expenseappmvvm.data.database.entities.Expense
import com.example.expenseappmvvm.data.database.entities.User

@TypeConverters(CategoryConverter::class, DateConverter::class)
@Database(
    entities = [User::class, Expense::class],
    version = 5
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun expenseDao(): ExpenseDao
    //Add rest of Dao's here
}