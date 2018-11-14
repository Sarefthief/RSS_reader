package com.saref.rss_reader.news;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ListView;

import com.saref.rss_reader.LifeCycleInterface;
import com.saref.rss_reader.R;

import java.util.ArrayList;

final class NewsScreen implements LifeCycleInterface
{
    private Parcelable state;
    private ListView listView;
    private Activity activity;
    private ArrayList<FeedItem> itemList;

    static final String STOP_SERVICE_MESSAGE = "stop service";
    static final String SEND_ITEM_LIST_MESSAGE = "send list";
    static final String LIST_NAME = "item list";
    static final String LIST_VIEW_STATE = "list view save state";

    NewsScreen (final Activity activity)
    {
        this.activity = activity;
        listView = activity.findViewById(R.id.newsList);
        startService();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            itemList = intent.getParcelableArrayListExtra(LIST_NAME);
            showNews();
        }
    };

    private void startService()
    {
        activity.startService(ParserService.getParserServiceIntent(activity));
    }

    private void showNews()
    {
        NewsAdapter adapter = new NewsAdapter(activity, itemList);
        listView.setAdapter(adapter);
        if (null != state){
            listView.onRestoreInstanceState(state);
        }
    }

    @Override
    public void onResume()
    {
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, new IntentFilter(SEND_ITEM_LIST_MESSAGE));
    }

    @Override
    public void onPause()
    {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(receiver);
    }

    public Parcelable onSaveInstanceState()
    {
        return listView.onSaveInstanceState();
    }

    public void restoreListViewState(final Parcelable state)
    {
        this.state = state;
    }
}
