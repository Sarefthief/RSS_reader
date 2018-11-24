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

import java.util.ArrayList;

final class NewsScreen implements LifeCycleInterface
{
    private Parcelable state;
    private ListView listView;
    private final Activity activity;
    private ArrayList<FeedItem> itemList;
    private NewsAdapter adapter;
    private final ProgressBar progressBar;
    private final String urlToCheck;

    NewsScreen(final Activity activity, final String url)
    {
        this.activity = activity;
        urlToCheck = url;
        listView = activity.findViewById(R.id.newsList);
        progressBar = activity.findViewById(R.id.newsListProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        activity.startService(LoadNewsFromDatabaseService.getLoadNewsServiceIntent(activity, urlToCheck));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(final Context context, final Intent intent)
        {
            if (NewsActivity.LOAD_FROM_DATABASE_MESSAGE.equals(intent.getAction()))
            {
                if (urlToCheck.equals(intent.getStringExtra(NewsActivity.LINK_TO_CHECK)))
                {
                    itemList = intent.getParcelableArrayListExtra(NewsActivity.LOAD_FROM_DATABASE_MESSAGE);
                    if (0 != itemList.size())
                    {
                        progressBar.setVisibility(View.GONE);
                    }
                    setAdapter();
                    activity.startService(FeedParserService.getParserServiceIntent(activity, urlToCheck));
                }
            }
            if (NewsActivity.ADD_NEWS_FROM_PARSER_MESSAGE.equals(intent.getAction()))
            {
                if (urlToCheck.equals(intent.getStringExtra(NewsActivity.LINK_TO_CHECK)))
                {
                    final ArrayList<FeedItem> listToAdd = intent.getParcelableArrayListExtra(NewsActivity.ADD_NEWS_FROM_PARSER_MESSAGE);
                    itemList.addAll(0, listToAdd);
                    if (0 == itemList.size())
                    {
                        progressBar.setVisibility(View.GONE);
                        listView.setEmptyView(activity.findViewById(R.id.emptyNewsList));
                    }
                    setAdapter();
                }
            }
        }
    };

    private void setAdapter()
    {
        adapter = new NewsAdapter(activity, itemList);
        listView.setAdapter(adapter);
        if (null != state)
        {
            listView.onRestoreInstanceState(state);
        }
    }

    @Override
    public void onResume()
    {
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, new IntentFilter(NewsActivity.LOAD_FROM_DATABASE_MESSAGE));
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, new IntentFilter(NewsActivity.ADD_NEWS_FROM_PARSER_MESSAGE));
        if (adapter != null)
        {
            adapter.changeClickState();
        }
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
