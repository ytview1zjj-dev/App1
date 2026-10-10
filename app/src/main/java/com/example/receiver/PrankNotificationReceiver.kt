package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.PrankRepository
import com.example.util.NotificationHelper

class PrankNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getStringExtra("EXTRA_PRANK_ID") ?: return
        val senderName = intent.getStringExtra("EXTRA_SENDER_NAME") ?: "Message"
        val appType = intent.getStringExtra("EXTRA_APP_TYPE") ?: "custom"
        val appLabel = intent.getStringExtra("EXTRA_APP_LABEL") ?: "Messages"
        val title = intent.getStringExtra("EXTRA_TITLE") ?: senderName
        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: "New message received"
        val imagePath = intent.getStringExtra("EXTRA_IMAGE_PATH")
        val presetRes = if (intent.hasExtra("EXTRA_PRESET_IMAGE_RES")) {
            intent.getIntExtra("EXTRA_PRESET_IMAGE_RES", 0)
        } else null

        // Display Notification
        NotificationHelper.showNotification(
            context = context,
            id = id,
            senderName = senderName,
            appType = appType,
            appLabel = appLabel,
            title = title,
            message = message,
            imagePath = imagePath,
            presetImageResId = if (presetRes != 0) presetRes else null
        )

        // Mark as Delivered in database
        val repo = PrankRepository(context)
        repo.updateStatus(id, "DELIVERED")
    }
}
