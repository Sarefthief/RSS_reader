package com.saref.rss_reader.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.saref.rss_reader.R;
import com.saref.rss_reader.channels.Channel;
import com.saref.rss_reader.channels.ChannelsActivity;
import com.saref.rss_reader.channels.add.AddChannelActivity;
import com.saref.rss_reader.database.DeleteChannelService;

public final class NewsActivity extends AppCompatActivity
{
    public static final String ADD_NEWS_FROM_PARSER_MESSAGE = "ADD_NEWS_FROM_PARSER_MESSAGE";
    public static final String LOAD_FROM_DATABASE_MESSAGE = "LOAD_FROM_DATABASE_MESSAGE";
    public static final String CHANNEL_TO_CHECK = "CHANNEL_TO_CHECK";
    public static final String LINK_TO_CHECK = "LINK_TO_CHECK";
    public static final String LIST_VIEW_STATE = "LIST_VIEW_STATE";
    public static final String CHANNEL_EXTRA = "CHANNEL_EXTRA";
    public static final String CHANNEL_LINK_EXTRA = "CHANNEL_LINK_EXTRA";
    public static final String CHANNEL_IS_DELETED = "CHANNEL_IS_DELETED";

    private NewsScreen newsScreen;

    public static Intent getNewsActivityIntent(final Activity activity, final Channel channel)
    {
        Intent intent = new Intent(activity, NewsActivity.class);
        intent.putExtra(CHANNEL_EXTRA, channel);
        return intent;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_screen);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (null != getIntent().getExtras())
        {
            Channel channel = getIntent().getParcelableExtra(CHANNEL_EXTRA);
            setTitle(channel.getTitle());
            newsScreen = new NewsScreen(this, channel);
        }
        if (null != savedInstanceState)
        {
            newsScreen.restoreListViewState(savedInstanceState.getParcelable(LIST_VIEW_STATE));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_news_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            startActivity(ChannelsActivity.getChannelsActivityIntent(this));
        }
        if (item.getItemId() == R.id.deleteChannelMenuButton)
        {
            Channel channel = getIntent().getParcelableExtra(CHANNEL_EXTRA);
            startService(DeleteChannelService.getDeleteChannelServiceIntent(this, channel.getLink()));
        }
        return super.onOptionsItemSelected(item);
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
