package com.noiprocs.gnik.randomreminder.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;

import com.noiprocs.gnik.randomreminder.MainReminderActivity;
import com.noiprocs.gnik.randomreminder.R;
import com.noiprocs.gnik.randomreminder.RandomReminderUtil;
import com.noiprocs.gnik.randomreminder.core.MemoryAiderException;
import com.noiprocs.gnik.randomreminder.sqlite.RandomReminder;

public class RandomReminderService extends IntentService {
    private static final int NOTIFICATION_ID = 3;
    private RandomReminder mRandomReminder;

    public RandomReminderService() {
        super("RandomReminderService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            mRandomReminder = new RandomReminder(this);
            mRandomReminder.loadData();
        } catch (MemoryAiderException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        RandomReminderUtil.initializedPreference(this);
        Notification.Builder builder = new Notification.Builder(this);
        try {
            String message = mRandomReminder.getRandomLeaf();
            builder.setContentTitle("Random reminder").setContentText(message).setSmallIcon(R.mipmap.ic_launcher_round);
            Intent notifyIntent = new Intent(this, MainReminderActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, notifyIntent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent).setAutoCancel(true);
            if (RandomReminderUtil.getBoolean(getResources().getString(R.string.key_vibration))) {
                builder.setDefaults(Notification.DEFAULT_VIBRATE);
            }
            Notification notification = builder.build();
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(NOTIFICATION_ID, notification);
        } catch (MemoryAiderException e) {
            e.printStackTrace();
        }
    }
}
