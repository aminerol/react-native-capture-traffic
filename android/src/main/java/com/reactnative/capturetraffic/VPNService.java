package com.reactnative.capturetraffic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.facebook.react.bridge.ReactContext;
import com.github.megatronking.netbare.NetBareService;

public class VPNService extends NetBareService {

    private static final String CHANNEL_ID = "com.reactnative.capturetraffic.NOTIFICATION_CHANNEL_ID";
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID,
                        getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW));
            }
        }
    }

    @Override
    protected int notificationId() {
        return 102;
    }

    @NonNull
    @Override
    protected Notification createNotification() {

        Intent intent = new Intent(this, ReactContext.class);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.netbare_notification)
                .setContentTitle("VPN ON")
                .setContentText("Filtering youtube")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.netbare_notification))
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .build();
    }
}
