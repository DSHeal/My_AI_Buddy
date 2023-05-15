package com.dsheal.youraimentor.domain.models

data class GptChatRequestModel(
    val model: String,
    val messages: List<GptChatMessageModel>
)

data class GptChatMessageModel(
    val role: String,
    val content: String
)
