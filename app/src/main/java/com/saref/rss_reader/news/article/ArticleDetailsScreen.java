package com.saref.rss_reader.news.article;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
        description.getSettings();
        description.setBackgroundColor(Color.TRANSPARENT);
        final Button linkButton = activity.findViewById(R.id.articleLink);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            description.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else
        {
            description.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        final String htmlEnd = "</body>";
        final String htmlStart = "<head><style>img{display: inline;height: auto;max-width: 100%; margin-top:10px; margin-bottom:10px}</style></head><body style='text-align:justify'>";
        description.loadDataWithBaseURL(null, htmlStart + feedItem.getDescription() + htmlEnd, "text/html", "UTF-8", null);
        title.setText(feedItem.getTitle());
        linkButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!isClicked)
                {
                    isClicked = true;
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(feedItem.getLink())));
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
