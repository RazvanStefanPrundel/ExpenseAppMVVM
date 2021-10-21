package com.example.expenseappmvvm.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Users", indices = [Index(value = ["userEmail"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) var userId: Long = 0,
    @ColumnInfo(name = "userName") var userName: String = "",
    @ColumnInfo(name = "userEmail") var userEmail: String = "",
    @ColumnInfo(name = "userPassword") var userPassword: String = "",
    @ColumnInfo(name = "userCurrency") var userCurrency: String = "RON",
    @ColumnInfo(name = "userImage") var userImage: ByteArray = ByteArray(0)
)
