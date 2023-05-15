package com.dsheal.youraimentor.domain.models

data class GptChatResponseModel(
    val id: String,
    val created: Long,
    val message: String
)