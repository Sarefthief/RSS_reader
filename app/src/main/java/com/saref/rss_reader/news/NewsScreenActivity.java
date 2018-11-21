package com.saref.rss_reader.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.saref.rss_reader.R;

import java.net.URL;

public final class NewsScreenActivity extends AppCompatActivity
{
    public static final String SEND_ITEM_LIST_MESSAGE = "SEND_ITEM_LIST_MESSAGE";
    public static final String LIST_NAME = "LIST_NAME";
    public static final String LIST_VIEW_STATE = "LIST_VIEW_STATE";
    public static final String NEWS_SCREEN_ACTIVITY_EXTRA = "NEWS_SCREEN_ACTIVITY_EXTRA";

    private NewsScreen newsScreen;

    public static Intent getNewsScreenActivityIntent(final Activity activity, final URL url)
    {
        Intent intent = new Intent(activity, NewsScreenActivity.class);
        intent.putExtra(NEWS_SCREEN_ACTIVITY_EXTRA, url);
        return intent;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_screen);
        if(null != getIntent().getExtras()){
            newsScreen = new NewsScreen(this,(URL) getIntent().getExtras().get(NEWS_SCREEN_ACTIVITY_EXTRA));
        }

        if (null != savedInstanceState){
            newsScreen.restoreListViewState(savedInstanceState.getParcelable(LIST_VIEW_STATE));
        }
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

    @Override
    public void onSaveInstanceState(final Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_VIEW_STATE, newsScreen.onSaveInstanceState());
    }
}
