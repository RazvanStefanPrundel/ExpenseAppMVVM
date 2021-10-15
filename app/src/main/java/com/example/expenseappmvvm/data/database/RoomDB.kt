package com.example.expenseappmvvm.data.database

import android.content.Context
import androidx.room.Room

class RoomDB(context: Context) {

    val appDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    companion object {
        const val DATABASE_NAME = "ExpenseDatabase"
    }
}