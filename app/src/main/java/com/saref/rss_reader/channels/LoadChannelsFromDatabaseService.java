package com.saref.rss_reader.channels;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.content.LocalBroadcastManager;

import com.saref.rss_reader.Constants;
import com.saref.rss_reader.database.ChannelsContract;
import com.saref.rss_reader.database.DatabaseManager;

import java.util.ArrayList;
import java.util.logging.Logger;

public class LoadChannelsFromDatabaseService extends IntentService
{
    private SQLiteDatabase database;
    private static final Logger logger = Logger.getLogger(LoadChannelsFromDatabaseService.class.getName());

    private final String[] projection = {
            ChannelsContract.COLUMN_NAME_TITLE,
            ChannelsContract.COLUMN_NAME_LINK
    };

    public static Intent getLoadChannelsServiceIntent(final Activity activity)
    {
        return new Intent(activity, LoadChannelsFromDatabaseService.class);
    }

    public LoadChannelsFromDatabaseService()
    {
        super("LoadChannelsFromDatabaseService");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        database = DatabaseManager.getInstance(this).openReadableDatabase();
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
        ArrayList<Channel> channelsList = new ArrayList<>();
        Cursor cursor = null;
        try
        {
            cursor = database.query(ChannelsContract.TABLE_NAME, projection, null, null, null, null, null);
        }
        catch (SQLiteException e)
        {
            logger.severe("Cant load channels from database");
        }
        if (null != cursor)
        {
            final int titleColIndex = cursor.getColumnIndex(projection[0]);
            final int linkColIndex = cursor.getColumnIndex(projection[1]);
            while (cursor.moveToNext())
            {
                channelsList.add(new Channel(cursor.getString(titleColIndex), cursor.getString(linkColIndex)));
            }
            cursor.close();
        }
        sendBroadcast(channelsList);
    }

    private void sendBroadcast(final ArrayList<Channel> channelsList)
    {
        final Intent intent = new Intent(Constants.LOAD_CHANNELS_LIST_MESSAGE);
        intent.putParcelableArrayListExtra(Constants.LOAD_CHANNELS_LIST_MESSAGE, channelsList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
