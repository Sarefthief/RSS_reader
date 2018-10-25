package com.saref.rss_reader.news;

import android.app.Service;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchFeedThread implements Runnable {

    private final Service service;

    FetchFeedThread (final Service service)
    {
        this.service = service;
    }

    @Override
    public void run() {
        final String URL_LINK = "https://lenta.ru/rss";
        final String BROADCAST_STRING = "test";
        final String REQUEST_METHOD = "GET";

        final RssFeedParser parser = new RssFeedParser();

        try{
            final URL url = new URL(URL_LINK);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(REQUEST_METHOD);
            conn.setDoInput(true);
            conn.connect();
            final InputStream inputStream = conn.getInputStream();
            final List itemList = parser.parse(inputStream);
            conn.disconnect();

            int itemListSize = itemList.size();
            final Intent intent = new Intent(BROADCAST_STRING);
            intent.putExtra("listSize", itemListSize);
            LocalBroadcastManager.getInstance(service).sendBroadcast(intent);
        } catch (IOException e){

        } catch (XmlPullParserException e){

        }
    }
}
