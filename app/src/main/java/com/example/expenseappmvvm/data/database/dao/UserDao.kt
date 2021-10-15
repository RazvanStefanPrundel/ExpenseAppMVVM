package com.example.expenseappmvvm.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expenseappmvvm.data.database.entities.User
import io.reactivex.Observable

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("Select * from Users where userId=:userId")
    fun getUserById(userId: String): Observable<User>

    @Query("DELETE FROM Users")
    fun clearUserData()
}