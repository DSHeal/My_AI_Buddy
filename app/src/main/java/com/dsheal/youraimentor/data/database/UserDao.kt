package com.dsheal.youraimentor.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao {

    @Insert
    abstract fun insertUser(user: UserEntity)

    @Delete
    abstract fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM user")
    abstract fun listenUserFromDb(): Flow<UserEntity>
}