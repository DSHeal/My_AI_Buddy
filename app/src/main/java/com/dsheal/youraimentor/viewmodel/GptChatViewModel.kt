package com.dsheal.youraimentor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsheal.youraimentor.domain.models.GptChatQuestionAndAnswerModel
import com.dsheal.youraimentor.domain.models.GptChatResponseModel
import com.dsheal.youraimentor.domain.repository.GptChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GptChatViewModel @Inject constructor(private val repository: GptChatRepository) :
    ViewModel() {

    private val _answer = MutableStateFlow<GptChatResponseModel?>(null)
    val answer: StateFlow<GptChatResponseModel?> = _answer

    private val questionsAndAnswersList = MutableLiveData<List<GptChatQuestionAndAnswerModel>>()
    val questionsAndAnswers: LiveData<List<GptChatQuestionAndAnswerModel>> = questionsAndAnswersList

    init {
        listenAllPreviousQuestionsAndAnswersFromDb()
    }

    fun getAllQAndAsFromRemoteDb() {

    }

    fun getAnswerFromApiAndSave(question: String) {
        viewModelScope.launch {
            repository.getChatCompletionAndSaveToDb(question)
        }
            .invokeOnCompletion {
                listenAnswerFromDb()
            }
    }

    private fun listenAnswerFromDb() {
        viewModelScope.launch {
            _answer.value = repository.listenAnswerFromDb()
        }
    }

    fun listenAllPreviousQuestionsAndAnswersFromDb() {
        viewModelScope.launch {
            questionsAndAnswersList.value = repository.listenAllQuestionAndAnswersFromDb()
        }
    }
}