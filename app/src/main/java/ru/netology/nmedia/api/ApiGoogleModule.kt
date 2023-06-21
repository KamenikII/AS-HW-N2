package ru.netology.nmedia.api

import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**класс, предоставляющий зависимости с google*/

@InstallIn(SingletonComponent::class) //зависимость на уровне всего приложения
@Module //предоставляет зависимости
class ApiGoogleModule {
    @Provides
    @Singleton
    fun provideFirebaseApp(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Provides
    @Singleton
    fun provideGoogleApiAvailability(): GoogleApiAvailability = GoogleApiAvailability.getInstance()
}