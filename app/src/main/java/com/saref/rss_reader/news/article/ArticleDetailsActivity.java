package com.saref.rss_reader.news.article;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.saref.rss_reader.R;
import com.saref.rss_reader.channels.Channel;
import com.saref.rss_reader.channels.ChannelsActivity;
import com.saref.rss_reader.news.FeedItem;
import com.saref.rss_reader.news.NewsActivity;

public final class ArticleDetailsActivity extends AppCompatActivity
{
    private final static String FEED_ITEM_EXTRA = "FEED_ITEM_EXTRA";
    private ArticleDetailsScreen articleDetailsScreen;
    private Channel channel;

    public static Intent getArticleDetailsActivityIntent(final Context context, final FeedItem feedItem, final Channel channel)
    {
        final Intent intent = new Intent(context, ArticleDetailsActivity.class);
        intent.putExtra(FEED_ITEM_EXTRA, feedItem);
        intent.putExtra(NewsActivity.CHANNEL_TO_CHECK, channel);
        return intent;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.articleDetailsActionBarTitle);
        Intent intent = getIntent();
        final FeedItem feedItem = intent.getParcelableExtra(FEED_ITEM_EXTRA);
        channel = intent.getParcelableExtra(NewsActivity.CHANNEL_TO_CHECK);
        articleDetailsScreen = new ArticleDetailsScreen(this, feedItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            startActivity(NewsActivity.getNewsActivityIntent(this, channel));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        articleDetailsScreen.onResume();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(NewsActivity.getNewsActivityIntent(this, channel));
    }
}
