package com.saref.rss_reader.channels.parser;

import android.app.Activity;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.content.LocalBroadcastManager;

import com.saref.rss_reader.exceptions.ChannelAlreadyExistException;
import com.saref.rss_reader.ConnectionEstablishment;
import com.saref.rss_reader.R;
import com.saref.rss_reader.exceptions.WrongXmlTypeException;
import com.saref.rss_reader.channels.Channel;
import com.saref.rss_reader.channels.ChannelsActivity;
import com.saref.rss_reader.channels.add.AddChannelActivity;
import com.saref.rss_reader.database.ChannelsContract;
import com.saref.rss_reader.database.RssReaderDbHelper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class CheckChannelService extends IntentService
{
    public static final String CHECK_CHANNEL_EXTRA_URL = "extra url";
    private SQLiteDatabase database;
    private RssReaderDbHelper dbHelper;

    public CheckChannelService()
    {
        super("CheckChannelService");
    }

    public static Intent getCheckChannelServiceIntent(final Activity activity, final Channel channel)
    {
        final Intent intent = new Intent(activity, CheckChannelService.class);
        intent.putExtra(CHECK_CHANNEL_EXTRA_URL, channel);
        return intent;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        dbHelper = new RssReaderDbHelper(this);
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        database.close();
        dbHelper.close();
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent.hasExtra(CHECK_CHANNEL_EXTRA_URL))
        {
            final Channel channel = intent.getParcelableExtra(CHECK_CHANNEL_EXTRA_URL);
            final ChannelParser channelParser = new ChannelParser();
            try
            {
                checkChannelExistence(channel);
                final HttpURLConnection connection = ConnectionEstablishment.openConnection(new URL(channel.getLink()));
                final InputStream inputStream = connection.getInputStream();
                try
                {
                    channel.setTitle(channelParser.getChannelTitle(inputStream));
                }
                finally
                {
                    inputStream.close();
                    connection.disconnect();
                    saveChannelToDatabase(channel);
                }
            }
            catch (XmlPullParserException e)
            {

            }
            catch (IOException e)
            {

            }
            catch (WrongXmlTypeException e)
            {
                //TODO Логирование
                sendErrorBroadcast(getString(R.string.notRssOrAtomXmlError));
            }
            catch (ChannelAlreadyExistException e)
            {
                //TODO Логирование
                sendErrorBroadcast(getString(R.string.channelIsAlreadyExistsError));
            }
        }
    }

    private void saveChannelToDatabase(final Channel channel)
    {
        ContentValues values = new ContentValues();
        values.put(ChannelsContract.COLUMN_NAME_TITLE, channel.getTitle());
        values.put(ChannelsContract.COLUMN_NAME_LINK, channel.getLink());
        try
        {
            database.insertOrThrow(ChannelsContract.TABLE_NAME, null, values);
            sendBroadcast(channel);
        }
        catch (SQLiteException e)
        {
            //TODO Логирование
            sendErrorBroadcast(getString(R.string.channelWriteSqlError));
        }
    }

    private void checkChannelExistence(Channel channel) throws ChannelAlreadyExistException
    {
        String selection = ChannelsContract.COLUMN_NAME_LINK + " = ?";
        String[] selectionArgs = {channel.getLink()};

        Cursor cursor = database.query(ChannelsContract.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        int rowsCount = cursor.getCount();
        cursor.close();
        if (rowsCount != 0)
        {
            throw new ChannelAlreadyExistException();
        }
    }

    private void sendBroadcast(final Channel channel)
    {
        final Intent intent = new Intent(ChannelsActivity.ADD_CHANNEL_MESSAGE);
        intent.putExtra(ChannelsActivity.ADD_CHANNEL_MESSAGE, channel);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendErrorBroadcast(final String errorText)
    {
        final Intent intent = new Intent(AddChannelActivity.ADD_CHANNEL_ERROR);
        intent.putExtra(AddChannelActivity.ADD_CHANNEL_ERROR, errorText);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
