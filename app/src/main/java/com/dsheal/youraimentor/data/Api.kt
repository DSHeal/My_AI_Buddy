package com.dsheal.youraimentor.data

import com.dsheal.youraimentor.data.dto.GptChatCompletionDto
import com.dsheal.youraimentor.data.dto.GptChatRequestDto
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface Api {

    @POST("chat/completions")
    @Headers("Content-Type: application/json")
    suspend fun postChatCompletion(@Body request: GptChatRequestDto): GptChatCompletionDto
}