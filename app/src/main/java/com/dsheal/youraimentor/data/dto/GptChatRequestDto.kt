package com.dsheal.youraimentor.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GptChatRequestDto(
    @Json(name = "model") val model: String,
    @Json(name = "messages") val messages: List<GptChatRequestMessageDto>
)

@JsonClass(generateAdapter = true)
data class GptChatRequestMessageDto(
    @Json(name = "role") val role: String,
    @Json(name = "content") val content: String
)