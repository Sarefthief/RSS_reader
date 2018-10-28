package com.saref.rss_reader.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.saref.rss_reader.R;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<FeedItem>
{
    private ArrayList<FeedItem> itemList;

    public NewsAdapter(Context context, ArrayList<FeedItem> news)
    {
        super(context, 0, news);
        itemList = news;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FeedItem feedItem = itemList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_news, parent, false);
        }
        final TextView newsTitle = convertView.findViewById(R.id.newsTitle);
        final TextView newsDescription = convertView.findViewById(R.id.newsDescription);

        try{
            newsTitle.setText(feedItem.getTitle());
            newsDescription.setText(feedItem.getDescription());
        } catch (NullPointerException e){

        }

        return convertView;
    }
}
