package com.saref.rss_reader.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.saref.rss_reader.R;

public final class NewsScreenActivity extends AppCompatActivity
{
    NewsScreen newsScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_screen);
        newsScreen = new NewsScreen(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        newsScreen.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        newsScreen.onPause();
    }
}
