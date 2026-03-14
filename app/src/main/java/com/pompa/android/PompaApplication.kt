package com.pompa.android

import android.app.Application
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.pompa.android.notification.PompaNotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PompaApplication: Application() {

    companion object {
        private const val TAG = "PompaApplication"
    }

    override fun onCreate() {
        super.onCreate()
        PompaNotificationManager.createChannel(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Initial FCM token: ${task.result}")
            } else {
                Log.w(TAG, "Failed to fetch initial FCM token", task.exception)
            }
        }
        FirebaseMessaging.getInstance().subscribeToTopic("pompa_${BuildConfig.FLAVOR}")
    }
}
