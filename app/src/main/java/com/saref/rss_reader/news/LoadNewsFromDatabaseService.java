package com.saref.rss_reader.news;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.content.LocalBroadcastManager;

import com.saref.rss_reader.Constants;
import com.saref.rss_reader.database.ChannelsContract;
import com.saref.rss_reader.database.DatabaseManager;
import com.saref.rss_reader.database.NewsContract;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class LoadNewsFromDatabaseService extends IntentService
{
    private SQLiteDatabase database;
    private static final Logger logger = Logger.getLogger(LoadNewsFromDatabaseService.class.getName());

    private String[] projection = {
            NewsContract.COLUMN_NAME_TITLE,
            NewsContract.COLUMN_NAME_LINK,
            NewsContract.COLUMN_NAME_DESCRIPTION,
            NewsContract.COLUMN_NAME_DATE
    };

    public LoadNewsFromDatabaseService()
    {
        super("LoadNewsFromDatabase");
    }

    public static Intent getLoadNewsServiceIntent(final Context context, final String url)
    {
        Intent intent = new Intent(context, LoadNewsFromDatabaseService.class);
        intent.putExtra(Constants.CHANNEL_LINK_EXTRA, url);
        return intent;
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
        String link = intent.getStringExtra(Constants.CHANNEL_LINK_EXTRA);
        ArrayList<FeedItem> newsList = new ArrayList<>();
        Cursor cursor = null;
        String selection = NewsContract.COLUMN_NAME_CHANNEL_ID + " = ?";
        int channelId = getChannelId(link);
        String[] selectionArgs = {String.valueOf(channelId)};

        try
        {
            cursor = database.query(NewsContract.TABLE_NAME, projection, selection, selectionArgs, null, null, NewsContract._ID + " DESC");
        }
        catch (SQLiteException e)
        {
            logger.severe("Cant get news with channel Id = " + channelId);
        }
        if (null != cursor)
        {
            final int titleColIndex = cursor.getColumnIndex(projection[0]);
            final int linkColIndex = cursor.getColumnIndex(projection[1]);
            final int descriptionColIndex = cursor.getColumnIndex(projection[2]);
            final int dateColIndex = cursor.getColumnIndex(projection[3]);

            while (cursor.moveToNext())
            {
                newsList.add(new FeedItem(cursor.getString(titleColIndex), cursor.getString(linkColIndex), cursor.getString(descriptionColIndex), cursor.getString(dateColIndex)));
            }
            cursor.close();
        }
        sendBroadcast(newsList, link);
    }

    private int getChannelId(final String link)
    {
        final String selection = ChannelsContract.COLUMN_NAME_LINK + " = ?";
        final String[] channelSelectionArgs = {link};
        Cursor cursor = null;
        int channelId = -1;
        try
        {
            cursor = database.query(ChannelsContract.TABLE_NAME, null, selection, channelSelectionArgs, null, null, null);
            cursor.moveToNext();
            channelId = cursor.getInt(cursor.getColumnIndex(ChannelsContract._ID));
        }
        catch (SQLiteException e)
        {
            logger.severe("Cant find channel with link = " + link);
        }
        if (null != cursor)
        {
            cursor.close();
        }

        return channelId;
    }

    private void sendBroadcast(final ArrayList<FeedItem> newsList, final String link)
    {
        final Intent intent = new Intent(Constants.LOAD_FROM_DATABASE_MESSAGE);
        intent.putExtra(Constants.LINK_TO_CHECK, link);
        intent.putParcelableArrayListExtra(Constants.LOAD_FROM_DATABASE_MESSAGE, newsList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
