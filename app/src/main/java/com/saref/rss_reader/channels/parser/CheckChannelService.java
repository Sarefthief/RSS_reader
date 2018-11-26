package com.saref.rss_reader.channels.parser;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.content.LocalBroadcastManager;

import com.saref.rss_reader.ConnectionEstablishment;
import com.saref.rss_reader.Constants;
import com.saref.rss_reader.R;
import com.saref.rss_reader.channels.Channel;
import com.saref.rss_reader.database.ChannelsContract;
import com.saref.rss_reader.database.DatabaseManager;
import com.saref.rss_reader.exceptions.ChannelAlreadyExistException;
import com.saref.rss_reader.exceptions.WrongXmlTypeException;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public final class CheckChannelService extends IntentService
{
    private SQLiteDatabase database;
    private static final Logger logger = Logger.getLogger(CheckChannelService.class.getName());

    public CheckChannelService()
    {
        super("CheckChannelService");
    }

    public static Intent getCheckChannelServiceIntent(final Context context, final Channel channel)
    {
        final Intent intent = new Intent(context, CheckChannelService.class);
        intent.putExtra(Constants.CHECK_CHANNEL_EXTRA_URL, channel);
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
        if (intent.hasExtra(Constants.CHECK_CHANNEL_EXTRA_URL))
        {
            final Channel channel = intent.getParcelableExtra(Constants.CHECK_CHANNEL_EXTRA_URL);
            final ChannelParser channelParser = new ChannelParser();
            try
            {
                checkChannelExistence(channel);
                final HttpURLConnection connection = ConnectionEstablishment.openConnection(new URL(channel.getLink()));
                final InputStream inputStream = connection.getInputStream();
                try
                {
                    channel.setTitle(channelParser.getChannelTitle(inputStream));
                    saveChannelToDatabase(channel);
                }
                finally
                {
                    inputStream.close();
                    connection.disconnect();
                }
            }
            catch (XmlPullParserException e)
            {
                logger.severe("XmlPullParserException occurred");
                sendErrorBroadcast(getString(R.string.connectionError));
            }
            catch (IOException e)
            {
                logger.severe("Connection error");
                sendErrorBroadcast(getString(R.string.connectionError));
            }
            catch (WrongXmlTypeException e)
            {
                logger.severe(channel.getLink() + " contains wrong type of xml");
                sendErrorBroadcast(getString(R.string.notRssOrAtomXmlError));
            }
            catch (ChannelAlreadyExistException e)
            {
                logger.severe(channel.getLink() + " is already exist");
                sendErrorBroadcast(getString(R.string.channelIsAlreadyExistsError));
            }
        }
    }

    private void saveChannelToDatabase(final Channel channel)
    {
        final ContentValues values = new ContentValues();
        values.put(ChannelsContract.COLUMN_NAME_TITLE, channel.getTitle());
        values.put(ChannelsContract.COLUMN_NAME_LINK, channel.getLink());
        try
        {
            database.insertOrThrow(ChannelsContract.TABLE_NAME, null, values);
            sendBroadcast();
        }
        catch (SQLiteException e)
        {
            logger.severe(channel.getLink() + " throwed SqliteException");
            sendErrorBroadcast("");
        }
    }

    private void checkChannelExistence(final Channel channel) throws ChannelAlreadyExistException
    {
        final String selection = ChannelsContract.COLUMN_NAME_LINK + " = ?";
        final String[] selectionArgs = {channel.getLink()};

        try
        {
            final Cursor cursor = database.query(ChannelsContract.TABLE_NAME, null, selection, selectionArgs, null, null, null);
            int rowsCount = cursor.getCount();
            cursor.close();
            if (rowsCount != 0)
            {
                throw new ChannelAlreadyExistException();
            }
        }
        catch (SQLiteException e)
        {
            logger.severe(channel.getLink() + " throwed SqliteException");
            sendErrorBroadcast("");
        }
    }

    private void sendBroadcast()
    {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.ADD_CHANNEL_MESSAGE));
    }

    private void sendErrorBroadcast(final String errorText)
    {
        final Intent intent = new Intent(Constants.ADD_CHANNEL_ERROR);
        intent.putExtra(Constants.ADD_CHANNEL_ERROR, errorText);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
