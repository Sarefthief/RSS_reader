package com.saref.rss_reader.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.saref.rss_reader.R;

public final class NewsScreenActivity extends AppCompatActivity
{
    NewsScreen newsScreen;
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_screen);
        newsScreen = new NewsScreen(this);
        if (null != savedInstanceState){
            newsScreen.restoreListViewState(savedInstanceState.getParcelable(NewsScreen.LIST_VIEW_STATE));
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable(NewsScreen.LIST_VIEW_STATE, newsScreen.onSaveInstanceState());
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
