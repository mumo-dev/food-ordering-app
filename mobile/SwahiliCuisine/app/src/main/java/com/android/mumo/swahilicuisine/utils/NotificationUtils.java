package com.android.mumo.swahilicuisine.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.mumo.swahilicuisine.ConfirmOrderActivity;
import com.android.mumo.swahilicuisine.MainActivity;
import com.android.mumo.swahilicuisine.R;


public class NotificationUtils {
    private static final String CHANNEL_ID = "reminder_notification_id";
    private static final int NOTIFICATION_ID = 1000;


    public static void sendReminderNotification(Context context, String notificationMessage) {
        Log.i("JobService", "notification send ");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Order Notification";
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription(context.getString(R.string.reminder_channel_description));
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Order Status")
                .setContentText(notificationMessage)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    private static PendingIntent contentIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ConfirmOrderActivity.EXTRA_FROM_CONFIRM_ORDER, "yes");
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
