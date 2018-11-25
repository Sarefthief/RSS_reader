package com.saref.rss_reader.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.saref.rss_reader.R;
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
        final ArrayList<String> channelsLinksList = intent.getStringArrayListExtra(ALARM_EXTRA);
        for (int i = 0; i < channelsLinksList.size(); i++)
        {
            context.startService(FeedParserService.getParserServiceIntent(context, channelsLinksList.get(i)));
        }
    }

    public void setAlarm(final Context context, final ArrayList<String> channelsLinkList)
    {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String updateTime = sharedPreferences.getString(context.getString(R.string.alarmTimerPreferenceKey),context.getString(R.string.alarmTimerPreferenceDefault));
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent receiverIntent = new Intent(context, AlarmReceiver.class);
        receiverIntent.putStringArrayListExtra(ALARM_EXTRA, channelsLinkList);
        final PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (null != alarmManager)
        {
            alarmManager.cancel(alarmIntent);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * Integer.parseInt(updateTime), alarmIntent);
        }
    }

    public void cancelAlarm(final Context context)
    {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent receiverIntent = new Intent(context, AlarmReceiver.class);
        final PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (null != alarmManager)
        {
            alarmManager.cancel(alarmIntent);
        }
    }
}
