package com.saref.rss_reader.news;

import android.app.Service;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchFeedThread implements Runnable
{
    private final Service service;
    private ArrayList itemList;

    FetchFeedThread (final Service service)
    {
        this.service = service;
    }

    @Override
    public void run()
    {
        try {
            parseFeed(openConnection());
        } catch (IOException e){

        }
    }

    private HttpURLConnection openConnection() throws IOException
    {
        final String URL_LINK = "https://4pda.ru/feed/";
        final String REQUEST_METHOD = "GET";
        final int READ_TIMEOUT_IN_MILLISECONDS = 10000;
        final int CONNECT_TIMEOUT_IN_MILLISECONDS = 15000;

        final URL url = new URL(URL_LINK);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(READ_TIMEOUT_IN_MILLISECONDS);
        connection.setConnectTimeout(CONNECT_TIMEOUT_IN_MILLISECONDS);
        connection.setRequestMethod(REQUEST_METHOD);
        connection.setDoInput(true);
        connection.connect();

        return connection;
    }

    private void parseFeed(final HttpURLConnection connection)
    {
        try{
            final RssFeedParser parser = new RssFeedParser();
            final InputStream inputStream = connection.getInputStream();
            try{
                itemList = parser.parse(inputStream);
            } finally {
                sendBroadcasts();
                inputStream.close();
                connection.disconnect();
            }
        } catch (IOException e){

        } catch (XmlPullParserException e){

        }
    }

    private void sendBroadcasts()
    {
        Intent intent = new Intent(NewsScreen.SEND_ITEM_LIST_MESSAGE);
        intent.putExtra(NewsScreen.LIST_NAME, itemList);
        LocalBroadcastManager.getInstance(service).sendBroadcast(intent);

        intent = new Intent(NewsScreen.STOP_SERVICE_MESSAGE);
        LocalBroadcastManager.getInstance(service).sendBroadcast(intent);
    }
}
