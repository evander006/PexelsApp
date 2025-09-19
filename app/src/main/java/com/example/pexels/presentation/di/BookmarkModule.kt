package com.example.pexels.presentation.di

import android.content.Context
import androidx.room.Room
import com.example.pexels.data.repoImpl.BookmarkPhotoRepositoryImpl
import com.example.pexels.data.repository.BookmarkPhotoDao
import com.example.pexels.data.roomdb.AppDatabase
import com.example.pexels.domain.repository.BookmarkPhotoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(
            context, AppDatabase::class.java,"bookmarktable_db"
        ).build()
    }
    @Provides
    fun providesRoomDao(database: AppDatabase): BookmarkPhotoDao{
        return database.bookmarkPhotoDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BookmarkModule{
    @Binds
    abstract fun bindBookmarkRepo(
        impl: BookmarkPhotoRepositoryImpl
    ): BookmarkPhotoRepository
}


