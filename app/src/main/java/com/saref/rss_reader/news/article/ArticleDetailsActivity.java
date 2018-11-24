package com.saref.rss_reader.news.article;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.saref.rss_reader.R;
import com.saref.rss_reader.news.FeedItem;
import com.saref.rss_reader.news.NewsActivity;

public final class ArticleDetailsActivity extends AppCompatActivity
{
    private final static String FEED_ITEM_EXTRA = "FEED_ITEM_EXTRA";
    private ArticleDetailsScreen articleDetailsScreen;
    private String channelLink;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        Intent intent = getIntent();
        final FeedItem feedItem = intent.getParcelableExtra(FEED_ITEM_EXTRA);
        channelLink = intent.getStringExtra(NewsActivity.LINK_TO_CHECK);
        articleDetailsScreen = new ArticleDetailsScreen(this, feedItem);
    }

    public static Intent getArticleDetailsActivityIntent(final Activity activity, final FeedItem feedItem, final String channelLink)
    {
        final Intent intent = new Intent(activity, ArticleDetailsActivity.class);
        intent.putExtra(FEED_ITEM_EXTRA, feedItem);
        intent.putExtra(NewsActivity.LINK_TO_CHECK, channelLink);
        return intent;
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
        startActivity(NewsActivity.getNewsActivityIntent(this, channelLink));
    }
}
