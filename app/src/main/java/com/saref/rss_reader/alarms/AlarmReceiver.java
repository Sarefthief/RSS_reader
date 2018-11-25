package com.saref.rss_reader.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.saref.rss_reader.news.parser.FeedParserService;

import java.util.ArrayList;
import java.util.Calendar;

public final class AlarmReceiver extends BroadcastReceiver
{
    public static final String ALARM_EXTRA = "ALARM_EXTRA";

    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show();
        ArrayList<String> channelsLinksList = intent.getStringArrayListExtra(ALARM_EXTRA);
        for (int i = 0; i < channelsLinksList.size(); i++)
        {
            context.startService(FeedParserService.getParserServiceIntent(context, channelsLinksList.get(i)));
        }
    }

    public void setAlarm(final Context context, final ArrayList<String> channelsLinkList)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent receiverIntent = new Intent(context, AlarmReceiver.class);
        receiverIntent.putStringArrayListExtra(ALARM_EXTRA, channelsLinkList);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (null != alarmManager)
        {
            alarmManager.cancel(alarmIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
        }
    }

    public void cancelAlarm(final Context context)
    {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent receiverIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (null != alarmManager)
        {
            alarmManager.cancel(alarmIntent);
        }
    }
}
