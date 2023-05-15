package com.dsheal.youraimentor.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gpt_chat_response")
data class GptChatResponseEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "created")
    val created: Long,
    @ColumnInfo(name = "message")
    val message: String
)