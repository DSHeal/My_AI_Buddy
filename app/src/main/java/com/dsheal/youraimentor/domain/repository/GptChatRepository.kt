package com.dsheal.youraimentor.domain.repository

import com.dsheal.youraimentor.domain.models.GptChatQuestionAndAnswerModel
import com.dsheal.youraimentor.domain.models.GptChatResponseModel
import com.dsheal.youraimentor.utils.State
import kotlinx.coroutines.flow.Flow

interface GptChatRepository {

    suspend fun getChatCompletionAndSaveToDb(question: String)

    fun sendDataToFirebaseDb(qAndA: GptChatQuestionAndAnswerModel)

    suspend fun saveQAndAsInDatabase(qAndA: GptChatQuestionAndAnswerModel)

    suspend fun getAllDataFromFirebaseDb(): Flow<State<Map<String, Any>>>

    suspend fun listenAnswerFromDb(): GptChatResponseModel

    suspend fun listenAllQuestionAndAnswersFromDb(): List<GptChatQuestionAndAnswerModel>
}