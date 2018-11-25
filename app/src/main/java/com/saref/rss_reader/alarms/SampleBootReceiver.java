package com.saref.rss_reader.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public final class SampleBootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(final Context context,final Intent intent)
    {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction()))
        {
            context.startService(SetAlarmsService.getAlarmsServiceIntent(context));
        }
    }
}
