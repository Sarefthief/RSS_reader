package com.saref.rss_reader.alarms;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.saref.rss_reader.database.ChannelsContract;
import com.saref.rss_reader.database.DatabaseManager;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class SetAlarmsService extends IntentService
{
    private SQLiteDatabase database;
    private static final Logger logger = Logger.getLogger(SetAlarmsService.class.getName());

    public SetAlarmsService()
    {
        super("SetAlarmsService");
    }

    public static Intent getAlarmsServiceIntent(final Context context)
    {
        return new Intent(context, SetAlarmsService.class);
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
        final AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.setAlarm(this, getAllChannelsLinks());
    }

    private ArrayList<String> getAllChannelsLinks()
    {
        ArrayList<String> channelsLinksList = new ArrayList<>();

        try
        {
            final Cursor cursor = database.query(ChannelsContract.TABLE_NAME, null, null, null, null, null, null);
            final int linkColIndex = cursor.getColumnIndex(ChannelsContract.COLUMN_NAME_LINK);
            while (cursor.moveToNext())
            {
                channelsLinksList.add(cursor.getString(linkColIndex));
            }
            cursor.close();
        }
        catch (SQLiteException e)
        {
            logger.severe("SQLiteException in getAllChannelsLinks");
        }

        return channelsLinksList;
    }
}
