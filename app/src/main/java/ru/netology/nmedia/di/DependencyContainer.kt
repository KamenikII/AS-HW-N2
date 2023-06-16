package ru.netology.nmedia.di

import android.content.Context
import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl

/** КЛАСС, ОТВЕЧАЮЩИЙ ЗА ЗАВИСИМОСТИ */

class DependencyContainer(
    private val context: Context,
) {
    //компаньон
    companion object {
        //с ApiService
        private const val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"

        //с AppDb
        @Volatile
        private var instance: DependencyContainer? = null

        fun initApp(context: Context) {
            instance = DependencyContainer(context)
        }

        fun getInstance(): DependencyContainer {
            return instance!!
        }
    }

    //c AppAuth
    val appAuth = AppAuth(context)

    //с ApiService
    private val logging = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okhttp = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            appAuth.authStateFlow.value.token?.let { token ->
                chain
                    .request()
                    .newBuilder()
                    .addHeader("Authorization", token)
                    .build()
                    .apply { return@addInterceptor chain.proceed(this) }
            }
            return@addInterceptor chain.proceed(chain.request())
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okhttp)
        .build()

    val apiService = retrofit.create<ApiService>()

    //с AppDb
    private val appDB = Room.databaseBuilder(context, AppDb::class.java, "app.db")
        .fallbackToDestructiveMigration()
        .build()

    //с пост дао
    private val postDao = appDB.postDao()

    //c репозитория
    val repository: PostRepository = PostRepositorySQLiteImpl(
        postDao,
        apiService,
    )
}