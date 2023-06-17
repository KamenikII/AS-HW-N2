package ru.netology.nmedia.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.dao.PostDao
import javax.inject.Singleton

/**Модуль для работы БД*/

@InstallIn(SingletonComponent::class) //зависимость на уровне всего приложения
@Module
class DBModule {

    @Singleton //Аннотация, которое говорит на какое время создано.
    // В данном случае один раз на всё приложние
    @Provides
    fun provideDB(
        @ApplicationContext
        context: Context
    ): AppDb = Room.databaseBuilder(context, AppDb::class.java, "app.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun providePostDao(
        appDb: AppDb
    ): PostDao = appDb.postDao()
}

