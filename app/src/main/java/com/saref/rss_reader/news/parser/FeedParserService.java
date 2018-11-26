package com.saref.rss_reader.news.parser;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.saref.rss_reader.ConnectionEstablishment;
import com.saref.rss_reader.Constants;
import com.saref.rss_reader.R;
import com.saref.rss_reader.database.ChannelsContract;
import com.saref.rss_reader.database.DatabaseManager;
import com.saref.rss_reader.database.NewsContract;
import com.saref.rss_reader.exceptions.WrongXmlTypeException;
import com.saref.rss_reader.news.FeedItem;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

public final class FeedParserService extends IntentService
{
    private SQLiteDatabase database;
    private static final Logger logger = Logger.getLogger(FeedParserService.class.getName());

    public FeedParserService()
    {
        super("FeedParserService");
    }

    public static Intent getParserServiceIntent(final Context context, final String url)
    {
        Intent intent = new Intent(context, FeedParserService.class);
        intent.putExtra(Constants.CHANNEL_LINK_EXTRA, url);
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
            parseFeed(intent.getStringExtra(Constants.CHANNEL_LINK_EXTRA));
        }
    }

    private void parseFeed(final String link)
    {
        ArrayList<FeedItem> itemList;
        try
        {
            final FeedParser parser = new FeedParser();
            final URL url = new URL(link);
            final HttpURLConnection connection = ConnectionEstablishment.openConnection(url);
            final InputStream inputStream = connection.getInputStream();
            try
            {
                itemList = parser.parse(inputStream);
                checkNewsCountToSave(itemList, link);
            }
            finally
            {
                inputStream.close();
                connection.disconnect();
            }
        }
        catch (IOException e)
        {
            logger.severe("Cant establish connection with " + link);
            sendErrorBroadcast(getString(R.string.connectionError));
        }
        catch (XmlPullParserException e)
        {
            logger.severe("XmlPullParser exception occurred");
            sendErrorBroadcast(getString(R.string.connectionError));
        }
        catch (WrongXmlTypeException e)
        {
            logger.severe("Xml is wrong type");
            sendErrorBroadcast(getString(R.string.notRssOrAtomXmlError));
        }
    }

    private void checkNewsCountToSave(ArrayList<FeedItem> itemList, final String link)
    {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final int maxRowsCount = Integer.parseInt(sharedPreferences.getString(getString(R.string.maxNewsCountPreferenceKey),getString(R.string.maxNewsCountPreferenceDefault)));
        int newsToWriteCount = itemList.size();
        if (newsToWriteCount > maxRowsCount)
        {
            newsToWriteCount = maxRowsCount;
        }

        final int channelId = getChannelId(link);
        final Cursor cursor = getAllNewsOfChannelCursor(channelId);
        cursor.moveToNext();
        int rowsCount = 0;
        if (0 != cursor.getCount())
        {
            rowsCount = cursor.getCount();
            final int linkColIndex = cursor.getColumnIndex(NewsContract.COLUMN_NAME_LINK);
            String linkToCheck = cursor.getString(linkColIndex);

            for (int i = 0; i <= newsToWriteCount - 1; i++)
            {
                if (linkToCheck.equals(itemList.get(i).getLink()))
                {
                    newsToWriteCount = i;
                    break;
                }
            }
        }
        cursor.close();

        if (newsToWriteCount != itemList.size())
        {
            itemList = new ArrayList<>(itemList.subList(0, newsToWriteCount));
        }
        if (0 != itemList.size())
        {
            saveNewsToDatabase(itemList, rowsCount, channelId);
        }

        sendBroadcast(itemList, link);
    }

    private void saveNewsToDatabase(final ArrayList<FeedItem> itemList, final int rowsInDatabase, final int channelId)
    {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final int maxRowsCount = Integer.parseInt(sharedPreferences.getString(getString(R.string.maxNewsCountPreferenceKey),getString(R.string.maxNewsCountPreferenceDefault)));
        ContentValues values;
        int sumOfRows = rowsInDatabase + itemList.size();
        if (sumOfRows > maxRowsCount)
        {
            deleteExtraRows(sumOfRows - maxRowsCount, channelId);
        }
        for (int i = itemList.size() - 1; i >= 0; i--)
        {
            values = new ContentValues();
            values.put(NewsContract.COLUMN_NAME_TITLE, itemList.get(i).getTitle());
            values.put(NewsContract.COLUMN_NAME_LINK, itemList.get(i).getLink());
            values.put(NewsContract.COLUMN_NAME_DESCRIPTION, itemList.get(i).getDescription());
            values.put(NewsContract.COLUMN_NAME_DATE, itemList.get(i).getDate());
            values.put(NewsContract.COLUMN_NAME_CHANNEL_ID, channelId);
            try
            {
                database.insertOrThrow(NewsContract.TABLE_NAME, null, values);
            }
            catch (SQLiteException e)
            {
                logger.severe("Cant insert row with link = " + itemList.get(i).getLink());
                sendErrorBroadcast("");
            }
        }
    }

    private void deleteExtraRows(final int rowsToDelete, final int channelId)
    {
        final String selection = NewsContract.COLUMN_NAME_CHANNEL_ID + " = ?";
        final String[] newsSelectionArgs = {String.valueOf(channelId)};
        final String selectionToDelete = NewsContract.COLUMN_NAME_CHANNEL_ID + " = ?";

        Cursor cursor = database.query(NewsContract.TABLE_NAME, null, selection, newsSelectionArgs, null, null, null);
        cursor.moveToNext();
        for (int i = rowsToDelete - 1; i > 0; i--)
        {
            int idToDelete = cursor.getInt(cursor.getColumnIndex(NewsContract._ID));
            String[] selectionArgsToDelete = {String.valueOf(idToDelete)};
            database.delete(NewsContract.TABLE_NAME, selectionToDelete, selectionArgsToDelete);
            cursor.moveToNext();
        }
        cursor.close();
    }

    private Cursor getAllNewsOfChannelCursor(final int channelId)
    {
        final String selection = NewsContract.COLUMN_NAME_CHANNEL_ID + " = ?";
        final String[] newsSelectionArgs = {String.valueOf(channelId)};

        return database.query(NewsContract.TABLE_NAME, null, selection, newsSelectionArgs, null, null, NewsContract._ID + " DESC");
    }

    private int getChannelId(final String link)
    {
        final String selection = ChannelsContract.COLUMN_NAME_LINK + " = ?";
        final String[] channelSelectionArgs = {link};
        final Cursor cursor = database.query(ChannelsContract.TABLE_NAME, null, selection, channelSelectionArgs, null, null, null);
        cursor.moveToNext();
        final int channelId = cursor.getInt(cursor.getColumnIndex(ChannelsContract._ID));

        cursor.close();

        return channelId;
    }

    private void sendBroadcast(final ArrayList itemList, final String link)
    {
        final Intent intent = new Intent(Constants.ADD_NEWS_FROM_PARSER_MESSAGE);
        intent.putExtra(Constants.LINK_TO_CHECK, link);
        intent.putExtra(Constants.ADD_NEWS_FROM_PARSER_MESSAGE, itemList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendErrorBroadcast(String errorText)
    {
        final Intent intent = new Intent(Constants.FEED_PARSER_ERROR);
        intent.putExtra(Constants.FEED_PARSER_ERROR, errorText);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
