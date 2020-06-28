package com.thecrimsonpizza.tvtrackerkotlin.device

import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.core.base.MainActivity

private const val NOTIFICATION_ID = 3

class NewSeasonNotifications : IntentService("NewSeasonNotifications") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val builder = Notification.Builder(this)
            builder.setContentTitle("My Title")
            builder.setContentText("This is the Body")
            builder.setSmallIcon(R.drawable.proto_logo)
            val notifyIntent = Intent(this, MainActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            //to be able to launch your activity from the notification
            builder.setContentIntent(pendingIntent)
            val notificationCompat = builder.build()
            val managerCompat = NotificationManagerCompat.from(this)
            managerCompat.notify(NOTIFICATION_ID, notificationCompat)
//            final String action = intent.getAction();
//            if (ACTION_FOO.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionFoo(param1, param2);
//            } else if (ACTION_BAZ.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionBaz(param1, param2);
//            }
        }
    }
}