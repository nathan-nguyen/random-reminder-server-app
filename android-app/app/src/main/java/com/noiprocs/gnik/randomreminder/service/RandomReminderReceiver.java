package com.noiprocs.gnik.randomreminder.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RandomReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, RandomReminderService.class);
        context.startService(serviceIntent);
    }
}
