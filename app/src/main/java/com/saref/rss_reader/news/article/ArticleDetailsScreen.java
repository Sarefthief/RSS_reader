package com.saref.rss_reader.news.article;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.saref.rss_reader.R;
import com.saref.rss_reader.news.FeedItem;

final class ArticleDetailsScreen
{
    private final Activity activity;

    ArticleDetailsScreen(final Activity activity, final FeedItem feedItem)
    {
        this.activity = activity;
        showArticle(feedItem);
    }

    private void showArticle(final FeedItem feedItem)
    {
        TextView title = activity.findViewById(R.id.articleTitle);
        WebView description = activity.findViewById(R.id.articleDescription);
        Button linkButton = activity.findViewById(R.id.articleLink);

        description.loadData(feedItem.getDescription(),"text/html; charset=utf-8", "utf-8");
        title.setText(feedItem.getTitle());
        linkButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(feedItem.getLink()));
                activity.startActivity(browserIntent);
            }
        });
    }
}
