package com.example.expenseappmvvm.data.database.repositories

import com.example.expenseappmvvm.data.database.AppDatabase
import com.example.expenseappmvvm.data.database.entities.User

class UserRepository(private val appDatabase: AppDatabase) {
    fun insertUser(user: User) {
        appDatabase.userDao().insertUser(user)
    }
}