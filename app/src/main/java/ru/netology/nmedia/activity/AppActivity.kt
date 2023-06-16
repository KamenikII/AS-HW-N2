package ru.netology.nmedia.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import ru.netology.nmedia.R
import ru.netology.nmedia.di.DependencyContainer
import ru.netology.nmedia.viewmodel.AuthViewModel
import ru.netology.nmedia.viewmodel.ViewModelFactory


/** ДАННЫЙ КЛАСС ОТВЕЧАЕТ ЗА ЗАПУСК ПРИЛОЖЕНИЯ И РАБОТУ С СЕРВИСАМИ GOOGLE */

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    private val dependencyContainer = DependencyContainer.getInstance()
    private val viewModel: AuthViewModel by viewModels(
        factoryProducer = {
            ViewModelFactory(dependencyContainer.repository,
                dependencyContainer.appAuth,
                dependencyContainer.apiService,
            )
        }
    )

    //запуск приложения
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkGoogleApiAvailability()
    }


    //работаем с сервисами гугл
    private fun checkGoogleApiAvailability() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(this@AppActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@AppActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(this@AppActivity, R.string.google_play_unavailable, Toast.LENGTH_LONG)
                .show()
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            println(it)
        }
    }
}
