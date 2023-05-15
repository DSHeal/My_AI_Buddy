package com.dsheal.youraimentor.di

import android.content.Context
import androidx.room.Room
import com.dsheal.youraimentor.data.Api
import com.dsheal.youraimentor.data.database.GptChatDao
import com.dsheal.youraimentor.data.database.GptChatDatabase
import com.dsheal.youraimentor.data.mappers.GptChatMapper
import com.dsheal.youraimentor.data.repositoryImpl.GptChatRepositoryImpl
import com.dsheal.youraimentor.data.repositoryImpl.UserRepositoryImpl
import com.dsheal.youraimentor.domain.repository.GptChatRepository
import com.dsheal.youraimentor.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): GptChatDatabase {
        return Room.databaseBuilder(context, GptChatDatabase::class.java, "ai_buddy_database")
            .build()
    }

    @Provides
    @Singleton
    fun provideGptChatResponseDao(database: GptChatDatabase): GptChatDao {
        return database.gptChatDao()
    }

    @Provides
    @Singleton
    fun provideGptChatRepository(
        api: Api, database: GptChatDatabase,
        firebaseDatabase: FirebaseDatabase,
        firebaseAuth: FirebaseAuth,
        mapper: GptChatMapper
    ): GptChatRepository {
        return GptChatRepositoryImpl(
            api, database, firebaseDatabase, mapper, firebaseAuth
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        database: GptChatDatabase,
        firebaseDatabase: FirebaseDatabase,
        mapper: GptChatMapper
    ): UserRepository {
        return UserRepositoryImpl(database, firebaseDatabase, mapper)
    }
}