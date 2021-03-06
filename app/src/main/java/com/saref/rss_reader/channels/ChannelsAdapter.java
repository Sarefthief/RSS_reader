package com.saref.rss_reader.channels;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.saref.rss_reader.R;
import com.saref.rss_reader.news.NewsActivity;

import java.util.ArrayList;

public final class ChannelsAdapter extends ArrayAdapter<Channel>
{
    private ArrayList<Channel> channelsList;
    private Activity activity;

    ChannelsAdapter(final Activity activity, final ArrayList<Channel> channelsList)
    {
        super(activity, 0, channelsList);
        this.channelsList = channelsList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_channels, parent, false);
        }
        final TextView channelTitle = convertView.findViewById(R.id.channelTitle);
        final TextView channelLink = convertView.findViewById(R.id.channelLink);


        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                activity.startActivity(NewsActivity.getNewsActivityIntent(activity, channelsList.get(position)));
            }
        });

        final Channel channel = channelsList.get(position);
        channelTitle.setText(channel.getTitle());
        channelLink.setText(channel.getLink());

        return convertView;
    }

}
