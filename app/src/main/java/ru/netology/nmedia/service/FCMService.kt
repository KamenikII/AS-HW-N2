package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth
import kotlin.random.Random

/** КЛАСС, ОТВЕЧАЮЩИЙ ЗА СЕРВЕР И УВЕДОМЛЕНИЯ */

class FCMService : FirebaseMessagingService() {
    private val action = "action"
    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

    lateinit var appAuth:AppAuth

    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.data[action]?.let { actionType ->
            val listAction = Action.values().map { it.name }
            if (!listAction.contains(actionType)) {
                handleUnknownAction()
                return
            }
            when (Action.valueOf(actionType)) {
                Action.LIKE -> handleLike(gson.fromJson(message.data[content], LikeFCM::class.java))
                Action.SEND_POST -> handleSendPost(
                    gson.fromJson(
                        message.data[content],
                        NewPost::class.java
                    )
                )
            }
            return
        }

        val myId = appAuth.authStateFlow.value.id
        val inputNoty = message.data.values.map {
            gson.fromJson(it, Noty::class.java)
        }[0]

        when (inputNoty.recipientId) {
            myId -> {
                handleNotyAction(inputNoty, "Personal mail")
            }
            null -> {
                handleNotyAction(inputNoty, "Mass mailing")
            }
            else -> {
                handleNotyAction(inputNoty, "Invalid authentication, re-send token")
                appAuth.sendPushToken()
            }
        }
    }

    override fun onNewToken(token: String) {
        AppAuth.getInstance().sendPushToken(token)
    }

    private fun handleLike(content: LikeFCM) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    content.userName,
                    content.postAuthor,
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    private fun handleSendPost(content: NewPost) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_sending_post,
                    content.userName
                )
            )
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(content.textPost)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    private fun handleUnknownAction() {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.unknown_action))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(getString(R.string.obscure_operation))
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    private fun handleNotyAction(content: Noty, message: String) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(content.content)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    enum class Action { LIKE, SEND_POST}

    data class LikeFCM(
        val userId: Long,
        val userName: String,
        val postId: Long,
        val postAuthor: String,
    )

    data class NewPost(
        val userName: String,
        val textPost: String
    )

    data class Noty(
        val recipientId: Long? = null,
        val content: String
    )
}