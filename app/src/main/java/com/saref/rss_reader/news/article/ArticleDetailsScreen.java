package com.saref.rss_reader.news.article;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.saref.rss_reader.LifeCycleInterface;
import com.saref.rss_reader.R;
import com.saref.rss_reader.news.FeedItem;

final class ArticleDetailsScreen implements LifeCycleInterface
{
    private final Activity activity;
    private boolean isClicked = false;

    ArticleDetailsScreen(final Activity activity, final FeedItem feedItem)
    {
        this.activity = activity;
        showArticle(feedItem);
    }

    private void showArticle(final FeedItem feedItem)
    {
        final TextView title = activity.findViewById(R.id.articleTitle);
        final WebView description = activity.findViewById(R.id.articleDescription);
        final Button linkButton = activity.findViewById(R.id.articleLink);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            description.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else
        {
            description.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        description.loadData(feedItem.getDescription(), "text/html; charset=utf-8", "utf-8");
        title.setText(feedItem.getTitle());
        linkButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!isClicked)
                {
                    isClicked = true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(feedItem.getLink()));
                    activity.startActivity(browserIntent);
                }
            }
        });
    }

    @Override
    public void onResume()
    {
        isClicked = false;
    }

    @Override
    public void onPause()
    {
    }
}
