package com.dsheal.youraimentor.data.repositoryImpl

import com.dsheal.youraimentor.data.database.GptChatDatabase
import com.dsheal.youraimentor.data.database.UserEntity
import com.dsheal.youraimentor.data.mappers.GptChatMapper
import com.dsheal.youraimentor.domain.models.User
import com.dsheal.youraimentor.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val database: GptChatDatabase,
    private val firebaseDatabase: FirebaseDatabase,
    private val mapper: GptChatMapper
) : UserRepository {

    override fun saveUserInDb(user: User) {
        database.userDao().insertUser(mapper.mapUserModelToUserEntity(user))
    }

    override suspend fun listenUserFromDb(): Flow<User> {
        return database.userDao().listenUserFromDb().map { value: UserEntity ->
            mapper.mapUserEntityToUserModel(value)
        }
    }

    override suspend fun updateUserInRemoteDb(user: FirebaseUser?) {
        val userRef = firebaseDatabase.getReference("users/${user?.uid}")
        userRef.setValue(User(id = user?.uid ?: "", email = user?.email ?: ""))
    }
}