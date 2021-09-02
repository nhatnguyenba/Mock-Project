package com.example.test.calendar.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotifyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationHelper = NotifyNotificationHelper(context)
        val nb = notificationHelper.channelNotification
        notificationHelper.manager?.notify(1, nb.build())
    }
}