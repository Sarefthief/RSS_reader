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

    private SQLiteDatabase database;

    public DeleteChannelService()
    {
        super("DeleteChannelService");
    }

    public static Intent getDeleteChannelServiceIntent(final Activity activity, final String link)
    {
        final Intent intent = new Intent(activity, DeleteChannelService.class);
        intent.putExtra(DELETE_CHANNEL_EXTRA, link);
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
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(NewsActivity.CHANNEL_IS_DELETED));
    }
}
