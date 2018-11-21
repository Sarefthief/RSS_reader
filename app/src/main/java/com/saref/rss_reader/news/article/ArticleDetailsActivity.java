package com.saref.rss_reader.news.article;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.saref.rss_reader.R;
import com.saref.rss_reader.news.FeedItem;

import java.io.BufferedReader;

public final class ArticleDetailsActivity extends AppCompatActivity
{
    private final static String FEED_ITEM_EXTRA = "FEED_ITEM_EXTRA";
    private ArticleDetailsScreen articleDetailsScreen;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        FeedItem feedItem = getIntent().getParcelableExtra(FEED_ITEM_EXTRA);
        articleDetailsScreen = new ArticleDetailsScreen(this, feedItem);
    }

    public static Intent getArticleDetailsActivityIntent(final Activity activity, final FeedItem feedItem)
    {
        Intent intent = new Intent(activity, ArticleDetailsActivity.class);
        intent.putExtra(FEED_ITEM_EXTRA, feedItem);
        return intent;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        articleDetailsScreen.onResume();
    }
}
