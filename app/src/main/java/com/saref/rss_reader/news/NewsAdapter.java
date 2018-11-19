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
import com.saref.rss_reader.news.article.ArticleDetailsActivity;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<FeedItem>
{
    private ArrayList<FeedItem> itemList;
    private Activity activity;
    private boolean isClicked = false;

    NewsAdapter(Activity activity, ArrayList<FeedItem> news)
    {
        super(activity, 0, news);
        itemList = news;
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_news, parent, false);
        }
        final TextView newsTitle = convertView.findViewById(R.id.newsTitle);
        final TextView newsDescription = convertView.findViewById(R.id.newsDescription);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isClicked) {
                    FeedItem selectedElement = itemList.get(position);
                    isClicked = true;
                    activity.startActivity(ArticleDetailsActivity.getArticleDetailsActivityIntent(activity,selectedElement));
                }
            }
        });

        FeedItem feedItem = itemList.get(position);
        try{
            newsTitle.setText(feedItem.getTitle());
            newsDescription.setText(feedItem.getDescription());
        } catch (NullPointerException e){

        }

        return convertView;
    }

    public void changeClickState()
    {
        isClicked = false;
    }
}
