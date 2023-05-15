package com.dsheal.youraimentor.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GptChatCompletionDto(
    @Json(name = "id") val id: String,
    @Json(name = "object") val objectName: String,
    @Json(name = "created") val created: Long,
    @Json(name = "choices") val choices: List<GptChatChoiceDto>,
    @Json(name = "usage") val usage: GptChatUsageDto
)

@JsonClass(generateAdapter = true)
data class GptChatChoiceDto(
    @Json(name = "index") val index: Int,
    @Json(name = "message") val message: GptChatResponseMessageDto,
    @Json(name = "finish_reason") val finishReason: String
)

@JsonClass(generateAdapter = true)
data class GptChatResponseMessageDto(
    @Json(name = "role") val role: String,
    @Json(name = "content") val content: String
)
