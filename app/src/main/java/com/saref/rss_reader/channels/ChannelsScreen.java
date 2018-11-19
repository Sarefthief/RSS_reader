package com.saref.rss_reader.channels;

import android.app.Activity;
import android.widget.ListView;

import com.saref.rss_reader.R;

final class ChannelsScreen
{
    private Activity activity;

    ChannelsScreen(final Activity activity)
    {
        this.activity = activity;
        showElements();
    }

    private void showElements()
    {
        ListView channelListView = activity.findViewById(R.id.channelsList);
        channelListView.setEmptyView(activity.findViewById(R.id.emptyChannelsList));
    }
}
