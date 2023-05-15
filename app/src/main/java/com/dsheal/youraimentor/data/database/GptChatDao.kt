package com.dsheal.youraimentor.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class GptChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(gptChatResponseEntity: GptChatResponseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertQuestionAndAnswer(gptChatQuestionAndAnswerEntity: GptChatQuestionAndAnswerEntity)

    @Delete
    abstract suspend fun delete(gptChatResponseEntity: GptChatResponseEntity)

    @Query("SELECT * FROM gpt_chat_response WHERE id = :id")
    abstract suspend fun getById(id: String): GptChatResponseEntity

    @Query("SELECT * FROM gpt_chat_response")
    abstract suspend fun getAllResponses(): List<GptChatResponseEntity>

    @Query("SELECT * FROM gpt_chat_question_and_answer")
    abstract suspend fun getAllQuestionsAndAnswers(): List<GptChatQuestionAndAnswerEntity>

    @Query("SELECT * FROM gpt_chat_question_and_answer ORDER BY created DESC")
    abstract suspend fun getAllQuestionsAndAnswersFreshGoFirst(): List<GptChatQuestionAndAnswerEntity>
}