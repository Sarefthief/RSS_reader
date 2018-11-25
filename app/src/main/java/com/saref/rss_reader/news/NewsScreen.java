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
import com.saref.rss_reader.alarms.AlarmReceiver;
import com.saref.rss_reader.alarms.SetAlarmsService;
import com.saref.rss_reader.channels.Channel;
import com.saref.rss_reader.channels.ChannelsActivity;
import com.saref.rss_reader.news.parser.FeedParserService;

import java.util.ArrayList;

final class NewsScreen implements LifeCycleInterface
{
    private Parcelable state;
    private ListView listView;
    private final Activity activity;
    private ArrayList<FeedItem> itemList;
    private final ProgressBar progressBar;
    private final String channelLink;
    private Channel channel;
    private boolean parserIsWorking = false;

    NewsScreen(final Activity activity, final Channel channel)
    {
        this.activity = activity;
        this.channel = channel;
        channelLink = channel.getLink();
        listView = activity.findViewById(R.id.newsList);
        progressBar = activity.findViewById(R.id.newsListProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        activity.startService(LoadNewsFromDatabaseService.getLoadNewsServiceIntent(activity, channelLink));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(final Context context, final Intent intent)
        {
            if (NewsActivity.LOAD_FROM_DATABASE_MESSAGE.equals(intent.getAction()))
            {
                if (channelLink.equals(intent.getStringExtra(NewsActivity.LINK_TO_CHECK)))
                {
                    itemList = intent.getParcelableArrayListExtra(NewsActivity.LOAD_FROM_DATABASE_MESSAGE);
                    if (0 != itemList.size())
                    {
                        progressBar.setVisibility(View.GONE);
                    }
                    setAdapter();
                    startParserService();
                }
            }
            if (NewsActivity.ADD_NEWS_FROM_PARSER_MESSAGE.equals(intent.getAction()))
            {
                if (channelLink.equals(intent.getStringExtra(NewsActivity.LINK_TO_CHECK)))
                {
                    parserIsWorking = false;
                    progressBar.setVisibility(View.GONE);
                    final ArrayList<FeedItem> listToAdd = intent.getParcelableArrayListExtra(NewsActivity.ADD_NEWS_FROM_PARSER_MESSAGE);
                    itemList.addAll(0, listToAdd);
                    if (0 == itemList.size())
                    {
                        listView.setEmptyView(activity.findViewById(R.id.emptyNewsList));
                    }
                    setAdapter();
                }
            }
            if (NewsActivity.CHANNEL_IS_DELETED.equals(intent.getAction()))
            {
                activity.finish();
                activity.startService(SetAlarmsService.getAlarmsServiceIntent(activity));
                activity.startActivity(ChannelsActivity.getChannelsActivityIntent(activity));
            }
            //TODO Ошибки, особождение переменной
        }
    };

    private void setAdapter()
    {
        final NewsAdapter adapter = new NewsAdapter(activity, itemList, channel);
        listView.setAdapter(adapter);
        if (null != state)
        {
            listView.onRestoreInstanceState(state);
        }
    }

    public void startParserService()
    {
        if(!parserIsWorking)
        {
            activity.startService(FeedParserService.getParserServiceIntent(activity, channelLink));
            parserIsWorking = true;
        }
    }

    @Override
    public void onResume()
    {
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, new IntentFilter(NewsActivity.LOAD_FROM_DATABASE_MESSAGE));
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, new IntentFilter(NewsActivity.ADD_NEWS_FROM_PARSER_MESSAGE));
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, new IntentFilter(NewsActivity.CHANNEL_IS_DELETED));
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
