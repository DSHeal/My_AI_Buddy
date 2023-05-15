package com.dsheal.youraimentor.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GptChatResponseEntity::class, GptChatQuestionAndAnswerEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GptChatDatabase : RoomDatabase() {

    abstract fun gptChatDao(): GptChatDao
    abstract fun userDao(): UserDao
}