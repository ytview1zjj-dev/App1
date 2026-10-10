package com.example

import android.app.Application
import com.example.util.NotificationHelper

class PrankApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}
