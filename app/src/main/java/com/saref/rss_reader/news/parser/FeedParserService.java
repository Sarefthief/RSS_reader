package com.saref.rss_reader.news.parser;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.saref.rss_reader.ConnectionEstablishment;
import com.saref.rss_reader.news.NewsScreenActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public final class FeedParserService extends IntentService
{
    public FeedParserService()
    {
        super("FeedParserService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if(null != intent.getExtras()){
            parseFeed((URL) intent.getExtras().get(NewsScreenActivity.NEWS_SCREEN_ACTIVITY_EXTRA));
        }
    }

    public static Intent getParserServiceIntent(final Activity activity, URL url)
    {
        Intent intent = new Intent(activity, FeedParserService.class);
        intent.putExtra(NewsScreenActivity.NEWS_SCREEN_ACTIVITY_EXTRA, url);
        return intent;
    }

    private void parseFeed(URL url)
    {
        try {
            ArrayList itemList = new ArrayList();
            final RssFeedParser parser = new RssFeedParser();
            HttpURLConnection connection = ConnectionEstablishment.openConnection(url);
            final InputStream inputStream = connection.getInputStream();
            try {
                itemList = parser.parse(inputStream);
            } finally {
                sendBroadcast(itemList);
                inputStream.close();
                connection.disconnect();
            }
        } catch (IOException e) {

        } catch (XmlPullParserException e) {

        }
    }

    private void sendBroadcast(ArrayList itemList)
    {
        Intent intent = new Intent(NewsScreenActivity.SEND_ITEM_LIST_MESSAGE);
        intent.putExtra(NewsScreenActivity.LIST_NAME, itemList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
