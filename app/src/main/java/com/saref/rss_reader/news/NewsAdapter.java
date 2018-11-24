package com.saref.rss_reader.news;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.saref.rss_reader.R;
import com.saref.rss_reader.channels.Channel;
import com.saref.rss_reader.news.article.ArticleDetailsActivity;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<FeedItem>
{
    private ArrayList<FeedItem> itemList;
    private final Activity activity;
    private final Channel channel;

    NewsAdapter(final Activity activity, final ArrayList<FeedItem> news, final Channel channel)
    {
        super(activity, 0, news);
        itemList = news;
        this.activity = activity;
        this.channel = channel;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_news, parent, false);
        }
        final TextView newsTitle = convertView.findViewById(R.id.newsTitle);
        final TextView newsDescription = convertView.findViewById(R.id.newsDescription);
        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FeedItem selectedElement = itemList.get(position);
                activity.startActivity(ArticleDetailsActivity.getArticleDetailsActivityIntent(activity, selectedElement, channel));
            }
        });

        FeedItem feedItem = itemList.get(position);
        newsTitle.setText(feedItem.getTitle());
        newsDescription.setText(feedItem.getDescription());

        return convertView;
    }

}
