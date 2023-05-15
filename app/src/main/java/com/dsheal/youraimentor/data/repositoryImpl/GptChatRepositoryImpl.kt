package com.dsheal.youraimentor.data.repositoryImpl

import android.util.Log
import com.dsheal.youraimentor.data.Api
import com.dsheal.youraimentor.data.database.GptChatDatabase
import com.dsheal.youraimentor.data.mappers.GptChatMapper
import com.dsheal.youraimentor.domain.models.GptChatQuestionAndAnswerModel
import com.dsheal.youraimentor.domain.models.GptChatResponseModel
import com.dsheal.youraimentor.domain.repository.GptChatRepository
import com.dsheal.youraimentor.utils.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GptChatRepositoryImpl @Inject constructor(
    private val api: Api,
    private val database: GptChatDatabase,
    private val firebaseDatabase: FirebaseDatabase,
    private val mapper: GptChatMapper,
    private val firebaseAuth: FirebaseAuth
) : GptChatRepository {

    companion object {
        const val LOG_TAG = "GptChatRepositoryImpl"
    }

    val myRef = firebaseDatabase.getReference("items")

    override suspend fun getChatCompletionAndSaveToDb(question: String) {
        val qAndAHistoryList: List<GptChatQuestionAndAnswerModel> =
            mapper.mapQuestionsAndAnswersEntityToModel(
                database.gptChatDao().getAllQuestionsAndAnswersFreshGoFirst()
            )
        val response = api.postChatCompletion(
            mapper.mapModelToRequestDto(question, qAndAHistoryList)
        )
        database.gptChatDao()
            .insertQuestionAndAnswer(mapper.mapQuestionAndAnswerDtoToEntity(response, question))
        database.gptChatDao().insert(mapper.mapToEntity(response))

        if (firebaseAuth.currentUser != null) {
            sendDataToFirebaseDb(
                mapper.mapQuestionWithAnswerToModel(
                    question = question,
                    answer = mapper.mapGptChatResponseDtoToModel(response)
                )
            )
        }
    }

    override fun sendDataToFirebaseDb(qAndA: GptChatQuestionAndAnswerModel) {
        val currentUser = firebaseAuth.currentUser
        val userRef = firebaseDatabase.getReference("users/${currentUser?.uid}")
        userRef.child("items").push().setValue(qAndA)
//        val key = myRef.push().key
//        if (key == null) {
//            Log.w(LOG_TAG, "Couldn't get push key for posts")
//        }
//        myRef.child(key!!).setValue(qAndA)
    }

    override suspend fun getAllDataFromFirebaseDb(): Flow<State<Map<String, Any>>> = callbackFlow {
        myRef.get()
            .addOnCompleteListener { task ->
                val response = if (task.isSuccessful) {
                    val data = task.result.getValue<Map<String, HashMap<String, Any>>>()!!
                    Log.i("FROM_FB", data.toString())
                    launch {
                        saveDataFromRemoteSourceInLocalDb(data)
                    }

                    data.forEach {
                        val key = it.key
                        val value = it.value
                        val cl = value.javaClass
                        Log.i("FROM MAP", "КЛЮЧ = $key, ЗНАЧЕНИЕ = $value, КЛАСС = $cl")
                    }
                    State.Success<Map<String, Any>>(data)
                } else {
                    State.Failure(errorMessage = task.exception?.localizedMessage)
                }
                trySend(response).isSuccess
            }

        awaitClose {
            close()
        }

//        val currentUser = FirebaseAuth.getInstance().currentUser
//        val userRef = FirebaseDatabase.getInstance().getReference("users/${currentUser.uid}")
//        userRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val user = dataSnapshot.getValue(User::class.java)
//                val questions = user?.questions
//                // Do something with the questions
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle error
//            }
//        })
    }

    private suspend fun saveDataFromRemoteSourceInLocalDb(map: Map<String, HashMap<String, Any>>) {

//        map.forEach {
//            val idFromFb = it.key
//            Log.i("ID_FROM_FB", idFromFb)
//            val value = it.value
//            Log.i("VALUE_FROM_FB", value.toString())
//            var date = ""
//            var name = ""
//            var price: Long = 0
//            var category = ""
//            value.forEach { innerMap ->
//                if (innerMap.key == "purchaseDate") date = innerMap.value as String
//                Log.i("DATE", date)
//                if (innerMap.key == "spendingName") name = innerMap.value as String
//                Log.i("NAME", name)
//                if (innerMap.key == "spendingPrice") price = innerMap.value as Long
//                Log.i("PRICE", price.toString())
//                if (innerMap.key == "spendingCategory") {
//                    category = innerMap.value as String
//                    val listToSave = categoriesAlreadyAdded.plus(listOf(category))
//                    val json = Gson().toJson(listToSave)
//                    editor.putString("categories", json)
//                    editor.apply()
//                }
//                Log.i("CATEGORY", category)
//            }
//            val questionAndAnswerModel = GptChatQuestionAndAnswerModel(
//                id = idFromFb, spendingName = name,
//                spendingPrice = price.toInt(), spendingCategory = category, purchaseDate = date
//            )
//            Log.i("SPENDING", spending.toString())
//
//            saveQAndAsInDatabase(spending)
//        }
    }

    override suspend fun saveQAndAsInDatabase(qAndA: GptChatQuestionAndAnswerModel) {
        database.gptChatDao()
            .insertQuestionAndAnswer(mapper.mapQuestionAndAnswerModelToEntity(qAndA))
    }

    override suspend fun listenAnswerFromDb(): GptChatResponseModel {
        return mapper.mapToModel(database.gptChatDao().getAllResponses().last())
    }

    override suspend fun listenAllQuestionAndAnswersFromDb(): List<GptChatQuestionAndAnswerModel> {
        return mapper.mapQuestionsAndAnswersEntityToModel(
            database.gptChatDao().getAllQuestionsAndAnswers()
        )
    }
}
