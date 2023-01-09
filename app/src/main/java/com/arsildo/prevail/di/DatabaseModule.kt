package com.arsildo.prevail.di

import android.content.Context
import androidx.room.Room
import com.arsildo.prevail.data.source.local.SavedBoardsDao
import com.arsildo.prevail.data.source.local.SavedBoardsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


const val DATABASE_NAME = "SavedBoards.db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): SavedBoardsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            SavedBoardsDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideDatabaseDao(savedBoardsDatabase: SavedBoardsDatabase): SavedBoardsDao {
        return savedBoardsDatabase.boardDao()
    }

}