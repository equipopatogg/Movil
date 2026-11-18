package com.pactogg.app.core.push

import android.Manifest
import android.app.Notification
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
private const val NoticeId = 301

internal fun createPushChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(PushChannel, context.getString(R.string.push_channel),
            NotificationManager.IMPORTANCE_DEFAULT).apply { lockscreenVisibility = Notification.VISIBILITY_PRIVATE }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }
}

internal fun pushAllowed(context: Context): Boolean {
    val permission = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || ContextCompat.checkSelfPermission(
        context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    val channelEnabled = Build.VERSION.SDK_INT < Build.VERSION_CODES.O ||
        context.getSystemService(NotificationManager::class.java).getNotificationChannel(PushChannel)
            ?.importance != NotificationManager.IMPORTANCE_NONE
    return permission && channelEnabled && NotificationManagerCompat.from(context).areNotificationsEnabled()
}

/** Aviso con la app abierta (PL-10 RF5): título y texto del servidor, privado en la pantalla bloqueada. */
internal fun showPush(context: Context, title: String?, body: String?) {
    createPushChannel(context)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
    if (!pushAllowed(context)) return
    val intent = Intent(context, MainActivity::class.java).setAction(PushOpen)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    val pending = PendingIntent.getActivity(context, NoticeId, intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    val text = body?.take(200)?.takeIf { it.isNotBlank() } ?: context.getString(R.string.push_new_notice)
    val notice = NotificationCompat.Builder(context, PushChannel)
        .setSmallIcon(R.drawable.ic_push_notice)
        .setContentTitle(title?.take(80)?.takeIf { it.isNotBlank() } ?: context.getString(R.string.app_name))
        .setContentText(text)
        .setStyle(NotificationCompat.BigTextStyle().bigText(text))
        .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
        .setContentIntent(pending).setAutoCancel(true).build()
    // El permiso puede retirarse entre la revisión y el aviso.
    runCatching { NotificationManagerCompat.from(context).notify(NoticeId, notice) }
}

internal fun cancelPush(context: Context) = NotificationManagerCompat.from(context).cancel(NoticeId)
