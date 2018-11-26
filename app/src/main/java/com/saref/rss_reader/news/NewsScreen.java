package com.saref.rss_reader.news;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.saref.rss_reader.Constants;
import com.saref.rss_reader.LifeCycleInterface;
import com.saref.rss_reader.R;
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
            if (Constants.LOAD_FROM_DATABASE_MESSAGE.equals(intent.getAction()))
            {
                if (channelLink.equals(intent.getStringExtra(Constants.LINK_TO_CHECK)))
                {
                    itemList = intent.getParcelableArrayListExtra(Constants.LOAD_FROM_DATABASE_MESSAGE);
                    if (0 != itemList.size())
                    {
                        progressBar.setVisibility(View.GONE);
                    }
                    setAdapter();
                    startParserService();
                }
            }
            if (Constants.ADD_NEWS_FROM_PARSER_MESSAGE.equals(intent.getAction()))
            {
                if (channelLink.equals(intent.getStringExtra(Constants.LINK_TO_CHECK)))
                {
                    parserIsWorking = false;
                    progressBar.setVisibility(View.GONE);
                    final ArrayList<FeedItem> listToAdd = intent.getParcelableArrayListExtra(Constants.ADD_NEWS_FROM_PARSER_MESSAGE);
                    itemList.addAll(0, listToAdd);
                    if (0 == itemList.size())
                    {
                        listView.setEmptyView(activity.findViewById(R.id.emptyNewsList));
                    }
                    setAdapter();
                }
            }
            if (Constants.CHANNEL_IS_DELETED.equals(intent.getAction()))
            {
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                final boolean check = sharedPreferences.getBoolean(context.getString(R.string.alarmCheckBoxPreferenceKey), true);
                if(check){
                    activity.startService(SetAlarmsService.getAlarmsServiceIntent(activity));
                }
                activity.startActivity(ChannelsActivity.getChannelsActivityIntent(activity));
                activity.finish();
            }
            if (Constants.FEED_PARSER_ERROR.equals(intent.getAction()))
            {
                Toast.makeText(activity, intent.getStringExtra(Constants.FEED_PARSER_ERROR), Toast.LENGTH_LONG).show();
                parserIsWorking = false;
            }
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
        final IntentFilter filter = new IntentFilter(Constants.LOAD_FROM_DATABASE_MESSAGE);
        filter.addAction(Constants.ADD_NEWS_FROM_PARSER_MESSAGE);
        filter.addAction(Constants.CHANNEL_IS_DELETED);
        filter.addAction(Constants.FEED_PARSER_ERROR);
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, filter);
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
