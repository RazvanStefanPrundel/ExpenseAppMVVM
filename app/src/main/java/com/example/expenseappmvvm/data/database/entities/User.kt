package com.example.expenseappmvvm.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Users", indices = [Index(value = ["userEmail"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) var userId: Long = 0,
    var userName: String = "",
    var userEmail: String = "",
    var userPassword: String = ""
)
