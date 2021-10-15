package com.example.expenseappmvvm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expenseappmvvm.data.database.dao.UserDao
import com.example.expenseappmvvm.data.database.entities.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    //Add rest of Dao's here
}