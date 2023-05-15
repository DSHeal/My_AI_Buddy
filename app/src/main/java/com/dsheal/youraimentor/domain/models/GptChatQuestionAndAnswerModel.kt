package com.dsheal.youraimentor.domain.models

data class GptChatQuestionAndAnswerModel(
    val id: String,
    val created: Long,
    val question: String,
    val answer: String
)
