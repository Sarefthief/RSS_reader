package com.saref.rss_reader.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.saref.rss_reader.R;
import com.saref.rss_reader.channels.ChannelsActivity;

public final class NewsActivity extends AppCompatActivity
{
    public static final String ADD_NEWS_FROM_PARSER_MESSAGE = "ADD_NEWS_FROM_PARSER_MESSAGE";
    public static final String LOAD_FROM_DATABASE_MESSAGE = "LOAD_FROM_DATABASE_MESSAGE";
    public static final String LINK_TO_CHECK = "LINK_TO_CHECK";
    public static final String LIST_VIEW_STATE = "LIST_VIEW_STATE";
    public static final String CHANNEL_LINK_EXTRA = "CHANNEL_LINK_EXTRA";

    private NewsScreen newsScreen;

    public static Intent getNewsActivityIntent(final Activity activity, final String url)
    {
        Intent intent = new Intent(activity, NewsActivity.class);
        intent.putExtra(CHANNEL_LINK_EXTRA, url);
        return intent;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_screen);
        if (null != getIntent().getExtras())
        {
            newsScreen = new NewsScreen(this, getIntent().getStringExtra(CHANNEL_LINK_EXTRA));
        }
        if (null != savedInstanceState)
        {
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
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, ChannelsActivity.class));
    }

    @Override
    public void onSaveInstanceState(final Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_VIEW_STATE, newsScreen.onSaveInstanceState());
    }
}
