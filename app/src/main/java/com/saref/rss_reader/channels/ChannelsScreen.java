package com.saref.rss_reader.channels;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ListView;

import com.saref.rss_reader.LifeCycleInterface;
import com.saref.rss_reader.R;

import java.util.ArrayList;

final class ChannelsScreen implements LifeCycleInterface
{
    private final Activity activity;
    private ArrayList<Channel> channelsList = new ArrayList<>();

    ChannelsScreen(final Activity activity)
    {
        this.activity = activity;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(final Context context, final Intent intent)
        {
            if (ChannelsActivity.LOAD_CHANNELS_LIST_MESSAGE.equals(intent.getAction()))
            {
                channelsList = intent.getParcelableArrayListExtra(ChannelsActivity.LOAD_CHANNELS_LIST_MESSAGE);
                showChannels();
            }
        }
    };


    private void showChannels()
    {
        final ChannelsAdapter adapter = new ChannelsAdapter(activity, channelsList);
        final ListView channelListView = activity.findViewById(R.id.channelsList);
        channelListView.setAdapter(adapter);
        if (0 == channelsList.size())
        {
            channelListView.setEmptyView(activity.findViewById(R.id.emptyChannelsList));
        }
    }

    @Override
    public void onResume()
    {
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, new IntentFilter(ChannelsActivity.LOAD_CHANNELS_LIST_MESSAGE));
        activity.startService(LoadChannelsFromDatabaseService.getLoadChannelsServiceIntent(activity));
    }

    @Override
    public void onPause()
    {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(receiver);
    }
}
