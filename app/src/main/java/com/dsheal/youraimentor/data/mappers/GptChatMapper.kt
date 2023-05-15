package com.dsheal.youraimentor.data.mappers

import com.dsheal.youraimentor.data.database.GptChatQuestionAndAnswerEntity
import com.dsheal.youraimentor.data.database.GptChatResponseEntity
import com.dsheal.youraimentor.data.database.UserEntity
import com.dsheal.youraimentor.data.dto.*
import com.dsheal.youraimentor.domain.models.*
import javax.inject.Inject

class GptChatMapper @Inject constructor() {

    fun mapToEntity(dto: GptChatCompletionDto): GptChatResponseEntity {
        val message = dto.choices.first().message.content
        return GptChatResponseEntity(dto.id, dto.created, message)
    }

    fun mapQuestionAndAnswerDtoToEntity(
        dto: GptChatCompletionDto,
        question: String
    ): GptChatQuestionAndAnswerEntity =
        GptChatQuestionAndAnswerEntity(
            id = dto.id,
            created = dto.created,
            question = question,
            answer = dto.choices.first().message.content
        )

    fun mapQuestionAndAnswerModelToEntity(model: GptChatQuestionAndAnswerModel): GptChatQuestionAndAnswerEntity {
        return GptChatQuestionAndAnswerEntity(
            id = model.id,
            created = model.created,
            question = model.question,
            answer = model.answer
        )
    }

    fun mapQuestionWithAnswerToModel(
        question: String,
        answer: GptChatResponseModel
    ): GptChatQuestionAndAnswerModel {
        return GptChatQuestionAndAnswerModel(
            id = answer.id,
            created = answer.created,
            question = question,
            answer = answer.message
        )
    }

    fun mapGptChatResponseDtoToModel(dto: GptChatCompletionDto): GptChatResponseModel {
        return GptChatResponseModel(
            id = dto.id,
            created = dto.created,
            message = dto.choices.first().message.content
        )
    }

    fun mapToModel(entity: GptChatResponseEntity): GptChatResponseModel {
        return GptChatResponseModel(
            id = entity.id,
            created = entity.created,
            message = entity.message
        )
    }

    fun mapQuestionsAndAnswersEntityToModel(
        questionsAndAnswersEntityList: List<GptChatQuestionAndAnswerEntity>
    ): List<GptChatQuestionAndAnswerModel> {
        return questionsAndAnswersEntityList.map { entity ->
            GptChatQuestionAndAnswerModel(
                id = entity.id,
                created = entity.created,
                question = entity.question,
                answer = entity.answer
            )
        }
    }


    fun mapRequestDtoToModel(dto: GptChatRequestDto): GptChatRequestModel {
        val messages = dto.messages.map { mapRequestMessageDtoToModel(it) }
        return GptChatRequestModel(dto.model, messages)
    }

    private fun mapRequestMessageDtoToModel(dto: GptChatRequestMessageDto): GptChatMessageModel {
        return GptChatMessageModel(dto.role, dto.content)
    }

    fun mapModelToRequestDto(
        request: String,
        qAndAsListHistory: List<GptChatQuestionAndAnswerModel>
    ): GptChatRequestDto {
        return GptChatRequestDto(
            model = "gpt-3.5-turbo",
            messages =
            mapQAndAHistoryListToGptChatRequestMessageDtoList(qAndAsListHistory) +
                    listOf(
                        GptChatRequestMessageDto(
                            role = "user",
                            content = request
                        )
                    )
        )
    }

    fun mapQAndAHistoryListToGptChatRequestMessageDtoList(
        qAndAsListHistory: List<GptChatQuestionAndAnswerModel>
    ): List<GptChatRequestMessageDto> {
        val mutList = mutableListOf<GptChatRequestMessageDto>()
        for (model in qAndAsListHistory) {
            mutList.add(
                GptChatRequestMessageDto(
                    role = "user",
                    content = model.question
                )
            )
            mutList.add(
                GptChatRequestMessageDto(
                    role = "assistant",
                    content = model.answer
                )
            )
        }
        val finalList = mutList.toList()
        return finalList
    }

    fun mapUserModelToUserEntity(user: User): UserEntity {
        return UserEntity(
            id = 0,
            email = user.email
        )
    }

    fun mapUserEntityToUserModel(userEntity: UserEntity): User {
        return User(
            email = userEntity.email
        )
    }
}