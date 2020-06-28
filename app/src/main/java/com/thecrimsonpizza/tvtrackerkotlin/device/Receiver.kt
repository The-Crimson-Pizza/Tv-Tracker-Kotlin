package com.thecrimsonpizza.tvtrackerkotlin.device

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.GROUP_KEY_SEASON_NEW
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.NEW_SEASON_NOTIFICATION_BUNDLE_CHANNEL_ID
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.NEW_SEASON_NOTIFICATION_BUNDLE_CHANNEL_NAME
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.NEW_SEASON_NOTIFICATION_CHANNEL_ID
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.NEW_SEASON_NOTIFICATION_CHANNEL_NAME
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.SEASON_ID_EXTRA
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.SEASON_NUMBER_EXTRA
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.SERIE_NOMBRE_EXTRA

class Receiver : BroadcastReceiver() {
    private var notificationManager: NotificationManager? = null

    //    NotificationManagerCompat notificationManager;
    private var summaryNotificationBuilder: NotificationCompat.Builder? = null
    override fun onReceive(context: Context, intent: Intent) {
        val name = intent.getStringExtra(SERIE_NOMBRE_EXTRA)
        val numTemporada = intent.getIntExtra(SEASON_NUMBER_EXTRA, 0)
        val id = intent.getIntExtra(SEASON_ID_EXTRA, 0)
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //        notificationManager = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if (notificationManager.getNotificationChannels().size() < 2) {
            createBundleChannel()
            createNotificationChannel()
            //            }
        }
        summaryNotificationBuilder = getSummaryNotificationBuilder(context)
        val notification =
            NotificationCompat.Builder(context, NEW_SEASON_NOTIFICATION_CHANNEL_ID)
                .setGroup(GROUP_KEY_SEASON_NEW)
                .setContentTitle("Nueva temporada")
                .setContentText(
                    context.getString(
                        R.string.notificacion_nueva_temporada,
                        numTemporada.toString(),
                        name
                    )
                )
                .setSmallIcon(R.drawable.proto_logo)
                .setGroupSummary(false)
        notificationManager!!.notify(id, notification.build())
        notificationManager!!.notify(0, summaryNotificationBuilder!!.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NEW_SEASON_NOTIFICATION_CHANNEL_ID,
                NEW_SEASON_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager!!.createNotificationChannel(channel)
        }
    }

    private fun createBundleChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val groupChannel = NotificationChannel(
                NEW_SEASON_NOTIFICATION_BUNDLE_CHANNEL_ID,
                NEW_SEASON_NOTIFICATION_BUNDLE_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager!!.createNotificationChannel(groupChannel)
        }
    }

    private fun getSummaryNotificationBuilder(context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NEW_SEASON_NOTIFICATION_BUNDLE_CHANNEL_ID)
            .setGroup(GROUP_KEY_SEASON_NEW)
            .setGroupSummary(true)
            .setContentTitle("Nuevas temporadas")
            .setContentText("Aqui aparecen new seasons")
            .setSmallIcon(R.drawable.proto_logo)
    }

    companion object {
        const val ACTION_ALARM_RECEIVER = "NewSeasonReceiver"
    }
}