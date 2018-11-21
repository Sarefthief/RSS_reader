package com.saref.rss_reader.channels;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ListView;

import com.saref.rss_reader.LifeCycleInterface;
import com.saref.rss_reader.R;
import com.saref.rss_reader.news.FeedItem;
import com.saref.rss_reader.news.NewsScreenActivity;

import java.util.ArrayList;

final class ChannelsScreen implements LifeCycleInterface
{
    private Activity activity;
    private ChannelsAdapter adapter;
    private ArrayList<Channel> channelsList = new ArrayList<>();

    ChannelsScreen(final Activity activity)
    {
        this.activity = activity;
        showChannels();
    }

    private void showChannels()
    {
        adapter = new ChannelsAdapter(activity, channelsList);
        final ListView channelListView = activity.findViewById(R.id.channelsList);
        channelListView.setAdapter(adapter);
        if (0 == channelsList.size()){
            channelListView.setEmptyView(activity.findViewById(R.id.emptyChannelsList));
        }
    }

    public void addChannel(Channel channel)
    {
        adapter.add(channel);
        showChannels();
    }
    @Override
    public void onResume()
    {
        if(adapter != null){
            adapter.changeClickState();
        }
    }

    @Override
    public void onPause()
    {
    }
}
