package ru.netology.nmedia.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.PostRemoteKeyDao
import ru.netology.nmedia.dao.PostRemoteKeyEntity
import ru.netology.nmedia.entity.PostEntity

/** КЛАСС, ОТВЕЧАЮЩИЙ ЗА РАБОТУ БД */

@Database(entities = [PostEntity::class, PostRemoteKeyEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
}