package com.albertabdullin.testtask.util

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.albertabdullin.testtask.R
import com.albertabdullin.testtask.ui.MainActivity

class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        val titleText = p1!!.getStringExtra(TITLE_TEXT_KEY).toString()
        val textContent = p1.getStringExtra(TEXT_CONTEXT_KEY).toString()
        showNotification(p0!!, getNotificationBuilder(p0, titleText, textContent))
    }

    private fun getNotificationBuilder(context: Context, titleText: String, textContent: String)
            : NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val builder = NotificationCompat.Builder(
                context, context.getString(R.string.height_dollar_exchange_rate_id))
                .setContentTitle(titleText)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(textContent))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            builder.priority = NotificationCompat.PRIORITY_DEFAULT
        return builder
    }

    private fun showNotification(context: Context, builder: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(context)) {
            notify(
                context.resources.getInteger(R.integer.height_dollar_exchange_rate_notification_id),
                builder.build()
            )
        }
    }

}