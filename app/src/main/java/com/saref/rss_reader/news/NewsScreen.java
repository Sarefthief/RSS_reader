package com.saref.rss_reader.news;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ListView;
import com.saref.rss_reader.R;

import java.util.ArrayList;

public final class NewsScreen
{
    private Activity activity;
    private ArrayList<FeedItem> itemList;

    NewsScreen (final Activity activity)
    {
        this.activity = activity;
        findViewComponents();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            itemList = intent.getParcelableArrayListExtra(activity.getString(R.string.LIST_NAME));
            showNews();
        }
    };

    private void findViewComponents()
    {
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, new IntentFilter(activity.getString(R.string.SEND_ITEM_LIST_MESSAGE)));
        activity.startService(ParserService.getParserServiceIntent(activity));
    }

    private void showNews()
    {
        NewsAdapter adapter = new NewsAdapter(activity, itemList);
        ListView listView = activity.findViewById(R.id.newsList);
        listView.setAdapter(adapter);
    }

}
