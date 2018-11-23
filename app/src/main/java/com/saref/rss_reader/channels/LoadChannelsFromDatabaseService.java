package com.saref.rss_reader.channels;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.content.LocalBroadcastManager;

import com.saref.rss_reader.database.ChannelsContract;
import com.saref.rss_reader.database.RssReaderDbHelper;

import java.util.ArrayList;

public class LoadChannelsFromDatabaseService extends IntentService
{
    private SQLiteDatabase database;
    private RssReaderDbHelper dbHelper;

    private String[] projection = {
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
        dbHelper = new RssReaderDbHelper(this);
        database = dbHelper.getReadableDatabase();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        database.close();
        dbHelper.close();
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
        final Intent intent = new Intent(ChannelsActivity.LOAD_CHANNELS_LIST_MESSAGE);
        intent.putParcelableArrayListExtra(ChannelsActivity.LOAD_CHANNELS_LIST_MESSAGE, channelsList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
