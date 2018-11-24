package com.saref.rss_reader.database;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.content.LocalBroadcastManager;

import com.saref.rss_reader.news.NewsActivity;

public class DeleteChannelService extends IntentService
{
    public final static String DELETE_CHANNEL_EXTRA = "DELETE_CHANNEL_EXTRA";
    public final static String DELETION_SUCCESSFUL = "DELETION_SUCCESSFUL";

    private SQLiteDatabase database;
    private RssReaderDbHelper dbHelper;

    public DeleteChannelService()
    {
        super("DeleteChannelService");
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

    public static Intent getDeleteChannelServiceIntent(final Activity activity, final String link)
    {
        final Intent intent = new Intent(activity, DeleteChannelService.class);
        intent.putExtra(DELETE_CHANNEL_EXTRA, link);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (null != intent.getExtras())
        {
            final String selection = ChannelsContract.COLUMN_NAME_LINK + " = ?";
            final String[] selectionArgs = {intent.getStringExtra(DELETE_CHANNEL_EXTRA)};

            try
            {
                database.delete(ChannelsContract.TABLE_NAME, selection, selectionArgs);
                sendBroadcast();
            }
            catch (SQLiteException e)
            {

            }
        }
    }

    private void sendBroadcast()
    {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(NewsActivity.ADD_NEWS_FROM_PARSER_MESSAGE));
    }
}
