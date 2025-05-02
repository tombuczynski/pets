package com.packt.pets.firebase

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.packt.pets.MainActivity
import com.packt.pets.R
import com.packt.pets.thutils.AppNotifications

/**
 * Created by Tom Buczynski on 01.05.2025.
 */
class FirebaseNotificationService : FirebaseMessagingService() {
    lateinit var notifications: AppNotifications

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // Log.d("Firebase message", message.notification?.body.toString())

        postNotification(this, message)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("Firebase", "New messaging token: $token")
    }

    override fun onCreate() {
        super.onCreate()

        notifications = AppNotifications("PetsNewsMessage", "Pets News", "Notifies about Pets App news")
    }

    private fun postNotification(c: Context, msg: RemoteMessage) {
        val tapIntent: Intent = Intent(c, MainActivity::class.java)
        tapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        notifications.showNotification(
            c,
            msg.notification?.title,
            msg.notification?.body,
            msg.notification?.ticker,
            1,
            R.drawable.ic_launcher_foreground,
            tapIntent,
        )
    }
}