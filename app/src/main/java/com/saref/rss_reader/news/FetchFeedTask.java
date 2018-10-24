package com.saref.rss_reader.news;

import android.os.AsyncTask;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchFeedTask extends AsyncTask<Void, Void, Void>
{
    private ArrayList itemList;
    private TextView test;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        RssFeedParser parser = new RssFeedParser();
        String urlLink = "https://dtf.ru/rss/all";
        itemList = new ArrayList<>();

        try{
            URL url = new URL(urlLink);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            itemList = parser.parse(inputStream);
        } catch (IOException e){

        } catch (XmlPullParserException e){

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        test.setText(itemList.size());
    }
}
