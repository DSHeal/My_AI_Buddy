package com.dsheal.youraimentor.domain.repository

import com.dsheal.youraimentor.domain.models.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun saveUserInDb(user: User)

    suspend fun listenUserFromDb(): Flow<User>

    suspend fun updateUserInRemoteDb(user: FirebaseUser?)
}