package com.example.expenseappmvvm.data.database.repositories

import com.example.expenseappmvvm.data.database.AppDatabase
import com.example.expenseappmvvm.data.database.entities.User
import io.reactivex.Single

class UserRepository(private val appDatabase: AppDatabase) {
    fun insertUser(user: User): Single<Long> {
        return appDatabase.userDao().insertUser(user)
    }

    fun getUserLogin(email: String, password: String): Single<User> {
        return appDatabase.userDao().getUserLogin(email, password)
    }

    fun getUserName(id: Long): Single<String> {
        return appDatabase.userDao().getUserName(id)
    }

    fun getUserByEmail(email: String): Single<User> {
        return appDatabase.userDao().getUserByEmail(email)
    }

    fun getUserById(idUser: Long): Single<User> {
        return appDatabase.userDao().getUserById(idUser)
    }
}