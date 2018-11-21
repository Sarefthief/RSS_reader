package com.saref.rss_reader.news;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.saref.rss_reader.LifeCycleInterface;
import com.saref.rss_reader.R;
import com.saref.rss_reader.news.parser.FeedParserService;

import java.net.URL;
import java.util.ArrayList;

final class NewsScreen implements LifeCycleInterface
{
    private Parcelable state;
    private ListView listView;
    private Activity activity;
    private ArrayList<FeedItem> itemList;
    private NewsAdapter adapter;
    private ProgressBar progressBar;

    NewsScreen (final Activity activity,final URL url)
    {
        this.activity = activity;
        listView = activity.findViewById(R.id.newsList);
        progressBar = activity.findViewById(R.id.newsListProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        startService(url);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            itemList = intent.getParcelableArrayListExtra(NewsScreenActivity.LIST_NAME);
            progressBar.setVisibility(View.GONE);
            showNews();
        }
    };

    private void startService(final URL url)
    {
        activity.startService(FeedParserService.getParserServiceIntent(activity,url));
    }

    private void showNews()
    {
        adapter = new NewsAdapter(activity, itemList);
        listView.setAdapter(adapter);
        if (0 == itemList.size()){
            listView.setEmptyView(activity.findViewById(R.id.emptyNewsList));
        }
        if (null != state){
            listView.onRestoreInstanceState(state);
        }
    }

    @Override
    public void onResume()
    {
        if(adapter != null){
            adapter.changeClickState();
        }
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, new IntentFilter(NewsScreenActivity.SEND_ITEM_LIST_MESSAGE));
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
