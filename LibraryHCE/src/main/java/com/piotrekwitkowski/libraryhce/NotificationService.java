package com.piotrekwitkowski.libraryhce;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

class NotificationService {
    static final String NOTIFICATION_CHANNEL_NAME = "HCE Service";
    static final String NOTIFICATION_CHANNEL_DESCRIPTION = "HCE Service channel";
    private Context context;

    NotificationService(Context ctx) {
        this.context = ctx;
    }

    void show(String text) {
        int randomNotificationId = (int) (Math.random()*1000);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_NAME)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat.from(context).notify(randomNotificationId, builder.build());

    }
}
