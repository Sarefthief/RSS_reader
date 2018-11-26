package com.saref.rss_reader.news;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
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
        ViewHolder holder = new ViewHolder();
        holder.newsTitle = convertView.findViewById(R.id.newsTitle);
        holder.newsDescription = convertView.findViewById(R.id.newsDescription);
        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FeedItem selectedElement = itemList.get(position);
                activity.startActivity(ArticleDetailsActivity.getArticleDetailsActivityIntent(activity, selectedElement, channel));
            }
        });

        final FeedItem feedItem = itemList.get(position);
        holder.newsTitle.setText(feedItem.getTitle());
        holder.newsDescription.setText(stripHtml(feedItem.getDescription()));

        return convertView;
    }

    private CharSequence stripHtml(String s) {
        return Html.fromHtml(s).toString().replace('\n', (char) 32)
                .replace((char) 160, (char) 32).replace((char) 65532, (char) 32).trim();
    }

    private static class ViewHolder {
        private TextView newsTitle;
        private TextView newsDescription;
    }
}
