package com.saref.rss_reader.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public final class SampleBootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(final Context context,final Intent intent)
    {
        final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
        final String QUICKBOOT_POWERON = "android.intent.action.QUICKBOOT_POWERON";
        if (BOOT_COMPLETED.equals(intent.getAction()) || QUICKBOOT_POWERON.equals(intent.getAction()))
        {
            context.startService(SetAlarmsService.getAlarmsServiceIntent(context));
        }
    }
}
