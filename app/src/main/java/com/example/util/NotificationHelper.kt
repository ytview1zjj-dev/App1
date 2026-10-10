package com.example.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import com.example.model.PrankItem
import com.example.receiver.PrankNotificationReceiver
import java.io.File

object NotificationHelper {

    const val CHANNEL_ID = "prank_message_channel"
    const val CHANNEL_NAME = "Prank & Incoming Messages"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Realistic incoming messages and prank notifications"
                enableLights(true)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 200, 300)
                setShowBadge(true)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleNotification(context: Context, prank: PrankItem) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, PrankNotificationReceiver::class.java).apply {
            putExtra("EXTRA_PRANK_ID", prank.id)
            putExtra("EXTRA_SENDER_NAME", prank.senderName)
            putExtra("EXTRA_APP_TYPE", prank.appType)
            putExtra("EXTRA_APP_LABEL", prank.appLabel)
            putExtra("EXTRA_TITLE", prank.title)
            putExtra("EXTRA_MESSAGE", prank.message)
            putExtra("EXTRA_IMAGE_PATH", prank.imagePath)
            if (prank.presetImageResId != null) {
                putExtra("EXTRA_PRESET_IMAGE_RES", prank.presetImageResId)
            }
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            prank.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    prank.scheduledTimeMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    prank.scheduledTimeMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            // Fallback for cases where SCHEDULE_EXACT_ALARM is restricted
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                prank.scheduledTimeMillis,
                pendingIntent
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelScheduledNotification(context: Context, prankId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, PrankNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            prankId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun showNotification(
        context: Context,
        id: String,
        senderName: String,
        appType: String,
        appLabel: String,
        title: String,
        message: String,
        imagePath: String?,
        presetImageResId: Int?
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Intent to launch app when tapped
        val contentIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            id.hashCode(),
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Determine icon based on appType
        val smallIconRes = when (appType) {
            "bank" -> R.drawable.ic_notification_bank
            "police" -> R.drawable.ic_notification_police
            "love" -> R.drawable.ic_notification_love
            else -> R.drawable.ic_notification_prank
        }

        // Decode bitmap if attached
        var attachedBitmap: Bitmap? = null
        if (!imagePath.isNullOrEmpty()) {
            val imgFile = File(imagePath)
            if (imgFile.exists()) {
                attachedBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            }
        }
        if (attachedBitmap == null && presetImageResId != null && presetImageResId != 0) {
            try {
                attachedBitmap = BitmapFactory.decodeResource(context.resources, presetImageResId)
            } catch (_: Exception) {}
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIconRes)
            .setContentTitle(if (senderName.isNotBlank()) senderName else title)
            .setContentText(message)
            .setSubText(appLabel)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setSound(defaultSoundUri)
            .setVibrate(longArrayOf(0, 300, 200, 300))
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        // Set BigPictureStyle if image is attached, else BigTextStyle
        if (attachedBitmap != null) {
            val bigPicStyle = NotificationCompat.BigPictureStyle()
                .bigPicture(attachedBitmap)
                .setBigContentTitle(if (senderName.isNotBlank()) senderName else title)
                .setSummaryText(message)
            builder.setStyle(bigPicStyle)
            // Also set a rounded thumbnail as large icon
            builder.setLargeIcon(createCircularBitmap(attachedBitmap))
        } else {
            val bigTextStyle = NotificationCompat.BigTextStyle()
                .bigText(message)
                .setBigContentTitle(if (senderName.isNotBlank()) senderName else title)
                .setSummaryText(appLabel)
            builder.setStyle(bigTextStyle)
        }

        notificationManager.notify(id.hashCode(), builder.build())
    }

    private fun createCircularBitmap(src: Bitmap): Bitmap {
        val size = Math.min(src.width, src.height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint().apply {
            isAntiAlias = true
        }
        val rect = Rect(
            (src.width - size) / 2,
            (src.height - size) / 2,
            (src.width + size) / 2,
            (src.height + size) / 2
        )
        val rectF = RectF(0f, 0f, size.toFloat(), size.toFloat())
        canvas.drawOval(rectF, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(src, rect, rectF, paint)
        return output
    }
}
