package com.saref.rss_reader.database;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.content.LocalBroadcastManager;

import com.saref.rss_reader.Constants;
import com.saref.rss_reader.alarms.AlarmReceiver;
import com.saref.rss_reader.alarms.SetAlarmsService;

public class DeleteChannelService extends IntentService
{
    private SQLiteDatabase database;

    public DeleteChannelService()
    {
        super("DeleteChannelService");
    }

    public static Intent getDeleteChannelServiceIntent(final Context context, final String link)
    {
        final Intent intent = new Intent(context, DeleteChannelService.class);
        intent.putExtra(Constants.DELETE_CHANNEL_EXTRA, link);
        return intent;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        database = DatabaseManager.getInstance(this).openWritableDatabase();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        DatabaseManager.getInstance(this).closeDatabase();
    }

    @Override
    protected void onHandleIntent(final Intent intent)
    {
        if (null != intent.getExtras())
        {
            final String selection = ChannelsContract.COLUMN_NAME_LINK + " = ?";
            final String[] selectionArgs = {intent.getStringExtra(Constants.DELETE_CHANNEL_EXTRA)};

            try
            {
                final AlarmReceiver alarmReceiver = new AlarmReceiver();
                alarmReceiver.cancelAlarm(this);
                database.delete(ChannelsContract.TABLE_NAME, selection, selectionArgs);
                sendBroadcast();
                startService(SetAlarmsService.getAlarmsServiceIntent(this));
            }
            catch (SQLiteException e)
            {

            }
        }
    }

    private void sendBroadcast()
    {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.CHANNEL_IS_DELETED));
    }
}
