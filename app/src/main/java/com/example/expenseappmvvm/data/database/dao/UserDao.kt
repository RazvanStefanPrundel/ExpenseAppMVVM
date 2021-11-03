package com.example.expenseappmvvm.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expenseappmvvm.data.database.entities.User
import io.reactivex.Single

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Single<Long>

    @Query("SELECT * FROM Users WHERE userEmail=:email and userPassword=:password")
    fun getUserLogin(email: String, password: String): Single<User>

    @Query("SELECT userName FROM Users WHERE userId=:idUser")
    fun getUserName(idUser: Long): Single<String>

    @Query("SELECT * FROM Users WHERE userEmail=:emailUser")
    fun getUserByEmail(emailUser: String): Single<User>

    @Query("SELECT * FROM Users WHERE userId=:idUser")
    fun getUserById(idUser: Long): Single<User>
}