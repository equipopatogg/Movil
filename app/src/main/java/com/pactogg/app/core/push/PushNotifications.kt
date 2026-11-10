package com.pactogg.app.core.push

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.pactogg.app.MainActivity
import com.pactogg.app.R

internal const val PushChannel = "pactogg_default"
internal const val PushOpen = "com.pactogg.app.OPEN_NOTIFICATIONS"

internal fun createPushChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(
            NotificationChannel(PushChannel, context.getString(R.string.push_channel),
                NotificationManager.IMPORTANCE_DEFAULT)
                .apply { lockscreenVisibility = android.app.Notification.VISIBILITY_PRIVATE })
    }
}

internal fun pushAllowed(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return false
    val channelEnabled = Build.VERSION.SDK_INT < Build.VERSION_CODES.O ||
        context.getSystemService(NotificationManager::class.java).getNotificationChannel(PushChannel)
            ?.importance != NotificationManager.IMPORTANCE_NONE
    return NotificationManagerCompat.from(context).areNotificationsEnabled() && channelEnabled
}

internal fun showPush(context: Context) {
    createPushChannel(context)
    if (!pushAllowed(context)) return
    val intent = Intent(context, MainActivity::class.java).setAction(PushOpen)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    val pending = PendingIntent.getActivity(context, 301, intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    val notice = NotificationCompat.Builder(context, PushChannel)
        .setSmallIcon(R.drawable.ic_push_notice)
        .setContentTitle(context.getString(R.string.app_name))
        .setContentText(context.getString(R.string.push_new_notice))
        .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
        .setContentIntent(pending).setAutoCancel(true).build()
    // Permission may be revoked between the check and notify.
    try { NotificationManagerCompat.from(context).notify(301, notice) } catch (_: SecurityException) { return }
}
