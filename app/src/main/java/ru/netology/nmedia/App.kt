package ru.netology.nmedia

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.netology.nmedia.di.DependencyContainer

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyContainer.initApp(this)
    }
}